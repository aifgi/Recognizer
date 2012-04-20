package ru.aifgi.recognizer.view.components.image;

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

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * @author aifgi
 */


public class ImagePanel extends JPanel implements Scrollable {
    private static final EventQueue EVENT_QUEUE = Toolkit.getDefaultToolkit().getSystemEventQueue();

    private final Set<ImageChangedListener> myListeners = new HashSet<>();
    private BufferedImage myImage;

    public ImagePanel() {
        this(null);
    }

    public ImagePanel(final BufferedImage image) {
        super();
        myImage = image;
        setAutoscrolls(true);

        addImageChangedListener(new ImageChangedListener() {
            @Override
            public void imageChanged(final ImageChangedEvent event) {
                revalidate();
                repaint();
            }
        });
    }

    public BufferedImage getImage() {
        return myImage;
    }

    public void setImage(final BufferedImage newImage) {
        final BufferedImage oldImage = myImage;
        myImage = newImage;
        EVENT_QUEUE.postEvent(new ImageChangedEvent(this, oldImage, newImage));
    }

    public synchronized void addImageChangedListener(final ImageChangedListener listener) {
        myListeners.add(listener);
    }

    public synchronized void removeImageChangedListener(final ImageChangedListener listener) {
        myListeners.remove(listener);
    }

    @Override
    protected void processEvent(final AWTEvent e) {
        if (e instanceof ImageChangedEvent) {
            processImageChangedEvent((ImageChangedEvent) e);
            return;
        }
        super.processEvent(e);
    }

    private void processImageChangedEvent(final ImageChangedEvent e) {
        for (final ImageChangedListener listener : myListeners) {
            listener.imageChanged(e);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return (myImage == null) ? super.getPreferredSize() :
                new Dimension(myImage.getWidth(), myImage.getHeight());
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (myImage != null) {
            final int width = getWidth();
            final int height = getHeight();
            final int imageWidth = myImage.getWidth();
            final int imageHeight = myImage.getHeight();
            int x = (width > imageWidth) ? (width - imageWidth) >> 1 : 0;
            int y = (height > imageHeight) ? (height - imageHeight) >> 1 : 0;
            g.drawImage(myImage, x, y, null);
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return 3;
    }

    @Override
    public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        return getScrollableUnitIncrement(visibleRect, orientation, direction) * 3;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        final Container parent = SwingUtilities.getUnwrappedParent(this);
        return (parent instanceof JViewport) && parent.getWidth() > getPreferredSize().width;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        final Container parent = SwingUtilities.getUnwrappedParent(this);
        return (parent instanceof JViewport) && parent.getHeight() > getPreferredSize().height;
    }
}

