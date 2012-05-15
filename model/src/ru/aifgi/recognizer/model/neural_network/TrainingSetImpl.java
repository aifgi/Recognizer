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
import ru.aifgi.recognizer.api.neural_network.TrainingElement;
import ru.aifgi.recognizer.api.neural_network.TrainingSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author aifgi
 */
public class TrainingSetImpl implements TrainingSet {
    private final List<TrainingElement> myTrainingElements;
    private final int myNumberOfClusters;

    public TrainingSetImpl(final List<TrainingElement> trainingElements, final int numberOfClusters) {
        myTrainingElements = trainingElements;
        myNumberOfClusters = numberOfClusters;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(myTrainingElements);
    }

    @Override
    public void normalize(final Normalizer normalizer) {
        for (final TrainingElement element : myTrainingElements) {
            element.normalize(normalizer);
        }
    }

    @Override
    public int size() {
        return myTrainingElements.size();
    }

    @Override
    public Iterator<TrainingElement> iterator() {
        return myTrainingElements.iterator();
    }

    @Override
    public int getNumberOfClusters() {
        return myNumberOfClusters;
    }
}
