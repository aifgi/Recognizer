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

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author aifgi
 */

class ModelFacadeImpl implements ModelFacade {
    private final Set<ProgressListener> myProgressListeners = new HashSet<>();

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

        doRecognize(inputImage);

        // debug code
        final int s = 100_000_000;
        for (int i = 0; i < s; ++i) {
            notifyProgress(Math.round(100.f * i / s), String.valueOf(i));
        }

        notifyDone("Recognition done");
        return null;
    }

    private void doRecognize(final ImageWrapper inputImage) {
        //To change body of created methods use File | Settings | File Templates.
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
}
