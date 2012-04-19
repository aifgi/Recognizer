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

import ru.aifgi.recognizer.view.actions.ActionGroup;
import ru.aifgi.recognizer.view.actions.BasicAction;

import javax.swing.*;
import java.util.Collection;

/**
 * @author aifgi
 */

public class ActionBasedMenu extends JMenu {
    private final ActionGroup myActionGroup;

    public ActionBasedMenu(final ActionGroup actionGroup) {
        super();
        myActionGroup = actionGroup;
        setText(myActionGroup.getPresentation().getName());
        fillMenu();
    }

    private void fillMenu() {
        final Collection<BasicAction> actions = myActionGroup.getActions();
        for (final BasicAction action : actions) {
            add(action.createMenuItem());
        }
    }
}
