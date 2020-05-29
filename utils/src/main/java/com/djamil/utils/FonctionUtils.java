package com.djamil.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Djvmil_ on 2020-02-12
 */

public class FonctionUtils {

    private static final String TAG = "FonctionUtils";

    public static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    public static String getNumberFloatFormat(Float val){

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRENCH);

        String valFormat = numberFormat.format(val);

        return valFormat;
    }

    public static String md5Hash(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            java.net.URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public static void playNotificationSound(Context ctx) {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + ctx.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(ctx, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert an array of bytes into a string of hex values.
     *
     * @param bytes
     *            Bytes to convert.
     * @return The bytes in hex string format.
     */
    public static String byte2HexString(byte[] bytes) {
        String ret = "";
        if (bytes != null) {
            for (Byte b : bytes) {
                ret += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return ret;
    }

    public static String hexString2Ascii(String hexString) {
        String s = System.getProperty("line.separator");
        String ascii = "";
        byte[] hex = hexStringToByteArray(hexString);
        for (int i = 0; i < hex.length; i++) {
            if (hex[i] < (byte) 0x20 || hex[i] == (byte) 0x7F) {
                hex[i] = (byte) 0x2E;
            }
        }
        // Hex to ASCII.
        try {
            ascii = (String) TextUtils.concat(ascii, " ", new String(hex,
                    "US-ASCII"), s);
            // Log.d("ascii", String.valueOf(ascii.length()));
            // Log.d("ascii trim length",
            // String.valueOf(ascii.trim().length()));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error while encoding to ASCII", e);
        }

        return ascii.trim();
    }

    /**
     * Convert a string of hex data into a byte array. Original author is: Dave
     * L. (http://stackoverflow.com/a/140861).
     *
     * @param s
     *            The hex string to convert
     * @return An array of bytes with the values of the string.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                /*
                 * Log.i("hexStringToByteArray",
                 * "s.charAt("+i+") >> "+String.valueOf(s.charAt(i))+", \n" +
                 * "s.charAt("+(i+1)+") >> "+
                 * String.valueOf(s.charAt(i+1))+", \n" +
                 * "Character.digit "+i+" >> "+(byte)
                 * (Character.digit(s.charAt(i), 16) << 4)+", \n" +
                 * "Character.digit "+(i+1)+" >> "+(byte)
                 * (Character.digit(s.charAt(i+1), 16)));
                 */

                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                        .digit(s.charAt(i + 1), 16));

                /*
                 * Log.i("hexStringToByteArray >> data["+(i / 2)+"]",
                 * String.valueOf(data[i / 2]));
                 */
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Argument(s) for hexStringToByteArray(String s)"
                    + "was not a hex string");
        }
        return data;
    }


    /**
     * Cette méthode permet de faire un test Luhn sur les numéros de compte
     *
     * @author mthiam
     * @param str
     *            le numéro de compte à vérifier
     * @return boolean
     */
    public static boolean validateAccount(String str) {
        try {
            int longueur = str.length();
            String reverse = new StringBuilder().append(str).reverse()
                    .toString();
            int sum = 0;
            for (int i = 0; i < longueur; i++) {
                int currVal = Integer.parseInt("" + reverse.charAt(i));
                if (i % 2 == 1) {
                    currVal *= 2;
                    if (currVal > 9) {
                        currVal -= 9;
                    }
                }
                sum += currVal;
            }
            return sum % 10 == 0;
        } catch (Exception e) {
            return false;
        }
    }

}
