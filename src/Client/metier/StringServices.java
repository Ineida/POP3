package Client.metier;

import java.util.Random;

public class StringServices {

    public static String generateRandomString(int length) {
        String seedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz_/!?.&";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Random rand = new Random();
        while (i < length) {
            sb.append(seedChars.charAt(rand.nextInt(seedChars.length())));
            i++;
        }
        return sb.toString();
    }
}
