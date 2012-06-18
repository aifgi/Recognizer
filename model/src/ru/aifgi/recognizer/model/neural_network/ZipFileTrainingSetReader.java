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

import com.google.gson.Gson;
import ru.aifgi.recognizer.api.neural_network.*;
import ru.aifgi.recognizer.model.ImageReader;
import ru.aifgi.recognizer.model.LabelsImpl;
import ru.aifgi.recognizer.model.preprosessing.Binarizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author aifgi
 */
public class ZipFileTrainingSetReader implements TrainingSetReader {
    public static final String LABELS_FILENAME = "labels.csv";
    public static final String STRUCTURE_FILENAME = "structure.json";

    private Labels myLabels;
    private NeuralNetworkStructure myNeuralNetworkStructure;
    private TrainingSet myTrainingSet;

    // TODO: exceptions
    public ZipFileTrainingSetReader(final File file) {
        try (final ZipFile zipFile = new ZipFile(file)) {
            readLabels(zipFile);
            readNetworkStructure(zipFile);
            readTrainingSet(zipFile);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readLabels(final ZipFile zipFile) throws IOException {
        final ZipEntry entry = zipFile.getEntry(LABELS_FILENAME);
        if (entry == null) {
            throw new RuntimeException("Missing " + LABELS_FILENAME + " file");
        }
        final Reader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
        final List<Character> list = readCSVFile(reader);
        myLabels = new LabelsImpl(list);
    }

    private void readNetworkStructure(final ZipFile zipFile) throws IOException {
        final ZipEntry entry = zipFile.getEntry(STRUCTURE_FILENAME);
        if (entry == null) {
            throw new RuntimeException("Missing " + STRUCTURE_FILENAME + " file");
        }
        final Reader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
        final Gson gson = new Gson();
        myNeuralNetworkStructure = gson.fromJson(reader, NeuralNetworkStructureImpl.class);
    }

    private void readTrainingSet(final ZipFile zipFile) throws IOException {
        final List<TrainingElement> trainingElements = new ArrayList<>();
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        final Binarizer binarizer = new Binarizer();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String name = entry.getName();
            if (!entry.isDirectory() && (name.endsWith("png") || name.endsWith("bmp"))) {
                System.out.println(name);
                final double[][] data = binarizer.apply(readImage(zipFile, entry));
                final String parentFolderName = getParentFolderName(entry);
                trainingElements.add(new TrainingElementImpl(data, Integer.valueOf(parentFolderName)));
            }
        }
        myTrainingSet = new TrainingSetImpl(trainingElements, myLabels.getSize());
    }

    private double[][] readImage(final ZipFile zipFile, final ZipEntry entry) throws IOException {
        return ImageReader.readImage(zipFile.getInputStream(entry));
    }

    // TODO: better way
    private String getParentFolderName(final ZipEntry entry) {
        final String name = entry.getName();
        final String[] split = name.split("/");
        if (split.length == 1) {
            return "";
        }
        else {
            return split[split.length - 2];
        }
    }

    private List<Character> readCSVFile(final Reader reader) throws IOException {
        final List<Character> list = new ArrayList<>();
        int value = reader.read();
        boolean waitComma = false;
        while (value != -1) {
            final char c = (char) value;
            if (!Character.isWhitespace(c)) {
                if (waitComma) {
                    if (c != ',') {
                        throw new RuntimeException("Invalid csv file");
                    }
                }
                else {
                    list.add(c);
                }
                waitComma = !waitComma;
            }
            value = reader.read();
        }
        if (!waitComma) {
            throw new RuntimeException("Invalid csv file");
        }
        return list;
    }

    @Override
    public Labels getLabels() {
        return myLabels;
    }

    @Override
    public NeuralNetworkStructure getNeuralNetworkStructure() {
        return myNeuralNetworkStructure;
    }

    @Override
    public TrainingSet getTrainingSet() {
        return myTrainingSet;
    }
}
