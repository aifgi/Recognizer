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

import com.google.common.base.Preconditions;
import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.neural_network.layers.OneDimensionalLayer;
import ru.aifgi.recognizer.model.neural_network.layers.StageOutput;
import ru.aifgi.recognizer.model.neural_network.layers.impl.FullyConnectedLayer;

/**
 * @author aifgi
 */

public class FullyConnectedStage implements Stage {
    public static class StageOutputImpl implements StageOutput {
        private final double[][] myOutputs;

        public StageOutputImpl(final double[][] outputs) {
            myOutputs = outputs;
        }

        @Override
        public double[][][] getOutput3d() {
            return ArrayUtil.wrapTo3d(getOutput1d());
        }

        @Override
        public double[] getOutput1d() {
            return myOutputs[myOutputs.length - 1];
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
    public StageOutput forwardComputation(final StageOutput input) {
        double[] prevOutput = input.getOutput1d();
        final int length = myLayers.length;
        final double[][] outputs = new double[length][];
        for (int i = 0; i < length; ++i) {
            prevOutput = myLayers[i].computeOutput(prevOutput);
            outputs[i] = prevOutput;
        }
        return new StageOutputImpl(outputs);
    }

    @Override
    public StageOutput backwardComputation(final StageOutput stageOutput, final StageOutput errors) {
        Preconditions.checkArgument(stageOutput instanceof StageOutputImpl);
        double[] errorsArray = errors.getOutput1d();
        final double[][] outputs = ((StageOutputImpl) stageOutput).myOutputs;
        for (int i = outputs.length - 1; i >= 0; --i) {
            final double[] layerOutput = outputs[i];
            errorsArray = myLayers[i].backPropagation(layerOutput, errorsArray);
        }

        return new StageOutputImpl(ArrayUtil.wrapTo2d(errorsArray));
    }
}
