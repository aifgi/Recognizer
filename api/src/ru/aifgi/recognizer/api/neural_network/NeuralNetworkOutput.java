package ru.aifgi.recognizer.api.neural_network;

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
public interface NeuralNetworkOutput {
    double[][] first();

    double[] last();

    boolean hasNext();

    boolean hasPrevious();

    void next();

    void previous();

    double[] get1d();

    double[][][] get3d();

    double[][][] get3d(final int size);

    void pushBack(final double[] value);

    void pushBack(final double[][][] value);

    void pushFront(final double[] value);

    void pushFront(final double[][][] value);

    NeuralNetworkOutput begin();

    NeuralNetworkOutput end();
}
