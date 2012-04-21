package ru.aifgi.recognizer.model.neural_network;
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

import ru.aifgi.recognizer.api.neural_network.Function;
import ru.aifgi.recognizer.model.MathUtil;

/**
 * @author aifgi
 */


public enum Functions implements Function {
    LOGISTIC("Logistic") {
        @Override
        public double apply(final double input) {
            return 1 / (1 + Math.exp(-input));
        }

        @Override
        public double diff(final double functionValue) throws UnsupportedOperationException {
            return functionValue * (1 - functionValue);
        }

        @Override
        public double getMaxValue() {
            return 1;
        }

        @Override
        public double getMinValue() {
            return 0;
        }
    },
    TANH("Hyperbolic tangent") {
        private static final double A = 1.71593428;
        private static final double B = 2 / 3.;

        @Override
        public double apply(final double input) {
            return A * MathUtil.fastTanh(input, B);
        }

        @Override
        public double diff(final double functionValue) throws UnsupportedOperationException {
            return B / A * (A - functionValue) * (A + functionValue);
        }

        @Override
        public double getMaxValue() {
            return A;
        }

        @Override
        public double getMinValue() {
            return -A;
        }
    };

    private final String myName;

    private Functions(final String name) {
        myName = name;
    }

    @Override
    public String getName() {
        return myName;
    }
}

