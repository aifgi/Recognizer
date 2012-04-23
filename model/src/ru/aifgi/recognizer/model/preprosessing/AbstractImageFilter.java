package ru.aifgi.recognizer.model.preprosessing;

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

import ru.aifgi.recognizer.api.ImageFilter;
import ru.aifgi.recognizer.model.ReadWriteLocker;
import ru.aifgi.recognizer.model.thread_factories.DaemonThreadFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * @author aifgi
 */

public abstract class AbstractImageFilter implements ImageFilter {
    private static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool(new DaemonThreadFactory("ImageFilters"));

    @Override
    public double[][] apply(final double[][] inputImage) {
        double[][] res = null;
        final Lock readLock = ReadWriteLocker.getInstance().getReadLock(inputImage);
        readLock.lock();
        try {
            res = doApply(inputImage);
        }
        catch (InterruptedException e) {
            e.printStackTrace();  // TODO:
        }
        finally {
            readLock.unlock();
        }
        return res;
    }

    private double[][] doApply(final double[][] inputImage) throws InterruptedException {
        final int width = inputImage.length;
        final int height = inputImage[0].length;
        final double[][] result = new double[width][height];
        final CountDownLatch latch = new CountDownLatch(width);

        for (int i = 0; i < width; ++i) {
            final int x = i;
            THREAD_POOL.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < height; ++j) {
                            result[x][j] = applyImpl(inputImage, x, j);
                        }
                    }
                    finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        return result;
    }

    protected double getValue(final double[][] array, int x, int y) {
        if (x < 0) {
            x = -x - 1;
        }
        if (y < 0) {
            y = -y - 1;
        }
        final int width = array.length;
        final int height = array[0].length;
        if (x >= width) {
            x = width - (x - width) - 1;
        }
        if (y >= height) {
            y = height - (y - height) - 1;
        }
        return array[x][y];
    }

    protected abstract double applyImpl(final double[][] inputImage, final int x, final int y);

}
