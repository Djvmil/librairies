package com.suntelecoms.library_mifare.Util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.sensoftsarl.cynod.CynodMsg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.suntelecoms.library_mifare.Common;
import com.suntelecoms.library_mifare.database.Carte;
import sn.sensoft.cipher.CynodMobileWrapper;

import static com.suntelecoms.library_mifare.Activities.Preferences.Preference.UseInternalStorage;

/**
 * Created by sala on 08/01/2018.
 */

public  class NfcUtil {
    static String LOG_TAG = NfcUtil.class.getSimpleName();

    /** StartUpNode **/
    /**
     * Nodes (stats) MCT passes through during its startup.
     */
    public static enum StartUpNode {
        FirstUseDialog, DonateDialog, HasNfc, HasMifareClassicSupport,
        HasNfcEnabled, HasExternalNfc, ExternalNfcServiceRunning,
        HandleNewIntent
    }

    /** WRITING STATUS*/
    public final static int WRITE_OK =0;
    public final static int WRITE_ERROR_INDEX_OUT_OF_RANGE = 1;//,"1 - Sector index is out of range"),
    public final static int WRITE_ERROR_BLOCK_OUT_OF_RANGE = 2;//,"2 - Block index is out of range."),
    public final static int WRITE_ERROR_DATA_INVALID = 3; //,"3 - Data are not 16 byte."),
    public final static int WRITE_ERROR_AUTH= 4;//,"4 - Authentication went wrong."),
    public final static int WRITE_ERROR_WRITING = -1;//,"-1 - Error while writing to tag");
    public final static String lecteurCarteExterne = "lecteurCarteExterne";

    public static List<String> geAllSecretKeys(Context _context) {
        List<String> listSecretKeys;
        try {
            //Log.d(LOG_TAG, LOG_TAG+".geAllSecretKeys() >> DEBUT : " + DateUtils.currentDateToString());
            if (Common.getListSecretKeys() == null || Common.getListSecretKeys().isEmpty()){
                // Recup. clés de production
                List<String> allSecretKeys = new ArrayList<>(); //getListSecretKeysByFilename(getKeyFilenameList(_context));

                // Recup. clés par defaut
                for (String knownKeys : Common.SOME_CLASSICAL_KNOWN_KEYS) {
                    allSecretKeys.add(knownKeys);
                }

                // Sauvegarde dans la variable d'application
                Common.setListSecretKeys(allSecretKeys);

            }
            listSecretKeys = Common.getListSecretKeys();
            //Log.d(LOG_TAG, LOG_TAG+".geAllSecretKeys() >> FIN   : " + DateUtils.currentDateToString());

        } catch (Exception ex){
            listSecretKeys = null;
            Log.e(LOG_TAG, "geAllSecretKeys >> Exception");
            if (! TextUtils.isEmpty(ex.getMessage())){
                Log.e(LOG_TAG, "geAllSecretKeys >> message" + ex.getMessage());
            }
        }
        return listSecretKeys;
    }

    public static List<String> getListSecretKeysByFilename(List<String> keyFilenameList){
        //Log.d(LOG_TAG, LOG_TAG+".getListSecretKeysByFilename() >> DEBUT : " + DateUtils.currentDateToString());

        List<String> listSecretKeys = new ArrayList<>();
        if (keyFilenameList == null || keyFilenameList.isEmpty()){
            throw new IllegalArgumentException("Aucune information sur la liste des clés à recuperer");
        }
        try {

            for (String keyFilename: keyFilenameList ) {
                for (String secretKey: getSecretKeys(keyFilename)) {
                    //Log.d(LOG_TAG, "## getListSecretKeysByFilename : secretKey >> "+secretKey+", keyFilename "+keyFilename);
                    listSecretKeys.add(secretKey);
                }
            }
            //Log.d(LOG_TAG, LOG_TAG+".getListSecretKeysByFilename() >> FIN : " + DateUtils.currentDateToString());

        } catch (Exception ex){
            listSecretKeys = null;
            Log.e(LOG_TAG, "getListSecretKeys >> Exception");
            if (! TextUtils.isEmpty(ex.getMessage())){
                Log.e(LOG_TAG, "getListSecretKeys >> message" + ex.getMessage());
            }
        }

        return listSecretKeys;
    }


    public static List<String> getSecretKeys(String keyFilename) {
        //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() " + keyFilename +
        //		" >> DEBUT : " + DateUtils.currentDateToString());

        List<String> keys = new ArrayList<String>();
        File path = new File(getAppFileDir() + Common.KEYS_DIR);
        if (!path.exists()) {
            // Could not create directory.
            Log.e(LOG_TAG, "Error while creating '" + path.getAbsolutePath()+ Common.KEYS_DIR + "' directory.");
            return null;
        }

        // Tester si le keyStore existe sinon forcer à mettre à jour le
        // terminal(init keystore...)
        //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup fichier BEG : " + DateUtils.currentDateToString());

        File file = new File(getAppFileDir() + Common.KEYS_DIR, keyFilename);
        if (!file.exists()) {
            return null;
        }

        String keyStorePath = path.getAbsolutePath() + File.separator+ keyFilename;
        Log.i(LOG_TAG + " keyStorePath >> ", keyStorePath);
        String keyStorePwd = "Passer2013KeyStore";
        String secretKeyAAlias = "sn.sensoft.cipher.keya";
        String secretKeyAlias = "sn.sensoft.cipher.secretkey";
        String secretKeyBPwd = "Passer2013KeyB";
        String secretKeyBAlias = "sn.sensoft.cipher.keyb";
        String secretKeyPwd = "Passer2013SecretKey";
        String secretKeyAPwd = "Passer2013KeyA";

        try {
            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup secretKey BEG : " + DateUtils.currentDateToString());

            String secretKey = CynodMobileWrapper.getSecretKey(keyStorePath,
                    keyStorePwd, secretKeyAlias, secretKeyPwd);

            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup fichier END : " + DateUtils.currentDateToString());

            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup secretKeyA BEG : " + DateUtils.currentDateToString());

            String secretKeyA = CynodMobileWrapper.getKeyA(keyStorePath,
                    keyStorePwd, secretKeyAAlias, secretKeyAPwd);
            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup secretKeyA END : " + DateUtils.currentDateToString());

            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup secretKeyB BEG : " + DateUtils.currentDateToString());

            String secretKeyB = CynodMobileWrapper.getKeyB(keyStorePath,
                    keyStorePwd, secretKeyBAlias, secretKeyBPwd);
            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() >> REcup secretKeyB END : " + DateUtils.currentDateToString());


            if (secretKey != null) {
                keys.add(0, secretKey);
            }
            if (secretKeyA != null) {
                keys.add(1, secretKeyA);
            }
            if (secretKeyB != null) {
                keys.add(2, secretKeyB);
            }

            Log.i(LOG_TAG, LOG_TAG+".getSecretKeys() keyFilename >> "+keyFilename +
                    "\n  secretKey >> " + keys.get(0)+
                    "\n" +
                    " , secretKeyA >> " + keys.get(1)+
                    "\n" +
                    " , secretKeyB >> "+ keys.get(2));

            //Log.d(LOG_TAG, LOG_TAG+".getSecretKeys() " + keyFilename +
            //		" >> FIN : " + DateUtils.currentDateToString());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return keys;
    }

    /**
     *
     * Common.IS_KEYS_DIR_PRIVATE True => Repertoire privée de l'application, False => Répertoire public
     * @return
     */
    public static File getAppFileDir(){
        boolean isPrivate = Common.IS_KEYS_DIR_PRIVATE;
        File fileDir = null;
        if(isPrivate){
            ContextWrapper ctx = new ContextWrapper(Common.getAppContext());
            fileDir = ctx.getFilesDir();
            Log.i(LOG_TAG, "Récupération du repertoire de l'application en mode private >> "+fileDir.getAbsolutePath());
        }else{
            fileDir = Environment.getExternalStoragePublicDirectory(Common.HOME_DIR);
            if (fileDir.exists() == false && !fileDir.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating directory '" + fileDir+ "' directory.");
            }
            Log.i(LOG_TAG, "Récupération du repertoire de l'application en mode public >> "+fileDir.getAbsolutePath());
        }
        return fileDir;
    }


    public static CynodMsg getCynodMsg() throws Exception {
        /*
            @NotNull
            private String cardProfile;

            @NotNull
            private String ccnumber;

            @NotNull
            private Date expiry;

            @NotNull
            private String pin;

            private String pinDecrypted;

            @NotNull
            private String matricule;

            private String matriculeEncodeur;

            @NotNull
            private String name;

            @NotNull
            private Double balance;
         */

        Carte carte = new Carte();
        carte.setCcnumber("1234567");
        carte.setCardProfile("PF");
        carte.setMatriculeEncodeur("00001");
        carte.setMatricule("123456");
        carte.setExpiry(new Date());
        carte.setPin("1234");
        carte.setVersion(new Long('1'));
        carte.setSynced(false);
        carte.setDateEncodage(new Date());
        carte.setEncoded(true);
        carte.setName("Amadou Sala DIOP");
        carte.setBalance(200000.0);
        carte.setJobTitle("ADMINISTRATEUR");

        Log.i(LOG_TAG, carte.toString());
        return carte.toCynodMsg();
    }

    public static void copyStdKeysFilesIfNecessary(Context _context) {
        File std = Common.getFileFromStorage(Common.HOME_DIR + "/" + Common.KEYS_DIR + "/" + Common.STD_KEYS);
        File extended = Common.getFileFromStorage(Common.HOME_DIR + "/" +Common.KEYS_DIR + "/" + Common.STD_KEYS_EXTENDED);
        AssetManager assetManager = _context.getAssets();

        if (!std.exists()) {
            // Copy std.keys.
            try {
                InputStream in = assetManager.open(Common.KEYS_DIR + "/" + Common.STD_KEYS);
                OutputStream out = new FileOutputStream(std);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error while copying 'std.keys' from assets "+ "to external storage.");
            }
        }

        /** TODO Revoir s'il faut reactiver ultérieurement **/
        /*
        if (!extended.exists()) {
            // Copy extended-std.keys.
            try {
                InputStream in = assetManager.open(
                        Common.KEYS_DIR + "/" + Common.STD_KEYS_EXTENDED);
                OutputStream out = new FileOutputStream(extended);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
              } catch(IOException e) {
                  Log.e(LOG_TAG, "Error while copying 'extended-std.keys' "
                          + "from assets to external storage.");
              }
        }
        */
    }


    public static final int REQUEST_WRITE_STORAGE_CODE = 1;
    public static final int REQUEST_ENABLE_NFC = 1;
    public static final int REQUEST_CODE_ACTIVITY = 999;

    /**
     * Create the directories needed by MCT and clean out the tmp folder.
     */
    @SuppressLint("ApplySharedPref")
    public static void initFolders(Context _context) {
        // Change the storage for the second run.
        Common.getPreferences().edit().putBoolean(UseInternalStorage.toString(), false).commit();
        boolean isUseInternalStorage = Common.getPreferences().getBoolean(UseInternalStorage.toString(), false);
        Log.d(LOG_TAG, "isUseInternalStorage => "+isUseInternalStorage);

        // Run twice and init the folders on the internal and external storage.
        for (int i = 0; i < 2; i++) {
            if (!isUseInternalStorage && !Common.isExternalStorageWritableErrorToast(_context)) {
                continue;
            }

            // Create keys directory.
            File path = Common.getFileFromStorage(Common.HOME_DIR + "/" + Common.KEYS_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR + "/" + Common.KEYS_DIR + "' directory.");
                return;
            }

            // Create dumps directory.
            path = Common.getFileFromStorage(Common.HOME_DIR + "/" + Common.DUMPS_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR+ "/" + Common.DUMPS_DIR + "' directory.");
                return;
            }

            // Create tmp directory.
            path = Common.getFileFromStorage(Common.HOME_DIR + "/" + Common.TMP_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR + Common.TMP_DIR + "' directory.");
                return;
            }
            // Try to clean up tmp directory.
            File[] tmpFiles = path.listFiles();
            if (tmpFiles != null) {
                for (File file : tmpFiles) {
                    file.delete();
                }
            }


            // Create std. key file if there is none.
            copyStdKeysFilesIfNecessary(_context);

        }

    }


    public static void initNfc(Context _context){
        // Check if the user granted the app write permissions.
        if (Common.hasWritePermissionToExternalStorage(_context)) {
            NfcUtil.initFolders(_context);
        } else {
            // Request the permission.
            ActivityCompat.
                    requestPermissions((Activity) _context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_CODE);
        }
    }


}