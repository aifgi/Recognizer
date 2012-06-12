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

import ru.aifgi.recognizer.api.neural_network.NeuralNetworkTrainInformation;
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
    public double[][] computeGradients(final double[][] layerOutput, final double[][] errors) {
        final int length = layerOutput.length;
        final double[][] gradients = new double[length][length];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                gradients[i][j] = errors[i][j] * myFunction.diff(layerOutput[i][j]);
            }
        }
        return gradients;
    }

    @Override
    public double[][] backPropagation(final double[][] gradients) {
        final int length = gradients.length;
        final int resSize = length * myMaskSize;
        final double[][] errors = new double[resSize][resSize];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                final double value = myWeight * gradients[i / myMaskSize][j / myMaskSize];
                for (int x = 0; x < myMaskSize; ++x) {
                    final int xPos = i * myMaskSize + x;
                    for (int y = 0; y < myMaskSize; ++y) {
                        errors[xPos][j * myMaskSize + y] = value;
                    }
                }
            }
        }
        return errors;
    }

    @Override
    public void updateWeights(final double[][] layerInput, final double[][] gradients,
                              final NeuralNetworkTrainInformation trainInformation) {
        final int length = gradients.length;
        double biasDelta = 0;
        double weightDelta = 0;
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                final double gradient = gradients[i][j];
                biasDelta += gradient;
                for (int x = 0; x < myMaskSize; ++x) {
                    final int xPos = i * myMaskSize + x;
                    for (int y = 0; y < myMaskSize; ++y) {
                        weightDelta += gradient * layerInput[xPos][j * myMaskSize + y];
                    }
                }
            }
        }

        final double learningRate = trainInformation.getLearningRate();
        myBias += learningRate * biasDelta;
        myWeight += learningRate * weightDelta;
    }
}
