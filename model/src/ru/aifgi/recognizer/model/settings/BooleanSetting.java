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

import java.util.prefs.Preferences;

/**
 * @author aifgi
 */
public class BooleanSetting extends AbstractSetting<Boolean> {
    public BooleanSetting(final Preferences preferences, final String key, final boolean defaultValue) {
        super(preferences, key, defaultValue);
    }

    @Override
    public Boolean get() {
        return myPreferences.getBoolean(myKey, myDefaultValue);
    }

    @Override
    protected void checkValueValid(final Boolean value) throws InvalidValueException {
    }

    @Override
    protected void setImpl(final Boolean value) {
        myPreferences.putBoolean(myKey, value);
    }
}
