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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author aifgi
 */
public class Labels implements Serializable {
    private final BiMap<Character, Integer> myMap = HashBiMap.create();

    public Labels(final char[] chars) {
        final int length = chars.length;
        for (int i = 0; i < length; ++i) {
            myMap.put(chars[i], i);
        }
    }

    public Labels(final List<Character> chars) {
        final int length = chars.size();
        for (int i = 0; i < length; ++i) {
            myMap.put(chars.get(i), i);
        }
    }

    public Labels(final Map<Character, Integer> map) {
        myMap.putAll(map);
    }

    public int getLabelForChar(final char c) {
        return myMap.get(c);
    }

    public char getCharForLabel(final int label) {
        return myMap.inverse().get(label);
    }

    public int getSize() {
        return myMap.size();
    }
}
