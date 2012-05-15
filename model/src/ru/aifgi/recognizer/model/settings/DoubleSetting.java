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
public class DoubleSetting extends AbstractSetting<Double> {
    private final double myMinValue;
    private final double myMaxValue;

    public DoubleSetting(final Preferences preferences, final String key, final Double defaultValue) {
        this(preferences, key, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleSetting(final Preferences preferences, final String key, final Double defaultValue,
                         final double minValue, final double maxValue) {
        super(preferences, key, defaultValue);
        myMinValue = minValue;
        myMaxValue = maxValue;
    }

    @Override
    public Double get() {
        return myPreferences.getDouble(myKey, myDefaultValue);
    }

    @Override
    protected void checkValueValid(final Double value) throws InvalidValueException {
        if (value < myMinValue || value > myMaxValue) {
            throw new InvalidValueException("Invalid value");
        }
    }

    @Override
    protected void setImpl(final Double value) {
        myPreferences.putDouble(myKey, value);
    }
}
