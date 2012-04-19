package ru.aifgi.recognizer.view;
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

import ru.aifgi.recognizer.view.actions.ActionGroups;
import ru.aifgi.recognizer.view.actions.Actions;
import ru.aifgi.recognizer.view.components.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author aifgi
 */

class MainWindow extends JFrame {
    private JPanel myContentPane;
    private ImagePanel myImagePanel;
    private JSplitPane mySplitPane;
    private JTextArea myTextArea;

    private double myDividerLocation = 0.5;

    public MainWindow() {
        super();
        setContentPane(myContentPane);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                Actions.EXIT.getAction().perform(e);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                mySplitPane.setDividerLocation(myDividerLocation);
            }
        });

        createMainMenu();

        pack();
    }

    private void createMainMenu() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(ActionGroups.HELP_MENU.createMenuItem());
        setJMenuBar(menuBar);
    }
}
