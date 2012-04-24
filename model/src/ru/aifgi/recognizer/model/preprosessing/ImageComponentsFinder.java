package ru.aifgi.recognizer.model.preprosessing;

/*
 * Copyright 2012 Alexey Ivanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import ru.aifgi.recognizer.api.Rectangle;

import java.util.*;

/**
 * @author aifgi
 */

public class ImageComponentsFinder {
    private static class RectangleImpl implements Rectangle {
        private int x1;
        private int y1;
        private int x2;
        private int y2;

        public RectangleImpl(final int x, final int y) {
            x1 = x2 = x;
            y1 = y2 = y;
        }

        public void update(final int x, final int y) {
            if (x < x1) {
                x1 = x;
            }
            else if (x > x2) {
                x2 = x;
            }
            if (y < y1) {
                y1 = y;
            }
            else if (y > y2) {
                y2 = y;
            }
        }

        public void expand(final int horizontal, final int vertical) {
            x1 -= horizontal;
            y1 -= vertical;
            x2 += horizontal;
            y2 += vertical;
        }

        public void union(final RectangleImpl another) {
            x1 = Math.min(x1, another.x1);
            y1 = Math.min(y1, another.y1);
            x2 = Math.max(x2, another.x2);
            y2 = Math.max(y2, another.y2);
        }

        @Override
        public int getX() {
            return x1;
        }

        @Override
        public int getY() {
            return y1;
        }

        @Override
        public int getWidth() {
            return x2 - x1;
        }

        @Override
        public int getHeight() {
            return y2 - y1;
        }
    }

    private final double[][] myImage;
    private final int[][] myMask;

    public ImageComponentsFinder(final double[][] image) {
        myImage = image;
        myMask = new int[myImage.length][myImage[0].length];
        computeMask();
    }

    // TODO: test
    private void computeMask() {
        final int width = myImage.length;
        final int height = myImage[0].length;
        int count = 0;
        final Map<Integer, Integer> mergedComponents = new HashMap<>();
        for (int i = 0; i < width; ++i) {
            final int[] line = myMask[i];
            for (int j = 0; j < height; ++j) {
                if (myImage[i][j] != 0) {
                    final int value = findMinimumNeighbour(i, j, mergedComponents);
                    line[j] = (value == 0) ? ++count : value;
                }
            }
        }

        for (int i = 0; i < width; ++i) {
            final int[] line = myMask[i];
            for (int j = 0; j < height; ++j) {
                int t = line[j];
                if (t != 0) {
                    final Integer component = mergedComponents.get(t);
                    if (component != null) {
                        line[j] = component;
                    }
                }
            }
        }
    }

    // TODO: optimize
    private int findMinimumNeighbour(final int x, final int y, final Map<Integer, Integer> mergedComponents) {
        int res = Integer.MAX_VALUE;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                final int newValue = getValue(x + i, y + j);
                if (newValue != 0) {
                    if (newValue < res) {
                        mergedComponents.put(res, newValue);
                        res = newValue;
                    }
                    else if (newValue != res) {
                        mergedComponents.put(newValue, res);
                    }
                }

            }
        }
        return (res != Integer.MAX_VALUE) ? res : 0;
    }

    private int getValue(final int x, final int y) {
        if (x < 0 || y < 0 || x >= myImage.length || y >= myImage[0].length) {
            return 0;
        }
        return myMask[x][y];
    }

    public int[][] getConnectedComponentsMask() {
        return myMask;
    }

    public Collection<Rectangle> getWords() {
        return Collections.unmodifiableCollection(mergeComponentsToWords());
    }

    private Collection<? extends Rectangle> mergeComponentsToWords() {
        final Map<Integer, RectangleImpl> boundingBoxes = buildBoundingBoxes();
        return expandAndMergeRectangles(boundingBoxes, 3, 1);
    }

    private Map<Integer, RectangleImpl> buildBoundingBoxes() {
        final Map<Integer, RectangleImpl> boundingBoxes = new HashMap<>();
        final int width = myMask.length;
        final int height = myMask[0].length;
        for (int x = 0; x < width; ++x) {
            final int[] line = myMask[x];
            for (int y = 0; y < height; ++y) {
                final int componentLabel = line[y];
                if (componentLabel != 0) {
                    final RectangleImpl rectangle = boundingBoxes.get(componentLabel);
                    if (rectangle == null) {
                        boundingBoxes.put(componentLabel, new RectangleImpl(x, y));
                    }
                    else {
                        rectangle.update(x, y);
                    }
                }
            }
        }
        return boundingBoxes;
    }

    private Collection<? extends Rectangle> expandAndMergeRectangles(final Map<Integer, RectangleImpl> boxes,
                                                                     final int horizontalExpansion,
                                                                     final int verticalExpansion) {
        final Set<Integer> keys = boxes.keySet();
        final Set<Integer> removed = new HashSet<>();

        final int[][] mask = new int[myMask.length][myMask[0].length];
        for (final int label : keys) {
            if (!removed.contains(label)) {
                final RectangleImpl rectangle = boxes.get(label);
                rectangle.expand(horizontalExpansion, verticalExpansion);

                boolean merged;
                do {
                    merged = false;

                    loop:
                    for (int x = rectangle.x1; x <= rectangle.x2; ++x) {
                        for (int y = rectangle.y1; y <= rectangle.y2; ++y) {
                            final int anotherLabel = mask[x][y];
                            mask[x][y] = label;
                            if (anotherLabel != 0 && anotherLabel != label) {
                                if (!removed.contains(anotherLabel)) {
                                    final RectangleImpl anotherRectangle = boxes.get(anotherLabel);
                                    rectangle.union(anotherRectangle);
                                    removed.add(anotherLabel);
                                    merged = true;
                                    break loop;
                                }
                            }
                        }
                    }
                }
                while (merged);
            }
        }

        keys.removeAll(removed);

        return boxes.values();
    }
}
