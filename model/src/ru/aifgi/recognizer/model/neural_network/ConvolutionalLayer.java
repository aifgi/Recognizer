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
import ru.aifgi.recognizer.api.neural_network.Functions;

/**
 * @author aifgi
 */

public class ConvolutionalLayer implements Layer {
    private final double[][] myConvolutionalMask;
    private Function myFunction = Functions.TANH;
    private double myBias;

    public ConvolutionalLayer(final int maskSize) {
        myConvolutionalMask = new double[maskSize][maskSize];
    }

    @Override
    public double[][] computeOutput(final double[][] input) {
        final double[][] output = convolution(input);
        final int length = output.length;
        for (final double[] arr : output) {
            for (int i = 0; i < length; i++) {
                arr[i] = myFunction.apply(arr[i] + myBias);
            }
        }
        return output;
    }

    private double[][] convolution(final double[][] input) {
        final int maskSize = myConvolutionalMask.length;
        final int resultSize = input.length - maskSize + 1;
        final double[][] result = new double[resultSize][resultSize];
        for (int i = 0; i < resultSize; ++i) {
            final double[] arr = result[i];
            for (int j = 0; j < resultSize; ++j) {
                double value = 0;
                for (int c = 0; c < maskSize; ++c) {
                    for (int k = 0; k < maskSize; ++k) {
                        value += input[i + c][j + k] * myConvolutionalMask[c][k];
                    }
                }
                arr[j] = value;
            }
        }
        return result;
    }

    @Override
    public double[][] backPropagation(final double[][] gradients) {
        return null;
    }

    @Override
    public double[][] updateWeights(final double[][] deltas) {
        return new double[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double[][] updateWeights(final double[][] deltas, final double regularizationParameter) {
        return new double[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Function getFunction() {
        return myFunction;
    }

    @Override
    public void setFunction(final Function function) {
        myFunction = function;
    }
}
