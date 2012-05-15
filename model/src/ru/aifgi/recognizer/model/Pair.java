package ru.aifgi.recognizer.model;

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

import ru.aifgi.recognizer.api.graph.Edge;

/**
 * @author aifgi
 */
public class Pair<F, S> {
    private F myFirst;
    private S mySecond;

    public Pair() {
        this(null, null);
    }

    public Pair(final F first, final S second) {
        myFirst = first;
        mySecond = second;
    }

    public F getFirst() {
        return myFirst;
    }

    public S getSecond() {
        return mySecond;
    }

    public void setFirst(final F first) {
        myFirst = first;
    }

    public void setSecond(final S second) {
        mySecond = second;
    }

    public void set(final F first, final S second) {
        setFirst(first);
        setSecond(second);
    }
}
