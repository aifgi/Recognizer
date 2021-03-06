package ru.aifgi.recognizer.view;

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

import ru.aifgi.recognizer.api.ImageWrapper;

import java.awt.image.BufferedImage;

/**
 * @author aifgi
 */

public class BufferedImageWrapper implements ImageWrapper {
    private final BufferedImage myBufferedImage;

    public BufferedImageWrapper(final BufferedImage bufferedImage) {
        myBufferedImage = bufferedImage;
    }

    @Override
    public int getWidth() {
        return myBufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return myBufferedImage.getHeight();
    }

    @Override
    public double[][] getBrightnesses() {
        return computeBrightnesses();
    }

    // TODO: remove + 5 and +1 !!!
    private double[][] computeBrightnesses() {
        final int width = myBufferedImage.getWidth();
        final int height = myBufferedImage.getHeight();
        final double[][] brightnesses = new double[width + 10][height + 2];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                brightnesses[i + 5][j + 1] = getBrightness(i, j);
            }
        }
        return brightnesses;
    }

    private double getBrightness(final int x, final int y) {
        final int rgb = myBufferedImage.getRGB(x, y);
        final int red = getRed(rgb);
        final int green = getGreen(rgb);
        final int blue = getBlue(rgb);
        return 255 - (0.299 * red + 0.587 * green + 0.114 * blue);
    }

    private static int getRed(final int rgb) {
        return doGet(rgb, 16);
    }

    private static int getGreen(final int rgb) {
        return doGet(rgb, 8);
    }

    private static int getBlue(final int rgb) {
        return doGet(rgb, 0);
    }

    private static int doGet(final int rgb, final int s) {
        return (rgb >> s) & 0xFF;
    }
}
