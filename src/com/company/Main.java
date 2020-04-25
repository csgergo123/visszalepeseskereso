package com.company;

import java.util.*;

public class Main {

    private static int max[] = {2, 3, 5};

    // Vektorba ágyazott vektorral tároljuk el az előállított állapot hármasokat, ez lesz a verem
    // Az első három érték a kancsók állapotát jelzi, a harmadik pedig egy index az operators mátrixra, hogy az adott állapotban melyik operátort alkalmaztuk utoljára
    private static Vector<Vector<Integer>> states = new Vector<Vector<Integer>>();

    private static int[][] operators = {
            {0,1},{0,2},
            {1,0},{1,2},
            {2,0},{2,1},
    };

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

    static Boolean pour(int from, int to) {

        // Amennyi át lesz öntve:
        // min ('honnan' kancsó tartalma, 'hova' kancsóban levő üres hely)
        int value = Math.min(states.lastElement().get(from), max[to] - states.lastElement().get(to));

        int fromErtek = states.lastElement().get(from);
        int toErtek = states.lastElement().get(to);

        // Operátor hatás
        fromErtek -= value;
        toErtek += value;

        // Utolsó érték másolása az újonnan felvettbe
        Vector<Integer> v = new Vector<>();
        v.add(states.lastElement().get(0));
        v.add(states.lastElement().get(1));
        v.add(states.lastElement().get(2));
        v.add(states.lastElement().get(3));
        states.add(v);
        // Az újonnan felvett értékek frissítése a jelenlegi öntés hatására
        states.lastElement().set(from, fromErtek);
        states.lastElement().set(to, toErtek);
        states.lastElement().set(3, 0);

        System.out.println("Sikeres öntés!");

        // Kör figyelés
        // Visszafele bejárjuk az állapotok halmazát és ellenőrizzük, hogy előfordult-e már a mostanival megegyező állapot
        // Az első (tehát utolsó) ilyen előforduló állapot tries (egy szám, hogy hanyadik operátort alkalmaztuk rajta utoljára) értékét kivesszük és elmentjük a most hozzáadott állapothoz
        for (int i=states.size()-1; i>=0; i--) {
            if (states.get(i).get(0) == states.lastElement().get(0) &&
                    states.get(i).get(1) == states.lastElement().get(1) &&
                    states.get(i).get(2) == states.lastElement().get(2)) {
                // Az utolsó elemet nem kell vizsgálni, mivel azzal hasonlítjuk
                if (states.size()-1 != i) {
                    System.out.println("Ezzel az állapottal már találkoztam, beállítom a tries értékét: "+states.get(i).get(3));
                    states.lastElement().set(3, states.get(i).get(3)+1);
                    break;                  // Csak az első előforduló megegyező állapotra van szükség
                }
            }
        }

        System.out.print("Az új állapot: ");
        printState();

        return true;
    }

    static void printState() {
        System.out.print("{");;
        for (int i = 0; i < 3; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(states.lastElement().get(i));
        }
        System.out.println("} tries: " + states.lastElement().get(3));
    }

    public static void main(String args[]) {

        int from, to;
        int cnt = 0;

        Boolean lastItemWasDeleted = false;

        // Kezdőállapot felvétele
        Vector<Integer> v = new Vector<Integer>();
        v.add(0);   // Első kancsó
        v.add(0);   // Második kancsó
        v.add(5);   // Harmadik kancsó
        v.add(0);   // Próbák száma (alkalmazott operátorok)
        states.add(v);

        System.out.print("Kezdőállapot: ");
        printState();
        System.out.println("\n-------------\n");

        while (!isGoalState()) {
            System.out.println("\n");
            System.out.println(cnt+". lefutás");
            states.forEach((n) -> System.out.println(n));
            //Thread.sleep(500);

            // Kör figyelés
            // Visszafele bejárjuk az állapotok halmazát és ellenőrizzük, hogy előfordult-e már a mostanival megegyező állapot
            // Az első (tehát utolsó) ilyen előforduló állapot tries (egy szám, hogy hanyadik operátort alkalmaztuk rajta utoljára) értékét kivesszük és eltároljuk, hogy innen folytathassuk az operátorok alkalmazását
            for (int i=states.size()-1; i>=0; i--) {
                if (states.get(i).get(0) == states.lastElement().get(0) &&
                        states.get(i).get(1) == states.lastElement().get(1) &&
                        states.get(i).get(2) == states.lastElement().get(2)) {
                    // Az utolsó elemet nem kell vizsgálni, mivel azzal hasonlítjuk
                    if (states.size()-1 != i) {
                        //tries = states.get(i).get(3);
                        System.out.print("Ezzel az állapottal utoljára próbált operátor: "+states.get(i).get(3));
                        //tries++;
                        states.get(i).set(3, states.get(i).get(3)+1);
                        System.out.println(" Növelem 1-el: "+states.get(i).get(3));
                        break;                  // Csak az első előforduló megegyező állapotra van szükség
                    }
                }
            }

            if (lastItemWasDeleted == true) {
                states.lastElement().set(3, states.lastElement().get(3)+1);
                System.out.println("Az utolsó elem törölve lett, növelem még 1-el a triest: "+states.lastElement().get(3));
                lastItemWasDeleted = false;
            }


            // Debug rész korlátozott lépésszámú futással
            /*if (cnt >= 40) {
                System.out.println("\n-----------\nFutás vége\nEddigi állapotok:\n");
                states.forEach((n) -> System.out.println(n));
                System.exit(1);
            }*/

            // Ellenőrizzük, hogy van-e még végrehajtható operátor
            if (states.lastElement().get(3) > 5) {
                System.out.println("Nincs több operátor, visszalépek, utolsó állapotot törlöm:");
                printState();
                states.remove(states.size()-1);
                System.out.println("Így az utolsó állapot:");
                printState();
                lastItemWasDeleted = true;
                continue;
            }

            cnt++;

            // Operátorok (from, to) inkrementális növelése
            from = operators[states.lastElement().get(3)][0];
            to = operators[states.lastElement().get(3)][1];
            System.out.print("From: "+from);
            System.out.println(" To: "+to);

            // Előzetes ellenőrzés, előbb ellenőrizzük és csak a helyes állapotokat tároljuk le, így nem kell visszalépni a fában ha hibás
            // Ha helytelen lenne az állapot a jelenlegi operátorokat használva, növeljük a jelenlegi (tehát utolsó) állapot tries értékét, jelölve hanyadik operátornál járunk
            for (int i = states.size() - 1; i >= 0; i--) {
                if (states.get(i).get(0) == states.lastElement().get(0) &&
                        states.get(i).get(1) == states.lastElement().get(1) &&
                        states.get(i).get(2) == states.lastElement().get(2)) {
                    printState();
                    states.get(i).set(3, states.lastElement().get(3));
                    break;
                }
            }

            // Operátor alkalmazás előfeltétel ellenőrzés
            if (!isValidOperation(from, to)) {
                states.lastElement().set(3, states.lastElement().get(3)+1);
                System.out.println("Hibás állapot lenne ha végrehajtanám, növelem a tries-t: "+states.lastElement().get(3));
                if (states.lastElement().get(3) >= 5) {
                    System.out.println("Nincs több operátor, visszalépek, utolsó állapotot törlöm:");
                    printState();
                    states.remove(states.size()-1);
                    System.out.println("Így az utolsó állapot:");
                    printState();
                    lastItemWasDeleted = true;
                    continue;
                }
                continue;
            }

            // Ha ideáig eljut a futás, akkor ez egy helyes állapot, eltároljuk a vektorban
            pour(from, to);
        }

    }
}
