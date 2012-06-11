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

import com.google.common.base.Preconditions;
import ru.aifgi.recognizer.api.neural_network.NeuralNetworkOutput;
import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.MathUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author aifgi
 */
public class NeuralNetworkOutputImpl implements NeuralNetworkOutput {
    private final List<double[][][]> myThreeDimensionalArrays = new LinkedList<>();
    private final List<double[]> myOneDimensionalArrays = new LinkedList<>();

    private int mySize;
    private int myPosition;

    public NeuralNetworkOutputImpl(final double[][] inputVector) {
        final double[][][] t = new double[1][][];
        t[0] = inputVector;
        myThreeDimensionalArrays.add(t);
    }

    @Override
    public double[][] first() {
        final double[][][] doubles = myThreeDimensionalArrays.get(0);
        return doubles[0];
    }

    @Override
    public double[] last() {
        return myOneDimensionalArrays.get(myOneDimensionalArrays.size() - 1);
    }

    @Override
    public boolean hasNext() {
        return myPosition + 1 < mySize;
    }

    @Override
    public boolean hasPrevious() {
        return myPosition > 0;
    }

    @Override
    public void next() {
        ++myPosition;
    }

    @Override
    public void previous() {
        --myPosition;
    }

    @Override
    public double[] get1d() {
        final int size = myThreeDimensionalArrays.size();
        if (myPosition < size) {
            return ArrayUtil.toOneDimensionalArray(myThreeDimensionalArrays.get(myPosition));
        }
        return myOneDimensionalArrays.get(myPosition - size);
    }

    @Override
    public double[][][] get3d() {
        final int size = myThreeDimensionalArrays.size();
        if (myPosition < size) {
            return myThreeDimensionalArrays.get(myPosition);
        }
        return ArrayUtil.wrapTo3d(myOneDimensionalArrays.get(myPosition - size));
    }

    @Override
    public double[][][] get3d(final int size) {
        final int s = myThreeDimensionalArrays.size();
        if (myPosition < s) {
            final double[][][] doubles = myThreeDimensionalArrays.get(myPosition);
            if (doubles[0].length != size) {
                throw new IllegalStateException();
            }
            return doubles;
        }

        final int sqr = MathUtil.sqr(size);
        final double[] doubles = myOneDimensionalArrays.get(myPosition - s);
        final int t = doubles.length / sqr;
        Preconditions.checkState(doubles.length % sqr == 0);
        final double[][][] res = new double[t][size][size];
        for (int i = 0; i < t; ++i) {
            for (int j = 0; j < size; ++j) {
                for (int k = 0; k < size; ++k) {
                    res[i][j][k] = doubles[i * t + j * size + k];
                }
            }
        }
        return res;
    }

    @Override
    public void pushBack(final double[] value) {
        myOneDimensionalArrays.add(value);
        ++mySize;
        myPosition = myThreeDimensionalArrays.size() + myOneDimensionalArrays.size() - 1;
    }

    @Override
    public void pushBack(final double[][][] value) {
        myThreeDimensionalArrays.add(value);
        ++mySize;
        myPosition = myThreeDimensionalArrays.size() - 1;
    }

    @Override
    public void pushFront(final double[] value) {
        myOneDimensionalArrays.add(0, value);
        ++mySize;
        myPosition = myThreeDimensionalArrays.size() - 1;
    }

    @Override
    public void pushFront(final double[][][] value) {
        myThreeDimensionalArrays.add(0, value);
        ++mySize;
        myPosition = 0;
    }

    @Override
    public NeuralNetworkOutput begin() {
        myPosition = -1;
        return this;
    }

    @Override
    public NeuralNetworkOutput end() {
        myPosition = mySize;
        return this;
    }
}
