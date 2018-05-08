package de.neemann.digital.analyse.espresso.datastructure;

import static org.junit.Assert.*;

import org.junit.Test;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class CoverMatrixTest {

	public class BlockMatrixTest {
		Cube cube1 = new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.one, ThreeStateValue.dontCare}, ThreeStateValue.one);
		Cube cube2 = new Cube(new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.one, ThreeStateValue.dontCare}, ThreeStateValue.one);
		Cube cube3 = new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.zero}, ThreeStateValue.one);
		
		@Test
		public void testBlockMatrix() {
			Cover onSet = new Cover(cube1, cube2, cube3);
			BlockMatrix blockMat = new BlockMatrix(onSet, cube2);
			Cover testBlockM = new Cover(new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.zero, ThreeStateValue.one}, ThreeStateValue.zero),
										new Cube(new ThreeStateValue[] {ThreeStateValue.zero, ThreeStateValue.zero, ThreeStateValue.one}, ThreeStateValue.zero),
										new Cube(new ThreeStateValue[] {ThreeStateValue.one, ThreeStateValue.one, ThreeStateValue.zero}, ThreeStateValue.zero));
			assertEquals(testBlockM.toString(), blockMat.toString());
		}

	}

}
