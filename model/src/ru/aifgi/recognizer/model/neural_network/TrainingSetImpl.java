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

import ru.aifgi.recognizer.api.neural_network.Normalizer;
import ru.aifgi.recognizer.api.neural_network.TrainElement;
import ru.aifgi.recognizer.api.neural_network.TrainingSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author aifgi
 */
public class TrainingSetImpl implements TrainingSet {
    private final List<TrainElement> myTrainElements;
    private final int myNumberOfClusters;

    public TrainingSetImpl(final List<TrainElement> trainElements, final int numberOfClusters) {
        myTrainElements = trainElements;
        myNumberOfClusters = numberOfClusters;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(myTrainElements);
    }

    @Override
    public void normalize(final Normalizer normalizer) {
        for (final TrainElement element : myTrainElements) {
            element.normalize(normalizer);
        }
    }

    @Override
    public int size() {
        return myTrainElements.size();
    }

    @Override
    public Iterator<TrainElement> iterator() {
        return myTrainElements.iterator();
    }

    @Override
    public int getNumberOfClusters() {
        return myNumberOfClusters;
    }
}
