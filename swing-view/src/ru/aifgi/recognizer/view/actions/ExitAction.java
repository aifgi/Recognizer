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

import ru.aifgi.recognizer.view.ViewUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author aifgi
 */

class ExitAction extends MyAction {
    @Override
    public void perform(final AWTEvent e) {
        final Window mainWindow = ViewUtil.getMainWindow();
        final int confirmed = JOptionPane.showConfirmDialog(mainWindow,
                                                            "Are you sure to want to quit?",
                                                            "Confirm exit",
                                                            JOptionPane.YES_NO_OPTION,
                                                            JOptionPane.QUESTION_MESSAGE);
        if (confirmed == JOptionPane.YES_OPTION) {
            mainWindow.dispose();
        }
    }


}
