package de.neemann.digital.analyse.espresso;

import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static de.neemann.digital.analyse.expression.Variable.v;

public class Main {

    public static void main(String[] args) {

        Cover test1 = test1();
        Simplify testSimplify = new Simplify();
        List<Variable> vars= new ArrayList<Variable>() {{
            add(v("A"));
            add(v("B"));
            add(v("C"));
            add(v("D"));
            add(v("E"));
            add(v("F"));
            }};
        String resultName = "test";
        //testSimplify.minimizeCover(vars, resultName,test1);
    }


    /**
     * Testdaten aus Buch von S. 52
     * @return
     */
    private static Cover test1() {
        int inputLength = 6;
        Cover cover = new Cover(inputLength);

        ThreeStateValue zero = ThreeStateValue.zero;
        ThreeStateValue one = ThreeStateValue.one;
        ThreeStateValue dc = ThreeStateValue.dontCare;

        Cube c1 = new Cube(new ThreeStateValue[] {dc, zero, zero, zero, dc, zero}, one);
        cover.addCube(c1);
        Cube c2 = new Cube(new ThreeStateValue[] {zero, zero, one, dc, one, dc}, one);
        cover.addCube(c2);
        Cube c3 = new Cube(new ThreeStateValue[] {one, dc, one, dc, dc, one}, one);
        cover.addCube(c3);
        Cube c4 = new Cube(new ThreeStateValue[] {zero, one, dc, one, dc, dc}, one);
        cover.addCube(c4);
        Cube c5 = new Cube(new ThreeStateValue[] {dc, one, dc, dc, zero, one}, one);
        cover.addCube(c5);
        Cube c6 = new Cube(new ThreeStateValue[] {dc, one, zero, zero, dc, one}, one);
        cover.addCube(c6);
        Cube c7 = new Cube(new ThreeStateValue[] {one, zero, one, zero, zero, dc}, one);
        cover.addCube(c7);
        Cube c8 = new Cube(new ThreeStateValue[] {dc, zero, zero, zero, dc, one}, one);
        cover.addCube(c8);
        Cube c9 = new Cube(new ThreeStateValue[] {one, zero, one, zero, one, dc}, one);
        cover.addCube(c9);
        Cube c10 = new Cube(new ThreeStateValue[] {dc, one, zero, zero, dc, zero}, one);
        cover.addCube(c10);
        Cube c11 = new Cube(new ThreeStateValue[] {one, one, dc, one, dc, zero}, one);
        cover.addCube(c11);
        Cube c12 = new Cube(new ThreeStateValue[] {one, one, dc, one, dc, one}, one);
        cover.addCube(c12);

        return cover;
    }
}
