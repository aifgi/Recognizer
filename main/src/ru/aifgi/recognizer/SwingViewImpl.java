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

import ru.aifgi.recognizer.view.MainWindow;
import ru.aifgi.recognizer.view.SwingView;

import javax.swing.*;

/**
 * @author aifgi
 */

public class SwingViewImpl implements SwingView {
    private MainWindow myMainWindow;

    // TODO: change return type to Window or make MainWindow interface?
    @Override public MainWindow getMainWindow() {
        if (myMainWindow == null) {
            synchronized (this) {
                if (myMainWindow == null) {
                    myMainWindow = createMainWindow();
                }
            }
        }
        return myMainWindow;
    }

    protected MainWindow createMainWindow() {
        return new MainWindow();
    }

    @Override public void showErrorMessage(final Throwable throwable) {
        showErrorMessage(throwable.getClass().getName(), throwable.getLocalizedMessage());
    }

    @Override public void showErrorMessage(final String message) {
        showErrorMessage("Error", message);
    }

    @Override public void showErrorMessage(final String title, final String message) {
        JOptionPane.showMessageDialog(myMainWindow, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override public JFileChooser createFileChooser() {
        return new JFileChooser(System.getProperty("user.dir"));
    }

    SwingViewImpl() {
    }
}
