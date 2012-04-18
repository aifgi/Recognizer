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

import com.google.common.collect.MapMaker;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author aifgi
 */

public class ReadWriteLocker {
    private static volatile ReadWriteLocker ourLocker;

    public static ReadWriteLocker getInstance() {
        if (ourLocker == null) {
            synchronized (ReadWriteLocker.class) {
                if (ourLocker == null) {
                    ourLocker = new ReadWriteLocker();
                }
            }
        }
        return ourLocker;
    }

    private final ConcurrentMap<Object, ReadWriteLock> myLocks;

    private ReadWriteLocker() {
        myLocks = new MapMaker().concurrencyLevel(16).weakKeys().makeMap();
    }

    public Lock getReadLock(final Object o) {
        final ReadWriteLock readWriteLock = getLockForObject(o);
        return readWriteLock.readLock();
    }

    public Lock getWriteLock(final Object o) {
        final ReadWriteLock readWriteLock = getLockForObject(o);
        return readWriteLock.writeLock();
    }

    private ReadWriteLock getLockForObject(final Object o) {
        ReadWriteLock readWriteLock = myLocks.get(o);
        if (readWriteLock == null) {
            final ReentrantReadWriteLock value = new ReentrantReadWriteLock(true);
            readWriteLock = myLocks.putIfAbsent(o, value);
            if (readWriteLock == null) {
                readWriteLock = value;
            }
        }
        return readWriteLock;
    }
}
