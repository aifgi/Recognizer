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

import ru.aifgi.recognizer.api.neural_network.*;
import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.MathUtil;
import ru.aifgi.recognizer.model.neural_network.layers.StageOutput;
import ru.aifgi.recognizer.model.neural_network.stages.ConvolutionalStage;
import ru.aifgi.recognizer.model.neural_network.stages.FullyConnectedStage;
import ru.aifgi.recognizer.model.neural_network.stages.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aifgi
 */

public class NeuralNetworkImpl implements NeuralNetwork, Normalizer {
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

    private class Trainer {
        private final TrainingSet myTrainingSet;
        private final List<Double> myErrors = new ArrayList<>();
        private final double[][] myRightAnswers;
        private double myRateOfLearning;

        public Trainer(final TrainingSet trainingSet, final double rateOfLearning) {
            myTrainingSet = trainingSet;
            myTrainingSet.normalize(NeuralNetworkImpl.this);
            myRateOfLearning = rateOfLearning;

            final int numberOfClusters = myTrainingSet.getNumberOfClusters();
            myRightAnswers = new double[numberOfClusters][numberOfClusters];
            for (int i = 0; i < numberOfClusters; ++i) {
                myRightAnswers[i][i] = 1;
            }
        }

        public TrainingResult train() {
            final double initialRateLearning = myRateOfLearning;
            double prevError;
            double error = Double.MAX_VALUE;
            int count = 0;
            double deltaError;
            do {
                prevError = error;
                error = epoch();
                ++count;
                myErrors.add(error);
                deltaError = Math.abs(prevError - error);
                myRateOfLearning = initialRateLearning / (1 + count / 10.);
            }
            while (deltaError > 0.01 || deltaError / prevError > 0.001 || count > 500);
            return new TrainingResultImpl(myErrors);
        }

        private double epoch() {
            myTrainingSet.shuffle();

            double averageError = 0;
            for (final TrainElement element : myTrainingSet) {
                final StageOutput inputVector = makeInput(element.getData());
                final StageOutput[] layersOutputs = forwardComputation(inputVector);
                final double[] rightAnswer = myRightAnswers[element.getLabel()];
                backwardComputation(layersOutputs, rightAnswer);

                final double[] networkOutput = layersOutputs[layersOutputs.length - 1].getOutput1d();
                final double error = computeError(rightAnswer, networkOutput);
                averageError += error;
            }
            return averageError / myTrainingSet.size();
        }

        private void backwardComputation(final StageOutput[] layersOutputs, final double[] rightAnswer) {
            //To change body of created methods use File | Settings | File Templates.
        }

        private double computeError(final double[] rightAnswer, final double[] networkOutput) {
            double error = 0;
            final int length = rightAnswer.length;
            for (int i = 0; i < length; ++i) {
                final double e = networkOutput[i] - rightAnswer[i];
                error += MathUtil.sqr(e);
            }
            return error / 2;
        }
    }


    private final Stage[] myStages;
    private final int myInputSize;
    private final Function myFunction;

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
        myFunction = networkStructure.getActivationFunction();
    }

    @Override
    public double[] computeOutput(final double[][] input) {
        final double[][] normalizedInput = normalize(input);
        final StageOutput stageOutput = makeInput(normalizedInput);
        final StageOutput[] stageOutputs = forwardComputation(stageOutput);
        return stageOutputs[stageOutputs.length - 1].getOutput1d();
    }

    private StageOutput makeInput(final double[][] inputVector) {
        return new Input(inputVector);
    }

    private StageOutput[] forwardComputation(final StageOutput input) {
        final StageOutput[] stageOutputs = new StageOutput[myStages.length + 1];

        stageOutputs[0] = input;
        forwardThroughStages(stageOutputs);

        return stageOutputs;
    }

    private void forwardThroughStages(final StageOutput[] stageOutputs) {
        final int stagesLength = myStages.length;
        for (int i = 0; i < stagesLength; ++i) {
            final Stage stage = myStages[i];
            final StageOutput output = stage.forwardComputation(stageOutputs[i]);
            stageOutputs[i + 1] = output;
        }
    }

    @Override
    public double[][] normalize(final double[][] input) {
        final double functionMin = myFunction.getMinValue();
        final double functionMax = myFunction.getMaxValue();

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
    public TrainingResult train(final TrainingSet trainingSet) {
        final Trainer trainer = new Trainer(trainingSet, 0.1);
        return trainer.train();
    }
}
