package ru.aifgi.recognizer.api.neural_network;

import java.io.Serializable;

/**
 * @author Alexey.Ivanov
 */
public interface Labels extends Serializable {
    int getLabelForChar(char c);

    char getCharForLabel(int label);

    int getSize();
}
