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

/**
 * @author aifgi
 */

public enum Actions {
    OPEN_IMAGE(new OpenImageAction()),
    RECOGNIZE(new RecognizeAction()),
    STUDY(new StudyAction()),
    SAVE_RECOGNIZER(new SaveRecognizerAction()),
    LOAD_RECOGNIZER(new LoadRecognizerAction()),
    ABOUT(new AboutAction()),
    SAVE_TEXT(new SaveTextAction()),
    EXIT(new ExitAction());

    private final MyAction myAction;

    Actions(final MyAction action) {
        myAction = action;
    }

    public MyAction getAction() {
        return myAction;
    }
}
