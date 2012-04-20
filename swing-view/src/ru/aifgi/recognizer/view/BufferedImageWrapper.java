package ru.aifgi.recognizer.view;

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

import java.awt.image.BufferedImage;

/**
 * @author aifgi
 */

public class BufferedImageWrapper implements ImageWrapper {
    private final BufferedImage myBufferedImage;

    public BufferedImageWrapper(final BufferedImage bufferedImage) {
        myBufferedImage = bufferedImage;
    }

    @Override
    public int getWidth() {
        return myBufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return myBufferedImage.getHeight();
    }

    // TODO:
    @Override
    public double[][] getBrightnesses() {
        return new double[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
