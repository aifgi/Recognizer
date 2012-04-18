package ru.aifgi.recognizer.model.preprosessing;

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

/**
 * @author aifgi
 */

public class Binarizer extends AbstractImageFilter {
    private int myThreshold;

    @Override
    public double[][] apply(final double[][] inputImage) {
        myThreshold = OtsuAlgorithm.thresholding(inputImage);
        return super.apply(inputImage);
    }

    @Override
    protected double applyImpl(final double[][] inputImage, final int x, final int y) {
        return (inputImage[x][y] < myThreshold) ? 0 : 255;
    }
}
