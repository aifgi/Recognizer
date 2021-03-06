package ru.aifgi.recognizer.api;

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

import ru.aifgi.recognizer.api.neural_network.TrainingResult;

import java.io.File;

/**
 * @author aifgi
 */

public interface ModelFacade {
    void addProgressListener(final ProgressListener progressListener);

    void removeProgressListener(final ProgressListener progressListener);

    String recognize(final ImageWrapper inputImage);

    TrainingResult train(final File file);

    void loadRecognizer(final File file);

    void saveRecognizer(final File file);
}
