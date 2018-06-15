/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.heuristic.espresso.datastructure;

import static org.junit.Assert.*;

import de.neemann.digital.analyse.heuristic.datastructure.Cover;
import de.neemann.digital.analyse.heuristic.datastructure.Cube;
import org.junit.Test;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class CoverTest {

    Cube cube1 = new Cube(
            new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.one, ThreeStateValue.one},
            ThreeStateValue.zero);
    Cube cube2 = new Cube(
            new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.zero},
            ThreeStateValue.one);
    Cube cube3 = new Cube(
            new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.one},
            ThreeStateValue.dontCare);
    Cube cube4 = new Cube(
            new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.zero},
            ThreeStateValue.one);

    @Test
    public void testCoverInputLength() {
        Cover cover = new Cover(4);
        assertEquals(4, cover.getInputLength());
    }

    // TODO Exceptions testen
    @Test
    public void testCoverInputCubes() {
        try {
            Cover emptyCover = new Cover();
            Cover coverEmptyCubes = new Cover(new Cube(0));
            Cover cover = new Cover(cube1, cube2, cube3, cube4);
            assertEquals(3, cover.getInputLength());
            assertEquals(4, cover.size());

        } catch (Exception e) {

        }

    }

    @Test
    public void testCoverCopy() {
        Cover coverToCopy = new Cover(cube1, cube2, cube3, cube4);
        Cover copiedCover = new Cover(coverToCopy);
        assertEquals(coverToCopy.toString(), copiedCover.toString());
    }

    @Test
    public void testAddCube() {
        Cover cover = new Cover(cube1, cube2, cube3, cube4);
        Cover testCover = new Cover(cube1, cube2, cube3);
        assertEquals(3, testCover.size());
        testCover.addCube(cube4);
        assertEquals(4, testCover.size());
        assertEquals(cover.toString(), testCover.toString());
        assertEquals(cube4.toString(), testCover.getCube(testCover.size() - 1).toString());
    }

    @Test
    public void testSortCubes() {
        Cover sortedCoverAsc = new Cover(cube4, cube1, cube2, cube3);
        Cover sortedCoverDesc = new Cover(cube1, cube2, cube3, cube4);
        Cover cover = new Cover(cube1, cube2, cube3, cube4);
        cover.sort(true);
        assertEquals(sortedCoverAsc.toString(), cover.toString());

        cover.sort(false);
        assertEquals(sortedCoverDesc.toString(), cover.toString());

    }

}
