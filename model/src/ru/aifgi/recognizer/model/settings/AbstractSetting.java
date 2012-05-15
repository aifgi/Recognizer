package ru.aifgi.recognizer.model.settings;

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

import ru.aifgi.recognizer.api.settings.InvalidValueException;
import ru.aifgi.recognizer.api.settings.Setting;

import java.util.prefs.Preferences;

/**
 * @author aifgi
 */
public abstract class AbstractSetting<T> implements Setting<T> {
    protected final Preferences myPreferences;
    protected final String myKey;
    protected final T myDefaultValue;

    protected AbstractSetting(final Preferences preferences, final String key, final T defaultValue) {
        myPreferences = preferences;
        myKey = key;
        myDefaultValue = defaultValue;
    }

    @Override
    public void set(final T value) throws InvalidValueException {
        checkValueValid(value);
        setImpl(value);
    }

    protected abstract void checkValueValid(final T value) throws InvalidValueException;

    protected abstract void setImpl(final T value);
}
