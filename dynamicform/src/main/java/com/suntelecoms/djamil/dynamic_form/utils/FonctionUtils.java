package com.suntelecoms.djamil.dynamic_form.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Djvmil_ on 2020-02-12
 */

public class FonctionUtils {
    public static final String BASE_URL = "http://51.254.219.103:8787/gwBiller/helpers/logo/";
    public static int SUMMARY_ID = 0;

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

    public static String formatDate(String dateFormat) {

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

     return (new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)).format(date);
    }



}
