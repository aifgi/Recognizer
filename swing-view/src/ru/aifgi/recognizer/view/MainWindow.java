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

import ru.aifgi.recognizer.Application;
import ru.aifgi.recognizer.view.actions.ActionGroup;
import ru.aifgi.recognizer.view.actions.ActionGroups;
import ru.aifgi.recognizer.view.actions.Actions;
import ru.aifgi.recognizer.view.actions.BasicAction;
import ru.aifgi.recognizer.view.components.ProgressBar;
import ru.aifgi.recognizer.view.components.image.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * @author aifgi
 */

public class MainWindow extends JFrame {
    private JPanel myContentPane;
    private ImagePanel myImagePanel;
    private JSplitPane mySplitPane;
    private JTextArea myTextArea;
    private ProgressBar myProgressBar;

    private double myDividerLocation = 0.5;

    public MainWindow() {
        super();
        setTitle(Application.getBundle().getString("application.title"));
        setContentPane(myContentPane);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));

        myProgressBar.setVisible(false);

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
        mySplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                myDividerLocation = ((double) mySplitPane.getDividerLocation()) /
                        (mySplitPane.getWidth() - mySplitPane.getDividerSize());
            }
        });

        loadRecognizer();
        Application.getModelFacade().addProgressListener(myProgressBar);

        createMainMenu();

        pack();
    }

    private void loadRecognizer() {
        final String settingsDirPath = System.getProperty("user.home") + "/.recognizer/";
        final File file = new File(settingsDirPath + "previous.rec");
        if (!file.exists()) {
            Application.getView().showErrorMessage(Application.getBundle().getString("recognizer.not.studied"));
            return;
        }
        Application.getModelFacade().loadRecognizer(file);
    }

    // TODO: find better way
    public void setImage(final BufferedImage image) {
        myImagePanel.setImage(image);
    }

    public void setText(final String text) {
        myTextArea.setText(text);
    }

    public String getText() {
        return myTextArea.getText();
    }

    public BufferedImage getImage() {
        return myImagePanel.getImage();
    }

    private void createMainMenu() {
        final JMenuBar menuBar = new JMenuBar();
        final ActionGroup mainMenuGroup = ActionGroups.MAIN_MENU.getActionGroup();
        for (BasicAction action : mainMenuGroup.getActions()) {
            menuBar.add(action.createMenuItem());
        }
        setJMenuBar(menuBar);
    }
}
