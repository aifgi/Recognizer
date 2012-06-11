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
import ru.aifgi.recognizer.model.MathUtil;
import ru.aifgi.recognizer.model.neural_network.stages.ConvolutionalStage;
import ru.aifgi.recognizer.model.neural_network.stages.FullyConnectedStage;
import ru.aifgi.recognizer.model.neural_network.stages.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aifgi
 */

public class NeuralNetworkImpl implements NeuralNetwork, Normalizer {
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
            for (final TrainingElement element : myTrainingSet) {
                final NeuralNetworkOutput neuralNetworkOutput = makeInput(element.getData());
                forwardComputation(neuralNetworkOutput);
                final double[] rightAnswer = myRightAnswers[element.getLabel()];
                backwardComputation(neuralNetworkOutput, rightAnswer);

                final double[] networkOutput = neuralNetworkOutput.last();
                final double error = computeError(rightAnswer, networkOutput);
                averageError += error;
            }
            return averageError / myTrainingSet.size();
        }

        private void backwardComputation(final NeuralNetworkOutput neuralNetworkOutput, final double[] rightAnswer) {
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
                    stageStructure.getMask(),
                    convolutionalReceptiveFieldSize,
                    subsamplingReceptiveFieldSize);
            inputSize -= convolutionalReceptiveFieldSize - 1;
            inputSize /= subsamplingReceptiveFieldSize;
        }

        final int[] fullyConnectedLayersSizes = networkStructure.getFullyConnectedLayersSizes();
        inputSize = MathUtil.sqr(inputSize) * stageStructures[stageStructures.length - 1].getNumberOfFeatureMaps();
        myStages[stageStructuresLength] = new FullyConnectedStage(fullyConnectedLayersSizes, inputSize);
        myFunction = networkStructure.getActivationFunction();
    }

    @Override
    public double[] computeOutput(final double[][] input) {
        final double[][] normalizedInput = normalize(input);
        final NeuralNetworkOutput neuralNetworkOutput = makeInput(normalizedInput);
        forwardComputation(neuralNetworkOutput);
        return neuralNetworkOutput.last();
    }

    private NeuralNetworkOutput makeInput(final double[][] inputVector) {
        return new NeuralNetworkOutputImpl(inputVector);
    }

    private void forwardComputation(final NeuralNetworkOutput neuralNetworkOutput) {
        for (final Stage stage : myStages) {
            stage.forwardComputation(neuralNetworkOutput);
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
