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

import com.google.common.math.DoubleMath;
import ru.aifgi.recognizer.api.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;

/**
 * @author aifgi
 */
// TODO: delete it
public class ImageUtil {
    public static double[][] readImage(final InputStream inputStream) {
        try {
            final BufferedImage image = ImageIO.read(inputStream);
            return computeBrightnesses(image);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeImage(final double[][] brightnesses, final File file) {
        final int width = brightnesses.length;
        final int height = brightnesses[0].length;
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                final int rgb = 0xFFFF * DoubleMath.roundToInt(1 - brightnesses[i][j], RoundingMode.HALF_UP);
                bufferedImage.setRGB(i, j, rgb);
            }
        }
        writeImage(bufferedImage, file);
    }

    public static void writeImage(final RenderedImage image, final File file) {
        try {
            ImageIO.write(image, "PNG", file);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeImage(final double[][] input, final Rectangle word, final File file) {
        final int width = word.getWidth();
        final int height = word.getHeight();
        final double[][] res = new double[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                res[x][y] = input[x + word.getX()][y + word.getY()];
            }
        }
        writeImage(res, file);
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

    private ImageUtil() {
    }
}
