package ru.aifgi.recognizer.view.actions;

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

import ru.aifgi.recognizer.Application;
import ru.aifgi.recognizer.model.Model;
import ru.aifgi.recognizer.view.Bundle;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author aifgi
 */
public class LoadRecognizerAction extends MyAction {
    public LoadRecognizerAction() {
        super(Bundle.getString("load.recognizer.action.name"));
    }

    @Override
    protected void performImpl(final AWTEvent event) {
        final JFileChooser fileChooser = Application.getView().createFileChooser();
        final int res = fileChooser.showOpenDialog(Application.getView().getMainWindow());
        if (res == JFileChooser.APPROVE_OPTION) {
            final File file = fileChooser.getSelectedFile();
            Model.getFacade().loadRecognizer(file);
        }
    }
}
