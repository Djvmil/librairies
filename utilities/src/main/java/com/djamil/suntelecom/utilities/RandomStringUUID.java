package com.djamil.suntelecom.utilities;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 4/15/20
 */
import java.security.SecureRandom;
import java.util.UUID;

public class RandomStringUUID {
    public static String getUUID() {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        String randomUUIDString       = uuid.toString();
        String randomUUIDuniqueString = RandomUtil.unique();

        System.out.println("Random unique UUID String = " + randomUUIDString);
/*
        System.out.println("Random UUID String = " + randomUUIDString);
        System.out.println("UUID version       = " + uuid.version());
        System.out.println("UUID variant       = " + uuid.variant());

        System.out.println(UUID.randomUUID().toString());
        System.out.println(RandomUtil.unique());
        System.out.println(RandomUtil.unique());
        System.out.println(RandomUtil.unique());

        System.out.println();
        System.out.println("random   = "+ Long.toHexString(0x8000000000000000L |21));
        System.out.println("random   = "+ Long.toBinaryString(0x8000000000000000L |21));
        System.out.println("random   = "+ Long.toHexString(Long.MAX_VALUE + 1));
*/

        return randomUUIDuniqueString;
    }

     static class RandomUtil {
        // Maxim: Copied from UUID implementation :)
        private static volatile SecureRandom numberGenerator = null;
        private static final long MSB = 0x8000000000000000L;

        static String unique() {
            SecureRandom ng = numberGenerator;
            if (ng == null) {
                numberGenerator = ng = new SecureRandom();
            }

            return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
        }
    }

}