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
import ru.aifgi.recognizer.view.components.ActionBasedMenu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author aifgi
 */

public class ActionGroup implements BasicAction {
    private class GroupPresentation implements Presentation {
        private String myName = "";

        @Override
        public String getName() {
            return myName;
        }

        @Override
        public String getMessage() {
            return null;
        }

        @Override
        public Icon getIcon() {
            return null;
        }
    }

    private final List<BasicAction> myActions;
    private final GroupPresentation myPresentation = new GroupPresentation();

    public ActionGroup(final List<BasicAction> actions) {
        myActions = actions;
    }

    public ActionGroup(final BasicAction... actions) {
        myActions = new ArrayList<>(actions.length);
        Collections.addAll(myActions, actions);
    }

    @Override
    public void perform(final AWTEvent e) {
        throw new UnsupportedOperationException("Couldn't perform action group");
    }

    @Override
    public boolean update() {
        boolean res = false;
        for (final BasicAction action : myActions) {
            res |= action.update();
        }
        return res;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setName(final String name) {
        myPresentation.myName = name;
    }

    @Override
    public Presentation getPresentation() {
        return myPresentation;
    }

    public Collection<BasicAction> getActions() {
        return Collections.unmodifiableCollection(myActions);
    }

    @Override
    public JMenuItem createMenuItem() {
        return new ActionBasedMenu(this);
    }
}
