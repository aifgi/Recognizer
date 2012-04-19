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

import ru.aifgi.recognizer.view.Presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author aifgi
 */

public abstract class MyAction extends AbstractAction implements BasicAction, Presentation {
    public MyAction(final String name) {
        super(name);
    }

    public MyAction(final String name, final Icon icon) {
        super(name, icon);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        perform(e);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public Presentation getPresentation() {
        return this;
    }

    @Override
    public String getName() {
        return (String) getValue(Action.NAME);
    }

    @Override
    public String getMessage() {
        final String value = (String) getValue(Action.SHORT_DESCRIPTION);
        return (value != null) ? value : getName();
    }

    @Override
    public Icon getIcon() {
        return (Icon) getValue(Action.SMALL_ICON);
    }
}
