package de.neemann.digital.analyse.espresso.datastructure.diff;

import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;

public interface IDifferenceMatrixMode {
    boolean getElement(Cover originalCover, Cube cube, int cubeIndex, int inputIndex);
}