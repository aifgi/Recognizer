package ru.aifgi.recognizer.view.components.image;

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

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author aifgi
 */

public class ImageChangedEvent extends AWTEvent {
    public static final int IMAGE_CHANGED_EVENT = AWTEvent.RESERVED_ID_MAX + 0x43;

    private final BufferedImage myOldValue;
    private final BufferedImage myNewValue;

    public ImageChangedEvent(final Object source, final BufferedImage oldValue, final BufferedImage newValue) {
        super(source, IMAGE_CHANGED_EVENT);
        myOldValue = oldValue;
        myNewValue = newValue;
    }

    public BufferedImage getOldValue() {
        return myOldValue;
    }

    public BufferedImage getNewValue() {
        return myNewValue;
    }
}
