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
import ru.aifgi.recognizer.api.neural_network.NeuralNetworkStructure;
import ru.aifgi.recognizer.api.neural_network.TrainSet;
import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.neural_network.layers.StageOutput;
import ru.aifgi.recognizer.model.neural_network.stages.ConvolutionalStage;
import ru.aifgi.recognizer.model.neural_network.stages.FullyConnectedStage;
import ru.aifgi.recognizer.model.neural_network.stages.Stage;

/**
 * @author aifgi
 */

public class NeuralNetworkImpl implements NeuralNetwork {
    private static class Input implements StageOutput {
        private final double[][][] myValue;

        private Input(final double[][] value) {
            myValue = new double[1][][];
            myValue[0] = value;
        }

        @Override
        public double[][][] getOutput3d() {
            return myValue;
        }

        @Override
        public double[] getOutput1d() {
            return ArrayUtil.toOneDimensionalArray(myValue);
        }

        @Override
        public boolean is3d() {
            return true;
        }

        @Override
        public boolean is1d() {
            return false;
        }
    }

    private final Stage[] myStages;
    private final int myInputSize;

    private final int myMinInput = 0;
    private final int myMaxInput = 255;

    public NeuralNetworkImpl(final NeuralNetworkStructure networkStructure) {
        int inputSize = networkStructure.getInputSize();
        myInputSize = inputSize;

        final NeuralNetworkStructure.StageStructure[] stageStructures = networkStructure.getStageStructures();
        final int stageStructuresLength = stageStructures.length;
        myStages = new Stage[stageStructuresLength + 1];
        for (int i = 0; i < stageStructuresLength; ++i) {
            final NeuralNetworkStructure.StageStructure stageStructure = stageStructures[i];
            final int convolutionalReceptiveFieldSize = stageStructure.getConvolutionalReceptiveFieldSize();
            final int subsamplingReceptiveFieldSize = stageStructure.getSubsamplingReceptiveFieldSize();
            myStages[i] = new ConvolutionalStage(stageStructure.getNumberOfFeatureMaps(),
                                                 convolutionalReceptiveFieldSize,
                                                 subsamplingReceptiveFieldSize);
            inputSize -= convolutionalReceptiveFieldSize - 1;
            inputSize /= subsamplingReceptiveFieldSize;
        }

        final int[] fullyConnectedLayersSizes = networkStructure.getFullyConnectedLayersSizes();
        myStages[stageStructuresLength] = new FullyConnectedStage(fullyConnectedLayersSizes, inputSize);
    }

    @Override
    public double[] computeOutput(final double[][] input) {
        final double[][] normalizedInput = normalize(input);
        final StageOutput stageOutput = new Input(normalizedInput);
        final StageOutput[] stageOutputs = forwardComputation(stageOutput);
        return stageOutputs[stageOutputs.length - 1].getOutput1d();
    }

    private StageOutput[] forwardComputation(final StageOutput input) {
        final StageOutput[] stageOutputs = new StageOutput[myStages.length + 1];

        stageOutputs[0] = input;
        forwardThroughStages(stageOutputs);

        return stageOutputs;
    }

    private void forwardThroughStages(final StageOutput[] stageOutputs) {
        final int stagesLength = myStages.length;
        for (int i = 0; i < stagesLength; i++) {
            final Stage stage = myStages[i];
            final StageOutput output = stage.getOutput(stageOutputs[i]);
            stageOutputs[i + 1] = output;
        }
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
