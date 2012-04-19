package ru.aifgi.recognizer.view.components;

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

import ru.aifgi.recognizer.view.actions.MyAction;

import javax.swing.*;

/**
 * @author aifgi
 */

public class ActionBasedMenuItem extends JMenuItem {
    private final MyAction myAction;


    public ActionBasedMenuItem(final MyAction action) {
        super("");
        myAction = action;
        addActionListener(myAction);
    }

    // TODO:
    @Override
    public String getText() {
        return (myAction == null) ? super.getText() : myAction.getPresentation().getName();
    }

    @Override
    public String getToolTipText() {
        return myAction.getPresentation().getMessage();
    }

    @Override
    public Icon getIcon() {
        return myAction.getPresentation().getIcon();
    }
}
