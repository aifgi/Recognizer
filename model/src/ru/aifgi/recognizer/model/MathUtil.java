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

public class MathUtil {
    private final static double PR = 2 / 3.;
    private final static double A0 = 1.0;
    private final static double A1 = 0.125 * PR;
    private final static double A2 = 0.0078125 * PR * PR;
    private final static double A3 = 0.000325520833333 * PR * PR * PR;
    private final static double THRESHOLD = 19.5;

    public static int sqr(final int value) {
        return value * value;
    }

    public static double sqr(final double value) {
        return value * value;
    }

    public static double fastTanh(final double input) {
        return fastTanh(input, 1);
    }

    // Return the hyperbolic tangent of (b * input)
    public static double fastTanh(final double input, final double b) {
        final double a1;
        final double a2;
        final double a3;
        if (Math.abs(b - PR) < 0.001) {
            a1 = A1;
            a2 = A2;
            a3 = A3;
        }
        else {
            a1 = 0.125 * b;
            a2 = 0.0078125 * sqr(b);
            a3 = 0.000325520833333 * b * sqr(b);
        }
        double y;

        if (input >= 0.0) {
            if (input < THRESHOLD) {
                y = A0 + input * (a1 + input * (a2 + input * (a3)));
            }
            else {
                return 1;
            }
        }
        else {
            if (input > -THRESHOLD) {
                y = A0 - input * (a1 - input * (a2 - input * (a3)));
            }
            else {
                return -1;
            }
        }

        y *= y;
        y *= y;
        y *= y;
        y *= y;

        return ((input > 0.0) ? (y - 1.0) : (1.0 - y)) / (y + 1.0);
    }

    private MathUtil() {
    }
}
