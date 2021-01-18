package com.suntelecoms.library_mifare.Util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sn.sensoft.cipher.CynodMobileWrapper;
import sn.sensoft.cipher.tool.Base64;
import sn.sensoft.cipher.tool.Converter;

public class SecurityUtil {
    private static final String LOG_TAG = SecurityUtil.class.getSimpleName();

    private static final String KEYSTORE_DIR = "ShowBox/key-files";
    private static final String KEYSTORE_NAME = "FO.keyStore.showBox.jks";
    private static final String KEY_STORE_PASSWORD = "P2017KeyStore";
    private static final String SECRET_KEY_A_ALIAS = "sn.sensoft.cipher.keya";
    private static final String SECRET_KEY_ALIAS = "sn.sensoft.cipher.secretkey";
    private static final String SECRET_KEY_B_PASSWORD = "Passer2017KeyB";
    private static final String SECRET_KEY_B_ALIAS = "sn.sensoft.cipher.keyb";
    private static final String SECRET_KEY_PASSWORD = "Passer2017SecretKey";
    private static final String SECRET_KEY_A_PASSWORD = "Passer2017KeyA";

    public static boolean initkeyStore(KeyStore keyStore) throws Exception {

        String keyStorePath = Environment.getExternalStorageDirectory().toString() + File.separator + KEYSTORE_DIR  + File.separator + KEYSTORE_NAME;

        String secretKey = keyStore.getSecret();
        String secretKeyA = keyStore.getKeyA();
        String secretKeyB = keyStore.getKeyB();

        return CynodMobileWrapper.initKeyStore(keyStorePath, KEY_STORE_PASSWORD,
                SECRET_KEY_ALIAS, SECRET_KEY_PASSWORD, secretKey, SECRET_KEY_A_ALIAS,
                SECRET_KEY_A_PASSWORD, secretKeyA, SECRET_KEY_B_ALIAS, SECRET_KEY_B_PASSWORD,
                secretKeyB);
    }

    public static String decrypt(String encrypted) throws Exception {
        if (TextUtils.isEmpty(encrypted)) {
            return null;
        }
        String keyStorePath = getKeyStorePath();
        if(TextUtils.isEmpty(keyStorePath)){
            Log.e(LOG_TAG, "Chemin fichier keystore non determiné !!");
            return null;
        }

        Byte[] decrypted = CynodMobileWrapper.decrypt(
                keyStorePath,
                KEY_STORE_PASSWORD,
                SECRET_KEY_ALIAS,
                SECRET_KEY_PASSWORD,
                Converter.convert(Base64.decodeBase64(encrypted.getBytes()))
        );

        return new String(Converter.convert(decrypted));
    }

    public static String getKeyStorePath(){
       return Environment.getExternalStorageDirectory().toString() + File.separator + KEYSTORE_DIR+ File.separator + KEYSTORE_NAME;
    }

    public static String encrypt(String text) throws Exception {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        String keyStorePath = getKeyStorePath();
        if(TextUtils.isEmpty(keyStorePath)){
            Log.e(LOG_TAG, "Chemin fichier keystore non determiné !!");
            return null;
        }

        Byte[] encrypted = CynodMobileWrapper.encrypt(
                keyStorePath,
                KEY_STORE_PASSWORD,
                SECRET_KEY_ALIAS,
                SECRET_KEY_PASSWORD,
                Converter.convert(text.getBytes())
        );

        return new String(Base64.encodeBase64(Converter.convert(encrypted)));
    }

    public static String md5Hash(String filePath) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(filePath);

            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();

            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            Log.e(LOG_TAG, "md5Hash: ", e);
            return null;
        }
    }
}
