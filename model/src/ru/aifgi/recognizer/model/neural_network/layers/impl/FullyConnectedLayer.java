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

import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.MathUtil;
import ru.aifgi.recognizer.model.neural_network.layers.LayerOutput;
import ru.aifgi.recognizer.model.neural_network.layers.OneDimensionalLayer;

/**
 * @author aifgi
 */

public class FullyConnectedLayer extends AbstractLayer implements OneDimensionalLayer {
    public static class LayerOutputImpl implements LayerOutput {
        private final double[] myOutput;

        public LayerOutputImpl(final double[] output) {
            myOutput = output;
        }

        public LayerOutputImpl(final double[][][] output) {
            myOutput = ArrayUtil.toOneDimensionalArray(output);
        }

        @Override
        public double[][][] getOutput3d() {
            final double[][][] doubles = new double[1][1][];
            doubles[0][0] = myOutput;
            return doubles;
        }

        @Override
        public double[] getOutput1d() {
            return myOutput;
        }

        @Override
        public boolean is3d() {
            return false;
        }

        @Override
        public boolean is1d() {
            return true;
        }
    }

    private double[][] myWeights;

    public FullyConnectedLayer(final int prevLayerSize, final int layerSize) {
        super();
        myWeights = new double[layerSize][prevLayerSize + 1];
        init();
    }

    @Override
    public int getDimension() {
        return myWeights.length;
    }

    @Override
    protected void init() {
        for (double[] weights : myWeights) {
            final double length = weights.length;
            for (int i = 0; i < length; ++i) {
                weights[i] = 0.6 * Math.random() - 0.3;
            }
        }
    }

    @Override
    public double[] computeOutput(final double[] input) {
        final double[] inducedLocalFields = computeInducedLocalField(input);
        final int length = myWeights.length;
        final double[] res = new double[length];
        for (int i = 0; i < myWeights.length; i++) {
            res[i] = myFunction.apply(inducedLocalFields[i]);
        }
        return res;
    }

    private double[] computeInducedLocalField(final double[] input) {
        final int length = myWeights.length;
        final int inputLength = input.length;

        final double[] inducedLocalFields = new double[length];
        for (int i = 0; i < length; i++) {
            final double[] weights = myWeights[i];

            // weights[inputLength] is threshold
            double inducedLocalField = weights[inputLength];
            for (int j = 0; j < inputLength; ++j) {
                inducedLocalField += input[j] * weights[j];
            }
            inducedLocalFields[i] = inducedLocalField;
        }
        return inducedLocalFields;
    }

    @Override
    public double[] backPropagation(final double[] gradients) {
        final int length = myWeights.length;
        final int inputLength = myWeights[0].length;

        final double[] weightedSum = new double[inputLength];
        for (int i = 0; i < length; ++i) {
            final double[] weights = myWeights[i];

            for (int j = 0; j < inputLength; ++j) {
                weightedSum[j] += gradients[i] * weights[j];
            }
        }
        return weightedSum;
    }

    @Override
    public double[][] updateWeights(final double[][] deltas, final double regularizationParameter) {
        final int length = myWeights.length;
        final int inputLength = myWeights[0].length;
        final double[][] oldWeights = copyWeights(inputLength, length);
        for (int i = 0; i < length; ++i) {
            final double[] weights = myWeights[i];
            final double[] delta = deltas[i];

            final int l = inputLength - 1;
            for (int j = 0; j < l; ++j) {
                weights[j] += delta[j] + computeRegularization(regularizationParameter, weights[j]);
            }
            weights[l] += delta[l];
        }
        return oldWeights;
    }

    private double computeRegularization(final double regularizationParameter, final double weight) {
        final double w0_2 = MathUtil.sqr(regularizationParameter);
        return 2 * (w0_2 * weight) / MathUtil.sqr(w0_2 + MathUtil.sqr(weight));
    }

    @Override
    public double[][] updateWeights(final double[][] deltas) {
        return updateWeights(deltas, 0);
    }

    private double[][] copyWeights(final int inputLength, final int length) {
        final double[][] oldWeights = new double[length][inputLength];
        for (int i = 0; i < length; ++i) {
            System.arraycopy(myWeights[i], 0, oldWeights[i], 0, inputLength);
        }
        return oldWeights;
    }

}
