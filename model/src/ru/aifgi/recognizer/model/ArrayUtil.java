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

/**
 * @author aifgi
 */

public class ArrayUtil {
    private ArrayUtil() {
    }

    public static double[] toOneDimensionalArray(final double[][][] array) {
        final int length1 = array.length;
        final int length2 = array[0].length;
        final int length3 = array[0][0].length;
        final double[] res = new double[length1 * length2 * length3];
        for (int i = 0; i < length1; ++i) {
            for (int j = 0; j < length2; ++j) {
                for (int k = 0; k < length3; ++k) {
                    res[i * length1 + j * length2 + k] = array[i][j][k];
                }
            }
        }
        return res;
    }
}
