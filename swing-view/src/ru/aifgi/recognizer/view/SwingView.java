package ru.aifgi.recognizer.view;

import javax.swing.*;

/**
 * @author Alexey.Ivanov
 */
public interface SwingView {
    // TODO: change return type to Window or make MainWindow interface?
    MainWindow getMainWindow();

    void showErrorMessage(Throwable throwable);

    void showErrorMessage(String message);

    void showErrorMessage(String title, String message);

    JFileChooser createFileChooser();
}
