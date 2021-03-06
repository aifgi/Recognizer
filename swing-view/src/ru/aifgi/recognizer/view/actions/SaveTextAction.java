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

import com.google.common.base.Strings;
import ru.aifgi.recognizer.Application;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * @author aifgi
 */
public class SaveTextAction extends MyAction {
    public SaveTextAction() {
        super(Application.getBundle().getString("save.text.action.name"));
    }

    @Override
    protected void performImpl(AWTEvent event) {
        final JFileChooser fileChooser = Application.getView().createFileChooser();
        final int approve = fileChooser.showSaveDialog(Application.getView().getMainWindow());
        if (approve == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            try {
                final Writer writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(selectedFile)));
                writer.write(Application.getView().getMainWindow().getText());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public boolean update() {
        final boolean oldEnabled = isEnabled();
        setEnabled(!Strings.isNullOrEmpty(Application.getView().getMainWindow().getText()));
        return super.update() || oldEnabled != isEnabled();
    }
}
