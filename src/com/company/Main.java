package com.company;

import java.util.*;
import java.util.spi.AbstractResourceBundleProvider;

public class Main {

    //private static int state[] = {0, 0, 5};
    private static int max[] = {2, 3, 5};

    //private static Vector<Integer[]> states = new Vector<>();
    //private static Vector states;
    //private static Vector<Vector<Integer>> states = new Vector<Vector<Integer>>();
    private static Vector<Vector<Integer>> states = new Vector<Vector<Integer>>();
    //private static Vector<Vector<Integer>> tries = new Vector<Vector<Integer>>();

    java.util.TreeMap<Vector<Integer>, Integer> map = new java.util.TreeMap<Vector<Integer>, Integer>();

    private static int[][] steps = {
            {0,1},{0,2},
            {1,0},{1,2},
            {2,0},{2,1},
    };


    /*static boolean isValid() {

        int sum = 0;

        for (int i = 0; i < 3; i++) {
            sum += state[i];

            // Kevesebb mint 0, vagy több mint a maximum van-e benne?
            if (state[i] < 0 || state[i] > max[i]) {
                return false;
            }
        }
        // Összesen annyi folyadék van-e, mint a kezdetben?
        return sum == max[2];
    }*/

    static boolean isGoalState() {
        // célállapot, ha 4 liter van az utolsó kancsóban
        return states.lastElement().get(2) == 4;
    }

    static boolean isValidOperation(int from, int to) {
        // a 'honnan' és 'hova' kancsók megfelelő indexeket jelölnek-e
        // üresbe öntünk-e
        // üresből öntünk-e
        return (from > -1 && from < 3 && to > -1 && to < 3 &&
                from != to && states.lastElement().get(from) != 0 && states.lastElement().get(to) != max[to]);
    }

    static Boolean pour(int from, int to, int tries) {

        // Operátor alkalmazási előfeltétel ellenőrzés
        if (!isValidOperation(from, to)) {
            System.err.println("The requested operation is not valid...");
            return false;
        }

        // Amennyi át lesz öntve:
        // min ('honnan' kancsó tartalma, 'hova' kancsóban levő üres hely)
        int value = Math.min(states.lastElement().get(from), max[to] - states.lastElement().get(to));

        int fromErtek = states.lastElement().get(from);
        int toErtek = states.lastElement().get(to);

        // Operátor hatás
        fromErtek -= value;
        toErtek += value;

        states.lastElement().set(from, fromErtek);
        states.lastElement().set(to, toErtek);
        states.lastElement().set(3, tries);

        return true;
    }

    static void printState(int tries) {
        System.out.print("\n{");;
        for (int i = 0; i < 3; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(states.lastElement().get(i));
        }
        System.out.println("} tries: " + tries);
    }

    public static void main(String args[]) throws InterruptedException {

        // Kezdőállapot felvétele
        Vector<Integer> v = new Vector<Integer>();
        v.add(0);
        v.add(0);
        v.add(5);
        v.add(0); // próbák száma
        states.add(v);

        int from, to;
        int tries = 0;

        printState(tries);

        while (!isGoalState()) {
            if (tries >= 5) {
                states.remove(states.size()-1);
            }

            from = steps[tries][0];
            to = steps[tries][1];

            System.out.println("From: " + from);
            System.out.println("To: " + to);

            tries++;

            if (!isValidOperation(from, to)) {
                System.out.println("The requested operation is not valid...");
                System.out.println("noooooooooooooooooooooo");
                continue;
            } else {
                System.out.println("yeeeeeeeeeeeeeeeeeeeeeee");
                pour(from, to, tries);
                // Eltároljuk a lépéseket egy Vektorban
                //Vector<Integer> tmpv = new Vector<Integer>();
                //tmpv.add(states.lastElement().get(0));
                //tmpv.add(states.lastElement().get(1));
                //tmpv.add(states.lastElement().get(2));
                //tmpv.clear();

                // Ha sikeres volt, nullázzuk az i-t
                tries = 0;
            }

            printState(states.lastElement().get(3));
            //System.exit(1);
            Thread.sleep(1000);

            System.out.println();

        }

    }
}
