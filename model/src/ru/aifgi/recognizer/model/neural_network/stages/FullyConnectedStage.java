package ru.aifgi.recognizer.model.neural_network.stages;

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

import ru.aifgi.recognizer.api.neural_network.NeuralNetworkOutput;
import ru.aifgi.recognizer.api.neural_network.NeuralNetworkTrainInformation;
import ru.aifgi.recognizer.model.neural_network.layers.OneDimensionalLayer;
import ru.aifgi.recognizer.model.neural_network.layers.impl.FullyConnectedLayer;

/**
 * @author aifgi
 */

public class FullyConnectedStage implements Stage {
    private final OneDimensionalLayer[] myLayers;

    public FullyConnectedStage(final int[] fullyConnectedLayersSizes, final int inputSize) {
        int prevSize = inputSize;
        final int length = fullyConnectedLayersSizes.length;
        myLayers = new OneDimensionalLayer[length];
        for (int i = 0; i < length; ++i) {
            final int outputSize = fullyConnectedLayersSizes[i];
            myLayers[i] = new FullyConnectedLayer(prevSize, outputSize);
            prevSize = outputSize;
        }
    }

    @Override
    public void forwardComputation(final NeuralNetworkOutput neuralNetworkOutput) {
        double[] prevOutput = neuralNetworkOutput.get1d();
        for (final OneDimensionalLayer myLayer : myLayers) {
            prevOutput = myLayer.computeOutput(prevOutput);
            neuralNetworkOutput.pushBack(prevOutput);
        }
    }

    @Override
    public void backwardComputation(final NeuralNetworkTrainInformation trainInformation,
                                    final NeuralNetworkOutput networkOutput, final NeuralNetworkOutput errors) {
        final int length = myLayers.length - 1;
        for (int i = length; i >= 0; --i) {
            final OneDimensionalLayer layer = myLayers[i];
            final double[] layerOutput = networkOutput.get1d();
            final double[] gradients = layer.computeGradients(layerOutput, errors.get1d());
            networkOutput.previous();
            errors.pushFront(layer.backPropagation(gradients));
            final double[][] deltas = layer.computeDeltas(networkOutput.get1d(), gradients, trainInformation);
            layer.updateWeights(deltas);
        }
    }
}
