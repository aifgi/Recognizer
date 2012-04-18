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

/**
 * @author aifgi
 */

public class GaussianFilter extends AbstractImageFilter {
    private static final int[][] MASK = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };
    private static final int NORMALIZER;

    static {
        int norm = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                norm += MASK[i][j];
            }
        }
        NORMALIZER = norm;
    }

    @Override
    protected double applyImpl(final double[][] inputImage, final int x, final int y) {
        double res = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                final int s = x + i - 1;
                final int t = y + j - 1;
                res += getValue(inputImage, s, t) * MASK[i][j];
            }
        }
        return res / NORMALIZER;
    }
}
