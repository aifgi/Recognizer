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
import ru.aifgi.recognizer.model.neural_network.layers.OneDimensionalLayer;

/**
 * @author aifgi
 */

public class FullyConnectedLayer extends AbstractLayer implements OneDimensionalLayer {
    private double[][] myWeights;

    public FullyConnectedLayer(final int prevLayerSize, final int layerSize) {
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
    public double[][] computeDeltas(final double[] layerInput, final double[] layerOutput,
                                    final double[] errors, final double learningRate) {
        final int length = errors.length;
        final int inputLength = layerInput.length + 1;
        final double[][] deltas = new double[length][inputLength];
        for (int i = 0; i < length; ++i) {
            final double[] delta = deltas[i];
            final double v = learningRate * errors[i] * myFunction.diff(layerOutput[i]);

            final int t = inputLength - 1;
            for (int j = 0; j < t; ++j) {
                delta[j] = v * layerInput[j];
            }
            delta[t] = v;
        }
        return deltas;
    }

    @Override
    public double[] backPropagation(final double[] errors) {
        final int length = myWeights.length;
        final int inputLength = myWeights[0].length;

        final double[] weightedSum = new double[inputLength];
        for (int i = 0; i < length; ++i) {
            final double[] weights = myWeights[i];

            for (int j = 0; j < inputLength; ++j) {
                weightedSum[j] += errors[i] * weights[j];
            }
        }
        return weightedSum;
    }

    @Override
    public void updateWeights(final double[][] deltas, final double regularizationParameter) {
        final int length = myWeights.length;
        final int inputLength = myWeights[0].length;
        for (int i = 0; i < length; ++i) {
            final double[] weights = myWeights[i];
            final double[] delta = deltas[i];

            final int l = inputLength - 1;
            for (int j = 0; j < l; ++j) {
                weights[j] += delta[j] + computeRegularization(regularizationParameter, weights[j]);
            }
            weights[l] += delta[l];
        }
    }

    private double computeRegularization(final double regularizationParameter, final double weight) {
        final double w0_2 = MathUtil.sqr(regularizationParameter);
        return 2 * (w0_2 * weight) / MathUtil.sqr(w0_2 + MathUtil.sqr(weight));
    }

    @Override
    public void updateWeights(final double[][] deltas) {
        updateWeights(deltas, 0);
    }
}
