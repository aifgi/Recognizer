package ru.aifgi.recognizer.model.neural_network.layers.impl;

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

import ru.aifgi.recognizer.model.MathUtil;
import ru.aifgi.recognizer.model.neural_network.layers.TwoDimensionalLayer;

/**
 * @author aifgi
 */

public class SubsamplingLayer extends AbstractLayer implements TwoDimensionalLayer {
    private final int myMaskSize;
    private double myWeight;
    private double myBias;

    public SubsamplingLayer(final int maskSize) {
        myMaskSize = maskSize;
        init();
    }

    @Override
    protected void init() {
        myBias = MathUtil.getRandom();
        myWeight = MathUtil.getRandom();
    }

    @Override
    public double[][] computeOutput(final double[][] input) {
        final double[][] result = subsampling(input);
        final int length = result.length;
        for (final double[] arr : result) {
            for (int i = 0; i < length; ++i) {
                arr[i] = myFunction.apply(arr[i] * myWeight + myBias);
            }
        }
        return result;
    }

    private double[][] subsampling(final double[][] input) {
        final int length = input.length;
        final int resultSize = length / myMaskSize;
        final double[][] result = new double[resultSize][resultSize];
        for (int i = 0; i < length; ++i) {
            final double[] arr = result[i / myMaskSize];
            for (int j = 0; j < resultSize; ++j) {
                arr[j / myMaskSize] += input[i][j];
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
}
