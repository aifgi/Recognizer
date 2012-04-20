package ru.aifgi.recognizer.view.dialogs;

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

import ru.aifgi.recognizer.view.Bundle;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author aifgi
 */

public class AboutDialog extends JDialog {
    private JPanel myContentPane;
    private JTabbedPane myTabbedPane;

    public AboutDialog() {
        super();
        setTitle(Bundle.getString("about"));
        setContentPane(myContentPane);
        setModal(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                setVisible(false);
            }
        });

        pack();
    }
}
