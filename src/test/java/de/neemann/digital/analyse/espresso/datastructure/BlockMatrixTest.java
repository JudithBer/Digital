package de.neemann.digital.analyse.espresso.datastructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class BlockMatrixTest {
	Cube cube1 = new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.one, ThreeStateValue.one}, ThreeStateValue.zero);
	Cube cube2 = new Cube(new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.zero}, ThreeStateValue.zero);
	Cube cube3 = new Cube(new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.one}, ThreeStateValue.zero);
	Cube cube4 = new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.zero}, ThreeStateValue.zero);
	
	@Test
	public void testBlockMatrix() {
		Cover offSet = new Cover(cube1, cube2, cube3, cube4);
		BlockMatrix blockMat = new BlockMatrix(offSet, cube1);
		Cover testBlockM = new Cover(new Cube(new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.zero, ThreeStateValue.zero}, ThreeStateValue.zero),
									new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.one}, ThreeStateValue.zero),
									new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.zero}, ThreeStateValue.zero),
									new Cube(new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.one}, ThreeStateValue.zero));
		assertEquals(testBlockM.toString(), blockMat.toString());
	}

}
