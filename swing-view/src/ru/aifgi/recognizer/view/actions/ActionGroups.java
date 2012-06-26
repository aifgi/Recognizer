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

import ru.aifgi.recognizer.Application;

import javax.swing.*;

/**
 * @author aifgi
 */

public enum ActionGroups {
    FILE_MENU(Application.getBundle().getString("file.menu"), new BasicAction[]{
            Actions.OPEN_IMAGE.getAction(), Actions.SAVE_TEXT.getAction(), Actions.CLOSE_IMAGE.getAction(),
            Separator.INSTANCE, Actions.EXIT.getAction()
    }),
    RECOGNIZE_MENU(Application.getBundle().getString("recognize.menu"), new BasicAction[]{
            Actions.RECOGNIZE.getAction(), Separator.INSTANCE, 
            Actions.STUDY.getAction(), Actions.LOAD_RECOGNIZER.getAction(), Actions.SAVE_RECOGNIZER.getAction()
    }),
    HELP_MENU(Application.getBundle().getString("help.menu"), new BasicAction[]{
            Actions.ABOUT.getAction()
    }),
    MAIN_MENU(new BasicAction[]{
            FILE_MENU.getActionGroup(), RECOGNIZE_MENU.getActionGroup(), HELP_MENU.getActionGroup()
    });

    private final ActionGroup myActionGroup;

    ActionGroups(final String name, final BasicAction[] actions) {
        myActionGroup = new ActionGroup(actions);
        myActionGroup.setName(name);
    }

    ActionGroups(final BasicAction[] actions) {
        myActionGroup = new ActionGroup(actions);
    }

    public ActionGroup getActionGroup() {
        return myActionGroup;
    }

    public JMenuItem createMenuItem() {
        return myActionGroup.createMenuItem();
    }
}
