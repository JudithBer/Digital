package de.neemann.digital.analyse.espresso.datastructure;

import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Data structure of a logical function. It is defined by an ArrayList of Cubes (implicants of the
 * function).
 */
public class Cover implements Iterable<Cube> {

    // ArrayList of Cubes
    private List<Cube> cubes;
    // Number of input variables of the function
    private int inputLength;

    /**
     * Generate a Cover with an empty list of cubes with the given number of input variables
     * @param inputLength
     *            Number of input variables of the function
     */
    public Cover(int inputLength) {
        this.cubes = new ArrayList<Cube>();
        this.inputLength = inputLength;
    }

    /**
     * Generate a Cover with the given cubes
     * @param cubes
     *            Given cubes of the cover
     * @throws Exception
     */
    public Cover(Cube... cubes) {
        // Validate the input parameters
        if (cubes == null)
            throw new NullPointerException("Parameter can't be null.");
        if (cubes.length == 0) {
            throw new UnsupportedOperationException(
                    "This constructor requires at least one cube. Use Cover(int inputLength) instead.");
        }

        this.cubes = new ArrayList<Cube>();
        // Set the size of the first cube as inputLength
        this.inputLength = cubes[0].getInputLength();

        // Set the given cubes
        for (Cube cube : cubes) {
            // Check that the cube has the same number of input variables
            if (cube.getInputLength() != inputLength) {
                throw new RuntimeException("All cubes need to have the same length.");
            }
            this.cubes.add(new Cube(cube));
        }

    }

    /**
     * Generate a new Cover (copy) filled with the values of the given Cover
     * @param origin
     *            Cover to copy
     */
    public Cover(Cover origin) {
        this.cubes = new ArrayList<Cube>();
        this.inputLength = origin.inputLength;
        this.cubes.addAll(origin.cubes);
    }

    /**
     * Add the given Cube into the Cover
     * @param newCube
     *            Cube to add to the Cover
     */
    public void addCube(Cube newCube) {
        this.cubes.add(newCube);
    }

    /**
     * Sort the cover based on the weight of the cubes
     * @param asc
     *            Direction of sorting ( asc = true, des = false)
     */
    public void sort(boolean asc) {
        List<Pair<Integer, Integer>> weights = this.getWeight();

        if (asc) {
            Collections.sort(weights, Collections.reverseOrder());
        } else {
            Collections.sort(weights);
        }

        List<Cube> tempCubes = new ArrayList<Cube>();
        for (Pair<Integer, Integer> cube : weights) {
            tempCubes.add(this.cubes.get(cube.getFirst()));
        }

        this.cubes = tempCubes;
    }

    /**
     * Calculate the weight of the Cubes of the Cover
     * @return List of Pairs with the index of the cube and the weight of it
     */
    private List<Pair<Integer, Integer>> getWeight() {

        // Calculate Column Sum
        int[] colSum = new int[inputLength * 2];
        ThreeStateValue[] cubeinput;
        for (Cube cube : this.cubes) {
            cubeinput = cube.convertToPCN();
            for (int i = 0; i < cubeinput.length; i++) {
                colSum[i] += cubeinput[i].asInt();
            }
        }

        // Calculate weight of each row
        List<Pair<Integer, Integer>> retWeight = new ArrayList<>();
        int weight;

        for (int j = 0; j < this.cubes.size(); j++) {
            weight = 0;
            cubeinput = this.cubes.get(j).convertToPCN();
            for (int i = 0; i < cubeinput.length; i++) {
                if (cubeinput[i] == ThreeStateValue.one) {
                    weight += colSum[i];
                }
            }
            retWeight.add(new Pair<>(j, weight));
        }

        return retWeight;
    }

    // Getter of the Cover attributes

    /**
     * Get the inputLength (number of input variables)
     * @return inputLength
     */
    public int getInputLength() {
        return inputLength;
    }

    /**
     * Get the Cube at position 'index'
     * @param index
     *            Index of the searched Cube
     * @return The Cube at position 'index'
     */
    public Cube getCube(int index) {
        return cubes.get(index);
    }

    /**
     * Get the number of Cubes of the Cover
     * @return The number of Cubes of the Cover
     */
    public int size() {
        return cubes.size();
    }

    @Override
    public Iterator<Cube> iterator() {
        return cubes.iterator();
    }

    @Override
    public String toString() {
        String result = "";
        for (Cube cube : cubes) {
            result += cube.toString() + "\n";
        }
        return result;
    }

    public boolean contains(Cube cube) {
        try {
            Cover differenceCover = new DifferenceMatrix(this, cube, "distance").getDiffCover();
            int inputLenght = differenceCover.getInputLength();

            // Alle Cubes der DiffMatrix durchgehen
            for (int i = 0; i < differenceCover.size(); i++) {
                int rowSum = 0;
                Cube currentCube = differenceCover.getCube(i);

                for (int j = 0; j < inputLenght; j++) {
                    if(currentCube.getState(j) == ThreeStateValue.one)
                        rowSum++;
                }

                if (rowSum == 0){
                    return true;
                }
            }

        } catch (EmptyCoverException e) {
            return false;
        }

        return false;
    }
}
