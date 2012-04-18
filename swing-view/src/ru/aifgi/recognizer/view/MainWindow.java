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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author aifgi
 */

public class MainWindow extends JFrame {
    private JPanel myContentPane;
    private ImagePanel myImagePanel;
    private JSplitPane mySplitPane;
    private JTextArea myTextArea;

    private double myDividerLocation = 0.5;

    public MainWindow() {
        super();
        setContentPane(myContentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                mySplitPane.setDividerLocation(myDividerLocation);
            }
        });

        pack();
    }
}
