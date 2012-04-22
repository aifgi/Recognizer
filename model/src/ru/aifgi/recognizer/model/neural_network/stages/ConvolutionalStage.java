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

import ru.aifgi.recognizer.model.ArrayUtil;
import ru.aifgi.recognizer.model.neural_network.layers.StageOutput;
import ru.aifgi.recognizer.model.neural_network.layers.impl.ConvolutionalLayer;
import ru.aifgi.recognizer.model.neural_network.layers.impl.SubsamplingLayer;

/**
 * @author aifgi
 */

public class ConvolutionalStage implements Stage {
    private static class StageOutputImpl implements StageOutput {
        private final double[][][] myConvolutionalLayerOutput;
        private final double[][][] mySabsamplingLayerOutput;

        public StageOutputImpl(final double[][][] conv, final double[][][] sub) {
            myConvolutionalLayerOutput = conv;
            mySabsamplingLayerOutput = sub;
        }

        @Override
        public double[][][] getOutput3d() {
            return mySabsamplingLayerOutput;
        }

        @Override
        public double[] getOutput1d() {
            return ArrayUtil.toOneDimensionalArray(mySabsamplingLayerOutput);
        }

        public double[][] getStageOutput(final int index) {
            return mySabsamplingLayerOutput[index];
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

    private static class LayerPair {
        ConvolutionalLayer convolutional;
        SubsamplingLayer subsampling;
    }

    private final LayerPair[] myLayers;

    public ConvolutionalStage(final int size, final int convolutionalMask, final int subsamplingMask) {
        myLayers = new LayerPair[size];
        for (LayerPair layerPair : myLayers) {
            layerPair = new LayerPair();
            layerPair.convolutional = new ConvolutionalLayer(convolutionalMask);
            layerPair.subsampling = new SubsamplingLayer(subsamplingMask);
        }
    }

    // TODO: fix "0"
    @Override
    public StageOutput getOutput(final StageOutput input) {
        final int length = myLayers.length;
        final double[][][] conv = new double[length][][];
        final double[][][] sub = new double[length][][];
        final double[][][] input3d = input.getOutput3d();
        for (int i = 0; i < length; ++i) {
            final LayerPair layerPair = myLayers[i];
            conv[i] = layerPair.convolutional.computeOutput(input3d[0]);
            sub[i] = layerPair.subsampling.computeOutput(conv[i]);
        }
        return new StageOutputImpl(conv, sub);
    }

    @Override
    public double[][][] train(final double[][][] input) {
        return null;
    }
}
