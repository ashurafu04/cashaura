package util;

import java.util.Random;

public class RIBGenerator {
    public static String generateRIB() {
        Random rand = new Random();
        StringBuilder rib = new StringBuilder("MA");
        for (int i = 0; i < 24; i++) {
            rib.append(rand.nextInt(10));
        }
        return rib.toString();
    }
}
