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
import ru.aifgi.recognizer.view.Bundle;

import javax.swing.*;
import java.awt.*;

/**
 * @author aifgi
 */

class ExitAction extends MyAction {
    public ExitAction() {
        super(Bundle.getString("exit.action.name"));
    }

    @Override
    public void performImpl(final AWTEvent e) {
        final Window mainWindow = Application.getView().getMainWindow();
        final int confirmed = JOptionPane.showConfirmDialog(mainWindow,
                                                            Bundle.getString("are.you.sure.to.want.to.quit"),
                                                            Bundle.getString("confirm.exit"),
                                                            JOptionPane.YES_NO_OPTION,
                                                            JOptionPane.QUESTION_MESSAGE);
        if (confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }


}
