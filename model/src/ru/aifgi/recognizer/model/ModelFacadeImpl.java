package ru.aifgi.recognizer.model;

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

import ru.aifgi.recognizer.api.ImageWrapper;
import ru.aifgi.recognizer.api.ModelFacade;
import ru.aifgi.recognizer.api.ProgressListener;
import ru.aifgi.recognizer.api.Rectangle;
import ru.aifgi.recognizer.api.neural_network.NeuralNetwork;
import ru.aifgi.recognizer.model.neural_network.NeuralNetworkImpl;
import ru.aifgi.recognizer.model.neural_network.NeuralNetworkStructureImpl;
import ru.aifgi.recognizer.model.preprosessing.Binarizer;
import ru.aifgi.recognizer.model.preprosessing.ImageComponentsFinder;

import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author aifgi
 */

class ModelFacadeImpl implements ModelFacade {
    private final Set<ProgressListener> myProgressListeners = new HashSet<>();
    private final Binarizer myBinarizer = new Binarizer();
    private NeuralNetwork myNeuralNetwork = new NeuralNetworkImpl(new NeuralNetworkStructureImpl());
    private Labels myLabels = new Labels(new char[] {
            'а', 'б', 'в', 'г', 'д', 'Е', 'ж', 'и', 'к', 'л', 'м', 'н', 'п', 'р', 'с',
            'т', 'у', 'ф', 'х', 'ц', 'ш', 'щ', 'ъ', 'ь', 'э', 'ю', 'я', 'е',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    });

    @Override
    public void addProgressListener(final ProgressListener progressListener) {
        myProgressListeners.add(progressListener);
    }

    @Override
    public void removeProgressListener(final ProgressListener progressListener) {
        myProgressListeners.remove(progressListener);
    }

    @Override
    public String recognize(final ImageWrapper inputImage) {
        if (EventQueue.isDispatchThread()) {
            // TODO: exceptions
            throw new RuntimeException("Recognition couldn't work in event dispatch thread");
        }
        notifyStarted("Recognition started");

        final String result = doRecognize(inputImage);

        notifyDone("Recognition done");
        return result;
    }

    private String doRecognize(final ImageWrapper inputImage) {
        double[][] input = inputImage.getBrightnesses();
        notifyProgress(1, "Binarization");
        input = myBinarizer.apply(input);
        notifyProgress(5, "Find connected components");
        final ImageComponentsFinder imageComponentsFinder = new ImageComponentsFinder(input);
        final Collection<Rectangle> words = imageComponentsFinder.getWords();
        notifyProgress(20, "Word components recognition");
        final WordRecognizer wordRecognizer = new WordRecognizer(input, myLabels, myNeuralNetwork);
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        for (final Rectangle word : words) {
            final String w = wordRecognizer.recognize(word);
            builder.append(w).append(' ');
            notifyProgress((int) (20 + 80 * ((double) ++i) / words.size()));
        }
        return builder.toString();
    }

    // TODO: another thread?
    private void notifyStarted(final String message) {
        for (final ProgressListener progressListener : myProgressListeners) {
            progressListener.started(message);
        }
    }

    private void notifyProgress(final int progress) {
        for (final ProgressListener progressListener : myProgressListeners) {
            progressListener.progress(progress);
        }
    }

    private void notifyProgress(final int progress, final String message) {
        for (final ProgressListener progressListener : myProgressListeners) {
            progressListener.progress(progress, message);
        }
    }

    private void notifyDone(final String message) {
        for (final ProgressListener progressListener : myProgressListeners) {
            progressListener.done(message);
        }
    }

    @Override
    public void study(File file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveRecognizer(final File file) {
        try (final ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            stream.writeObject(myLabels);
            stream.writeObject(myNeuralNetwork);
        }
        catch (IOException e) {
            // TODO:
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadRecognizer(final File file) {
        try (final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
            myLabels = (Labels) stream.readObject();
            myNeuralNetwork = (NeuralNetwork) stream.readObject();
        }
        catch (IOException e) {
            // TODO:
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
