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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author aifgi
 */
// TODO: delete it
public class ImageReader {
    public static double[][] readImage(final InputStream inputStream) {
        try {
            final BufferedImage image = ImageIO.read(inputStream);
            return computeBrightnesses(image);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static double[][] computeBrightnesses(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final double[][] brightnesses = new double[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                brightnesses[i][j] = getBrightness(image, i, j);
            }
        }
        return brightnesses;
    }

    private static double getBrightness(final BufferedImage image, final int x, final int y) {
        final int rgb = image.getRGB(x, y);
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

    private ImageReader() {
    }
}
