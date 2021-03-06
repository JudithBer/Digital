/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.heuristic.espresso.datastructure;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import de.neemann.digital.analyse.heuristic.datastructure.Cube;
import org.junit.Test;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class CubeTest {

    @Test
    public void testCubeInputLength() {
        Cube testCube = new Cube(3);
        ThreeStateValue[] tsv = {ThreeStateValue.dontCare, ThreeStateValue.dontCare,
                ThreeStateValue.dontCare};
        assertEquals(3, testCube.getInputLength());
        assertEquals(ThreeStateValue.one, testCube.getOutput());
        assertEquals((Arrays.toString(tsv) + " " + ThreeStateValue.one), testCube.toString());
    }

    @Test
    public void testCubeInputValues() {
        ThreeStateValue[] input = {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.one};
        Cube testCube = new Cube(input, ThreeStateValue.zero);
        ThreeStateValue[] tsv = {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.one};
        assertEquals((Arrays.toString(tsv) + " " + ThreeStateValue.zero), testCube.toString());
    }

    @Test
    public void testCubeCopy() {
        ThreeStateValue[] input = {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.one};
        Cube cubeToCopy = new Cube(input, ThreeStateValue.zero);
        Cube copiedCube = new Cube(cubeToCopy);
        assertEquals(cubeToCopy.toString(), copiedCube.toString());
    }

    @Test
    public void testConvertToPCN() {
        ThreeStateValue[] pcnCube;
        ThreeStateValue[] input = {ThreeStateValue.one, ThreeStateValue.zero,
                ThreeStateValue.dontCare};
        Cube testCube = new Cube(input, ThreeStateValue.zero);
        pcnCube = testCube.convertToPCN();
        ThreeStateValue[] pcnTest = {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.one,
                ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.one};
        assertEquals(Arrays.toString(pcnTest), Arrays.toString(pcnCube));

    }

}
