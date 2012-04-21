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
import ru.aifgi.recognizer.api.neural_network.NeuralNetwork;
import ru.aifgi.recognizer.api.neural_network.TrainSet;
import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.neural_network.layers.OneDimensionalLayer;
import ru.aifgi.recognizer.model.neural_network.layers.impl.FullyConnectedLayer;

/**
 * @author aifgi
 */

public class NeuralNetworkImpl implements NeuralNetwork {
    private final Stage[] myStages;
    private final OneDimensionalLayer myOutputLayer;
    private final int myInputSize;

    private final int myMinInput = 0;
    private final int myMaxInput = 255;

    public NeuralNetworkImpl(final int stageNumber, final int inputSize) {
        myInputSize = inputSize;
        myStages = new Stage[stageNumber];
        for (Stage stage : myStages) {
            stage = new Stage(4, 5, 2);
        }
        myOutputLayer = new FullyConnectedLayer(40, 40);
    }

    // TODO: merge network output with stageOutputs
    @Override
    public double[] computeOutput(final double[][] input) {
        final double[][] normalizedInput = normalize(input);
        final Stage.StageOutput stageOutput = new Stage.StageOutput(normalizedInput);
        final Stage.StageOutput[] stageOutputs = forwardComputation(stageOutput);
        final double[] doubles = ArrayUtil.toOneDimensionalArray(stageOutputs[myStages.length].getStageOutput());
        final double[] networkOutput = myOutputLayer.computeOutput(doubles);
        return networkOutput;
    }

    private Stage.StageOutput[] forwardComputation(final Stage.StageOutput input) {
        final int length = myStages.length;
        final Stage.StageOutput[] stageOutputs = new Stage.StageOutput[length + 1];
        stageOutputs[0] = input;
        for (int i = 0; i < length; i++) {
            final Stage stage = myStages[i];
            final Stage.StageOutput output = stage.getOutput(stageOutputs[i]);
            stageOutputs[i + 1] = output;
        }
        return stageOutputs;
    }

    private double[][] normalize(final double[][] input) {
        final Function function = Functions.TANH;
        final double fr = (function.getMaxValue() - function.getMinValue()) / 10;
        final double functionMin = function.getMinValue() + fr;
        final double functionMax = function.getMaxValue() - fr;

        final double[][] res = new double[input.length][input.length];
        final double t = (functionMax - functionMin) / (myMaxInput - myMinInput);
        for (int i = 0; i < input.length; ++i) {
            final double[] v = input[i];
            for (int j = 0; j < input.length; ++j) {
                res[i][j] = t * v[j] + (functionMax - t * myMaxInput);
            }
        }
        return res;
    }

    @Override
    public void train(final TrainSet trainSet) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
