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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author aifgi
 */

public class DaemonThreadFactory implements ThreadFactory {
    private final String myPoolName;
    private final AtomicInteger myNumber = new AtomicInteger(0);

    public DaemonThreadFactory(final String poolName) {
        myPoolName = poolName;
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread thread = new Thread(r, myPoolName + " Pool[Thread-" + myNumber.getAndIncrement() + "]");
        thread.setDaemon(true);
        return thread;
    }
}
