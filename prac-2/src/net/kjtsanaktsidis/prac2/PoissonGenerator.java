package net.kjtsanaktsidis.prac2;

import java.util.Random;

public class PoissonGenerator {

    static Random rgen = new Random();

    public static int generate() {
        //keep flipping coins till we get a head
        int i = 0;
        do {
            i++;
        } while (rgen.nextInt(2) == 0);
        return i;
    }
}
