package com.suntelecoms.library_mifare.Util;

import android.util.Log;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ConversionUtil {
    private static final String LOG_TAG = ConversionUtil.class.getSimpleName();

    public static int booleanToInt(Boolean bool) {
        return bool != null && bool ? 1 : 0;
    }

    public static boolean intToBoolean(int i) {
        return i != 0;
    }

    public static BigDecimal doubleToBigDecimal(Double d) {
        if (d == null) {
            return null;
        }

        return new BigDecimal(d);
    }

    public static Double bigDecimalToDouble(BigDecimal bd) {
        if (bd == null) {
            return null;
        }
        return bd.doubleValue();
    }

    public static String replaceSpecialCharacters(String text) {
        try {
            text = text.replaceAll("[ÈÉÊË]", "E");
            text = text.replaceAll("[ÀÁÂÃÄÅ]", "A");
            text = text.replaceAll("[ÀÁÂÃÄÅ]", "A");

            text = text.replaceAll("[àáâãäå]", "a");
            text = text.replaceAll("[ÈÉÊË]", "E");
            text = text.replaceAll("[èéêë]", "e");
            text = text.replaceAll("[ÍÏÎÌ]", "I");
            text = text.replaceAll("[íìîï]", "i");
            text = text.replaceAll("[Ç]", "C");
            text = text.replaceAll("[ç]", "c");
            text = text.replaceAll("[Ñ]", "N");
            text = text.replaceAll("[ñ]", "n");
            text = text.replaceAll("[ÓÒÔÖÕ]", "O");
            text = text.replaceAll("[óòôöõ]", "o");
            text = text.replaceAll("[ÚÙÛÜ]", "U");
            text = text.replaceAll("[úùûü]", "u");
            text = text.replaceAll("[Ý]", "Y");
            text = text.replaceAll("[ýÿ]", "y");
            text = text.replaceAll("[.]", "/");
            //text = text.replaceAll("[\\x0B]", " ");
            //text = text.replaceAll("\\s", " ");

            //return text.trim();
            return text;

        } catch (Exception e) {
            Log.e(LOG_TAG, "replaceSpecialCharacters >>> ", e);
            return null;
        }
    }


    public static String replaceSpecialCharactersWithoutDot(String text) {
        try {
            text = text.replaceAll("[ÈÉÊË]", "E");
            text = text.replaceAll("[ÀÁÂÃÄÅ]", "A");
            text = text.replaceAll("[ÀÁÂÃÄÅ]", "A");

            text = text.replaceAll("[àáâãäå]", "a");
            text = text.replaceAll("[ÈÉÊË]", "E");
            text = text.replaceAll("[èéêë]", "e");
            text = text.replaceAll("[ÍÏÎÌ]", "I");
            text = text.replaceAll("[íìîï]", "i");
            text = text.replaceAll("[Ç]", "C");
            text = text.replaceAll("[ç]", "c");
            text = text.replaceAll("[Ñ]", "N");
            text = text.replaceAll("[ñ]", "n");
            text = text.replaceAll("[ÓÒÔÖÕ]", "O");
            text = text.replaceAll("[óòôöõ]", "o");
            text = text.replaceAll("[ÚÙÛÜ]", "U");
            text = text.replaceAll("[úùûü]", "u");
            text = text.replaceAll("[Ý]", "Y");
            text = text.replaceAll("[ýÿ]", "y");
            //text = text.replaceAll("[\\x0B]", " ");
            //text = text.replaceAll("\\s", " ");

            //return text.trim();
            return text;

        } catch (Exception e) {
            Log.e(LOG_TAG, "replaceSpecialCharacters >>> ", e);
            return null;
        }
    }

    static public String displayNumber(BigDecimal number) {

        String numberTodiplay = "";
        try {
            NumberFormat numberFormatter;
            numberFormatter = NumberFormat.getNumberInstance(Locale.FRANCE);


            if (number != null) {
                numberTodiplay = numberFormatter.format(number);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "EXCEPTION StringUtils.formatString" + e.getMessage());
            numberTodiplay = "";
            e.printStackTrace();
        }
        return numberTodiplay;
    }

}
