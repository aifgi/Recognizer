package ru.aifgi.recognizer.view;

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

/**
 * @author aifgi
 */


public class ImagePanel extends JPanel implements Scrollable {
    private BufferedImage myImage;

    public ImagePanel() {
        this(null);
    }

    public ImagePanel(final BufferedImage image) {
        super(true);
        myImage = image;
        setAutoscrolls(true);
    }

    public Image getImage() {
        return myImage;
    }

    public void setImage(final BufferedImage image) {
        myImage = image;
        revalidate();
        repaint();
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

