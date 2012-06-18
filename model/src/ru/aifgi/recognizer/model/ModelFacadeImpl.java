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

import com.google.gson.Gson;
import ru.aifgi.recognizer.api.ImageWrapper;
import ru.aifgi.recognizer.api.ModelFacade;
import ru.aifgi.recognizer.api.ProgressListener;
import ru.aifgi.recognizer.api.Rectangle;
import ru.aifgi.recognizer.api.neural_network.*;
import ru.aifgi.recognizer.model.neural_network.*;
import ru.aifgi.recognizer.model.preprosessing.Binarizer;
import ru.aifgi.recognizer.model.preprosessing.ImageComponentsFinder;
import ru.aifgi.recognizer.model.thread_factories.MyThreadFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author aifgi
 */

class ModelFacadeImpl implements ModelFacade {
    private final ExecutorService myExecutorService = Executors.newCachedThreadPool(new MyThreadFactory("Recognition"));

    private final Set<ProgressListener> myProgressListeners = new HashSet<>();
    private NeuralNetwork myNeuralNetwork;
    private Labels myLabels;

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
        notifyStarted("Recognition started");

        final String result = doRecognize(inputImage);

        notifyDone("Recognition done");
        return result;
    }

    private String doRecognize(final ImageWrapper inputImage) {
        init();
        double[][] input = inputImage.getBrightnesses();
        notifyProgress(1, "Binarization");
        input = new Binarizer().apply(input);
        notifyProgress(5, "Find connected components");
        final ImageComponentsFinder imageComponentsFinder = new ImageComponentsFinder(input);
        final Collection<Rectangle> words = imageComponentsFinder.getWords();
        notifyProgress(20, "Word components recognition");

        final String[] recognizedWords = recognizeWords(input, words);

        return buildResult(recognizedWords);
    }

    private void init() {
        if (myNeuralNetwork == null) {
            myNeuralNetwork = new NeuralNetworkImpl(new NeuralNetworkStructureImpl());
        }
        if (myLabels == null) {
            myLabels = new LabelsImpl(new char[] {
                    'а', 'б', 'в', 'г', 'д', 'Е', 'ж', 'и', 'к', 'л', 'м', 'н', 'п', 'р', 'с',
                    'т', 'у', 'ф', 'х', 'ц', 'ш', 'щ', 'ъ', 'ь', 'э', 'ю', 'я', 'е',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            });
        }
    }

    private String[] recognizeWords(final double[][] input, final Collection<Rectangle> words) {
        final WordRecognizer wordRecognizer = new WordRecognizer(input, myLabels, myNeuralNetwork);
        final int size = words.size();
        final CountDownLatch latch = new CountDownLatch(size);
        final String[] recognizedWords = new String[size];
        int i = 0;
        for (final Rectangle word : words) {
            final int pos = i++;
            myExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    recognizedWords[pos] = wordRecognizer.recognize(word);
                    latch.countDown();
                    notifyProgress((int) (20 + 80 * ((double) size - latch.getCount()) / size));
                }
            });
        }
        try {
            latch.await();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return recognizedWords;
    }

    private String buildResult(final String[] recognizedWords) {
        final StringBuilder builder = new StringBuilder();
        for (final String word : recognizedWords) {
            builder.append(word).append(' ');
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

    private void setIndeterminate() {
        for (final ProgressListener progressListener : myProgressListeners) {
            progressListener.setIndeterminate(true);
        }
    }

    @Override
    public TrainingResult train(final File file) {
        setIndeterminate();
        notifyStarted("Study");
        final TrainingSet trainingSet = readTrainingData(file);
        final TrainingResult trainingResult = myNeuralNetwork.train(trainingSet);
        notifyDone("Study finished. Last error: " + String.valueOf(trainingResult.getLastError()));
        return trainingResult;
    }

    private TrainingSet readTrainingData(final File file) {
        final ZipFileTrainingSetReader trainingSetReader = new ZipFileTrainingSetReader(file);
        myLabels = trainingSetReader.getLabels();
        myNeuralNetwork = new NeuralNetworkImpl(trainingSetReader.getNeuralNetworkStructure());
        return trainingSetReader.getTrainingSet();
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
