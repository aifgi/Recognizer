package ru.aifgi.recognizer;

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

import ru.aifgi.recognizer.view.ViewUtil;

import javax.swing.*;
import java.util.Locale;

/**
 * @author aifgi
 */

public class Main {
    public static void main(final String[] args) {
        setLocale(args);
        Thread.setDefaultUncaughtExceptionHandler(ViewUtil.getApplicationUncaughtExceptionHandler());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewUtil.getMainWindow().setVisible(true);
            }
        });
    }

    private static void setLocale(final String[] args) {
        final String language;
        final String country;

        if (args.length < 2) {
            language = "en";
            country = "US";
        }
        else {
            language = args[0];
            country = args[1];
        }

        final Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
    }
}
