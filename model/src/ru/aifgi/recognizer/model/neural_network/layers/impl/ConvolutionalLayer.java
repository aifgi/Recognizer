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

public class ConvolutionalLayer extends AbstractLayer implements TwoDimensionalLayer {
    private final double[][] myConvolutionalMask;
    private double myBias;

    public ConvolutionalLayer(final int maskSize) {
        myConvolutionalMask = new double[maskSize][maskSize];
        init();
    }

    @Override
    protected void init() {
        myBias = MathUtil.getRandom();
        for (final double[] a : myConvolutionalMask) {
            for (int i = 0; i < myConvolutionalMask.length; ++i) {
                a[i] = MathUtil.getRandom();
            }
        }
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
        final int gradientsLength = gradients.length;
        final int maskSize = myConvolutionalMask.length;
        final int resSize = gradientsLength + maskSize - 1;
        final double[][] errors = new double[resSize][resSize];
        for (int i = 0; i < gradientsLength; ++i) {
            for (int j = 0; j < gradientsLength; ++j) {
                final double value = gradients[i][j];
                for (int c = 0; c < maskSize; ++c) {
                    for (int k = 0; k < maskSize; ++k) {
                        errors[i + c][i + k] += myConvolutionalMask[c][k] * value;
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
        final int maskSize = myConvolutionalMask.length;
        final double[][] maskDeltas = new double[maskSize][maskSize];
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                final double gradient = gradients[i][j];
                biasDelta += gradient;
                for (int c = 0; c < maskSize; ++c) {
                    for (int k = 0; k < maskSize; ++k) {
                        maskDeltas[c][k] += gradient * layerInput[i + c][j + k];
                    }
                }
            }
        }

        final double learningRate = trainInformation.getLearningRate();
        myBias += learningRate * biasDelta;
        for (int c = 0; c < maskSize; ++c) {
            for (int k = 0; k < maskSize; ++k) {
                myConvolutionalMask[c][k] += learningRate * maskDeltas[c][k];
            }
        }
    }
}
