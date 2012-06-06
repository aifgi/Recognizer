package ru.aifgi.recognizer.model;

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
import ru.aifgi.recognizer.api.graph.Graph;
import ru.aifgi.recognizer.model.viterbi.ViterbiAlgorithm;

/**
 * @author aifgi
 */

public class WordRecognizer {
    private final double[][] myInputImage;
    private final Labels myLabels;

    public WordRecognizer(final double[][] inputImage, final Labels labels) {
        myInputImage = inputImage;
        myLabels = labels;
    }

    public String recognize(final Rectangle wordRectangle) {
        final double[][] word = getWord(wordRectangle);
        final Graph graph = buildGraph(word);
        final int[] labels = ViterbiAlgorithm.getLabels(graph);
        return buildRecognitionResult(labels);
    }

    // TODO: delete hardcoded 20
    private double[][] getWord(final Rectangle wordRectangle) {
        final int x = wordRectangle.getX();
        final int y = wordRectangle.getY();
        final int width = wordRectangle.getWidth();
        final int height = wordRectangle.getHeight();
        final int annInputHeight = 20;
        final double[][] res;
        if (height <= annInputHeight) {
            res = new double[width][annInputHeight];
            final int topMargin = (annInputHeight - height) / 2;
            for (int i = 0; i < width; ++i) {
                final int lineNumber = i + x;
                System.arraycopy(myInputImage[lineNumber], y, res[i], topMargin, height);
            }
        }
        else {
            final double scale = ((double) annInputHeight) / height;
            final double newWidth = width * scale;
            res = new double[(int) newWidth][annInputHeight];
            for (int i = 0; i < newWidth; ++i) {
                for (int j = 0; j < annInputHeight; ++j) {
                    res[i][j] = myInputImage[((int) (i * scale))][((int) (j * scale))];
                }
            }
        }
        return res;
    }

    private Graph buildGraph(final double[][] word) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private String buildRecognitionResult(final int[] labels) {
        final StringBuilder builder = new StringBuilder(labels.length);
        for (final int label : labels) {
            builder.append(myLabels.getCharForLabel(label));
        }
        return builder.toString();
    }
}
