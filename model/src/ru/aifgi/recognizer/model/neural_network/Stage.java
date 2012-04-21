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

import ru.aifgi.recognizer.model.neural_network.layers.impl.ConvolutionalLayer;
import ru.aifgi.recognizer.model.neural_network.layers.impl.SubsamplingLayer;

/**
 * @author aifgi
 */

public class Stage {
    public static class StageOutput {
        private final double[][][] myConvolutionalLayerOutput;
        private final double[][][] mySabsamplingLayerOutput;

        public StageOutput(final double[][] input) {
            myConvolutionalLayerOutput = null;
            final double[][][] t = new double[1][][];
            t[0] = input;
            mySabsamplingLayerOutput = t;
        }

        public StageOutput(final double[][][] conv, final double[][][] sub) {
            myConvolutionalLayerOutput = conv;
            mySabsamplingLayerOutput = sub;
        }

        public double[][][] getStageOutput() {
            return mySabsamplingLayerOutput;
        }

        public double[][] getStageOutput(final int index) {
            return mySabsamplingLayerOutput[index];
        }
    }

    private static class LayerPair {
        ConvolutionalLayer convolutional;
        SubsamplingLayer subsampling;
    }

    private final LayerPair[] myLayers;

    public Stage(final int size, final int convolutionalMask, final int subsamplingMask) {
        myLayers = new LayerPair[size];
        for (LayerPair layerPair : myLayers) {
            layerPair = new LayerPair();
            layerPair.convolutional = new ConvolutionalLayer(convolutionalMask);
            layerPair.subsampling = new SubsamplingLayer(subsamplingMask);
        }
    }

    // TODO: fix "0"
    public StageOutput getOutput(final StageOutput input) {
        final int length = myLayers.length;
        final double[][][] conv = new double[length][][];
        final double[][][] sub = new double[length][][];
        for (int i = 0; i < length; ++i) {
            final LayerPair layerPair = myLayers[i];
            conv[i] = layerPair.convolutional.computeOutput(input.getStageOutput(0));
            sub[i] = layerPair.subsampling.computeOutput(conv[i]);
        }
        return new StageOutput(conv, sub);
    }

    public double[][][] train(final double[][][] input) {
        return null;
    }
}
