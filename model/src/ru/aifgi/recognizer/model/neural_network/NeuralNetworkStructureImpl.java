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
import ru.aifgi.recognizer.api.neural_network.NeuralNetworkStructure;

/**
 * @author aifgi
 */

// TODO: do not hardcoded it
public class NeuralNetworkStructureImpl implements NeuralNetworkStructure {
    private static class StageStructureImpl implements StageStructure {
        private final int myNumberOfFeatureMaps;
        private final int myConvolutionalReceptiveFieldSize;
        private final int mySubsamplingReceptiveFieldSize;
        private final int[][] myMask;

        private StageStructureImpl(final int numberOfFeatureMaps, final int convolutionalReceptiveFieldSize,
                                   final int subsamplingReceptiveFieldSize, final int[][] mask) {
            myNumberOfFeatureMaps = numberOfFeatureMaps;
            myConvolutionalReceptiveFieldSize = convolutionalReceptiveFieldSize;
            mySubsamplingReceptiveFieldSize = subsamplingReceptiveFieldSize;
            myMask = mask;
        }

        @Override
        public int getNumberOfFeatureMaps() {
            return myNumberOfFeatureMaps;
        }

        @Override
        public int getConvolutionalReceptiveFieldSize() {
            return myConvolutionalReceptiveFieldSize;
        }

        @Override
        public int getSubsamplingReceptiveFieldSize() {
            return mySubsamplingReceptiveFieldSize;
        }

        @Override
        public int[][] getMask() {
            return myMask;
        }
    }

    private final StageStructure[] myStageStructures;

    public NeuralNetworkStructureImpl() {
        myStageStructures = new StageStructure[2];
        int[][] mask = new int[6][1];
        myStageStructures[0] = new StageStructureImpl(6, 5, 2, mask);
        mask = new int[15][6];
        myStageStructures[1] = new StageStructureImpl(15, 5, 2, mask);
    }

    @Override
    public Function getActivationFunction() {
        return Functions.TANH;
    }

    @Override
    public int getInputSize() {
        return 32;
    }

    @Override
    public StageStructure[] getStageStructures() {
        return myStageStructures;
    }

    @Override
    public int[] getFullyConnectedLayersSizes() {
        return new int[]{80, 37};
    }
}
