package ru.aifgi.recognizer.model.test.images;

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

import java.awt.image.BufferedImage;
import java.math.RoundingMode;

/**
 * @author aifgi
 */

public class ImageUtil {
    public static boolean equals(final BufferedImage image1, final BufferedImage image2) {
        final int width1 = image1.getWidth();
        final int width2 = image2.getWidth();
        if (width1 != width2) {
            return false;
        }

        final int height1 = image1.getHeight();
        final int height2 = image2.getHeight();
        if (height1 != height2) {
            return false;
        }

        for (int i = 0; i < width1; ++i) {
            for (int j = 0; j < height1; ++j) {
                if (image1.getRGB(i, j) != image2.getRGB(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static BufferedImage createBufferedImage(final int[][] brightnesses) {
        final int width = brightnesses.length;
        final int height = brightnesses[0].length;
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                bufferedImage.setRGB(i, j, 0x0F << (brightnesses[i][j] % 6));
            }
        }
        return bufferedImage;
    }

    public static BufferedImage createBufferedImage(final double[][] brightnesses) {
        final int width = brightnesses.length;
        final int height = brightnesses[0].length;
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                bufferedImage.setRGB(i, j, DoubleMath.roundToInt(brightnesses[i][j], RoundingMode.HALF_UP));
            }
        }
        return bufferedImage;
    }

    private ImageUtil() {
    }
}
