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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import ru.aifgi.recognizer.view.SwingView;

import javax.swing.*;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author aifgi
 */
public class Application {
    public static final String MAIN = "main";
    public static final String LOCALE = "locale";
    public static final String DEBUG = "debug";

    private static final Thread.UncaughtExceptionHandler HANDLER = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            e.printStackTrace();
            ourView.showErrorMessage(e);
        }
    };
    private static final Set<AutoCloseable> CLOSEABLES = Sets.newIdentityHashSet();
    private static SwingView ourView;

    // TODO: move to settings manager
    private static final Map<String, Object> OPTIONS = Maps.newHashMap();

    public static void init(final String[] args) {
        parseCommandLine(args);
        setLocale();
        Thread.setDefaultUncaughtExceptionHandler(getApplicationUncaughtExceptionHandler());
        ourView = createView();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                cleanUp();
            }
        }));
    }

    private static SwingView createView() {
        final Boolean debug = (Boolean) OPTIONS.get(DEBUG);
        return (debug != null && debug) ? new DebugView() : new SwingViewImpl();
    }

    private static void cleanUp() {
        for (final AutoCloseable autoCloseable : CLOSEABLES) {
            try {
                autoCloseable.close();
            }
            catch (Exception e) {
                // TODO:
                e.printStackTrace();
            }
        }
    }

    public static void addAutoCloseable(final AutoCloseable autoCloseable) {
        CLOSEABLES.add(autoCloseable);
    }

    private static void parseCommandLine(final String[] args) {
        for (final String arg : args) {
            if (arg.startsWith("--")) {
                final String command = arg.substring(2);
                final int index = command.indexOf('=');
                if (index < 0) {
                    OPTIONS.put(command, Boolean.TRUE);
                }
                else {
                    final String key = command.substring(0, index);
                    final String value = command.substring(index + 1);
                    OPTIONS.put(key, value);
                }
            }
            else if (arg.startsWith("-")) {
                final String command = arg.substring(1);
                final int length = command.length();
                for (int i = 0; i < length; ++i) {
                    OPTIONS.put(command.substring(i, i + 1), Boolean.TRUE);
                }
            }
            else {
                if (!arg.contains("=")) {
                    OPTIONS.put(MAIN, arg);
                }
            }
        }
    }

    private static void setLocale() {
        String language = "en";
        String country = "US";
        final String localeString = (String) OPTIONS.get(LOCALE);
        if (localeString != null) {
            final int i = localeString.indexOf('_');
            if (i >= 0) {
                language = localeString.substring(0, i);
                country = localeString.substring(i + 1);
            }
        }
        final Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
    }

    public static Thread.UncaughtExceptionHandler getApplicationUncaughtExceptionHandler() {
        return HANDLER;
    }

    public static void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ourView.getMainWindow().setVisible(true);
            }
        });
    }

    public static SwingView getView() {
        return ourView;
    }
}
