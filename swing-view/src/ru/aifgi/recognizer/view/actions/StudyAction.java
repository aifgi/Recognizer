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

import ru.aifgi.recognizer.model.Model;
import ru.aifgi.recognizer.model.thread_factories.MyThreadFactory;
import ru.aifgi.recognizer.view.Bundle;
import ru.aifgi.recognizer.view.ViewUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author aifgi
 */
class StudyAction extends MyAction {
    private final ExecutorService myService = Executors.newSingleThreadExecutor(new MyThreadFactory("Study"));

    public StudyAction() {
        super(Bundle.getString("study.action.name"));
    }

    @Override
    protected void performImpl(final AWTEvent event) {
        final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        final int approve = fileChooser.showOpenDialog(ViewUtil.getMainWindow());
        if (approve == JFileChooser.APPROVE_OPTION) {
            myService.execute(new Runnable() {
                @Override
                public void run() {
                    final File file = fileChooser.getSelectedFile();
                    Model.getFacade().study(file);
                }
            });
        }
    }
}
