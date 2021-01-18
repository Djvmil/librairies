package com.suntelecoms.library_mifare.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sensoftsarl.cynod.CynodMsg;
import com.sensoftsarl.cynod.Utils;
import com.suntelecoms.library_mifare.BuildConfig;
import com.suntelecoms.library_mifare.Common;
import com.suntelecoms.library_mifare.MCReader;
import com.suntelecoms.library_mifare.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import com.suntelecoms.library_mifare.Activities.Preferences.Preference;
import com.suntelecoms.library_mifare.Util.Constantes;

import static com.suntelecoms.library_mifare.Activities.Preferences.Preference.UseInternalStorage;

public class ReadAllSectors extends BasicActivity {
    //==========================================================================================================
        // Input parameters.
        /**
         * Path to a directory with key files. The files in the directory
         * are the files the user can choose from. This must be in the Intent.
         */
        public final static String EXTRA_KEYS_DIR = Common.getFileFromStorage(Common.HOME_DIR + "/" + Common.KEYS_DIR).getAbsolutePath();

        // Sector count of the biggest MIFARE Classic tag (4K Tag)
        public static final int MAX_SECTOR_COUNT = 40;
        // Block count of the biggest sector (4K Tag, Sector 32-39)
        public static final int MAX_BLOCK_COUNT_PER_SECTOR = 16;

        private static final String LOG_TAG = ReadAllSectors.class.getSimpleName();

        private static final int DEFAULT_SECTOR_RANGE_FROM = 0;
        private static final int DEFAULT_SECTOR_RANGE_TO = 15;


 
        private final Handler mHandler = new Handler();
        private int mProgressStatus;
        //private ProgressBar mProgressBar;
        private boolean mIsCreatingKeyMap;
        private File mKeyDirPath;
        private int mFirstSector;
        private int mLastSector;

    //==========================================================================================================

    private final static int KEY_MAP_CREATOR = 1;
    public final static int RESULT_READ_INCOMPLETED = 10;
    public final static int RESULT_READ_BLANK = 6;
    public final static int RESULT_READ_UNKNOWN_ERROR = 4;

    CynodMsg cynodMsg = null;
    Intent intentResult = null;

    private SparseArray<String[]> mRawDump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_all_sectors);

        Log.e(LOG_TAG, "onCreate: " );
        intentResult = new Intent();

        if (! Common.getPreferences().getBoolean(UseInternalStorage.toString(),
                false) && !Common.isExternalStorageWritableErrorToast(this)) {

            Log.e(LOG_TAG, "finish: " );
            finish();
            return;
        }

    }


    /**
     * List files from the {@link #EXTRA_KEYS_DIR} and select the last used
     * ones if {@link Preference#SaveLastUsedKeyFiles} is enabled.
     */
    @Override
    public void onStart() {
        super.onStart();

        if (mKeyDirPath == null) {
            mKeyDirPath = new File(EXTRA_KEYS_DIR);
        }

        // Is external storage writable?
        if (!Common.getPreferences().getBoolean(UseInternalStorage.toString(), false) && !Common.isExternalStorageWritableErrorToast(this)) {
            setResult(3);
            finish();
            return;
        }

        // Does the directory exist?
        if (!mKeyDirPath.exists()) {
            setResult(1);

            finish();
            return;
        }

        // List key files and select last used (if corresponding
        // setting is active).
        boolean selectLastUsedKeyFiles = Common.getPreferences().getBoolean(Preference.SaveLastUsedKeyFiles.toString(), true);
        ArrayList<String> selectedFiles = null;
        if (selectLastUsedKeyFiles) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            // All previously selected key files are stored in one string
            // separated by "/".
            String selectedFilesChain = sharedPref.getString(
                    "last_used_key_files", null);
            if (selectedFilesChain != null) {
                selectedFiles = new ArrayList<>(Arrays.asList(selectedFilesChain.split("/")));
            }
        }

        File[] keyFilesListe = mKeyDirPath.listFiles();


        if (keyFiles != null)
            keyFiles.addAll(Arrays.asList(keyFilesListe));


    }
    ArrayList<File> keyFiles = new ArrayList<>();

    public void onCancelCreateKeyMap(View view) {
        if (mIsCreatingKeyMap) {
            mIsCreatingKeyMap = false;
        } else {
            finish();
        }
    }

    public void onCreateKeyMap(View view) {

        Log.e(LOG_TAG, "onCreateKeyMap: " );
        // Create reader.
        MCReader reader = Common.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }

        // Set key files.
        File[] keys = keyFiles.toArray(new File[keyFiles.size()]);
        if (!reader.setKeyFile(keys, this)) {
            // Error.
            reader.close();
            return;
        }

        // Don't turn screen of while mapping.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Read all.
        mFirstSector = 0;
        mLastSector = reader.getSectorCount()-1;
        // Set map creation range.
        if (!reader.setMappingRange(
                mFirstSector, mLastSector)) {
            // Error.
            Toast.makeText(this, R.string.info_mapping_sector_out_of_range, Toast.LENGTH_LONG).show();
            reader.close();
            return;
        }
        Common.setKeyMapRange(mFirstSector, mLastSector);
        // Init. GUI elements.
        mProgressStatus = -1;
        //mProgressBar.setMax((mLastSector - mFirstSector)+1);
        //mCreateKeyMap.setEnabled(false);
        mIsCreatingKeyMap = true;
        Toast.makeText(this, R.string.info_wait_key_map, Toast.LENGTH_SHORT).show();
        // Read as much as possible with given key file.
        createKeyMap(reader, this);
    }

    private void createKeyMap(final MCReader reader, final Context context) {
        new Thread(() -> {
            // Build key map parts and update the progress bar.
            while (mProgressStatus < mLastSector) {
                mProgressStatus = reader.buildNextKeyMapPart();
                if (mProgressStatus == -1 || !mIsCreatingKeyMap) {
                    // Error while building next key map part.
                    break;
                }

               // mHandler.post(() -> mProgressBar.setProgress((mProgressStatus - mFirstSector) + 1));
            }

            mHandler.post(() -> {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                reader.close();
                if (mIsCreatingKeyMap && mProgressStatus != -1) {
                    keyMapCreated(reader);
                } else {
                    // Error during key map creation.
                    Common.setKeyMap(null);
                    Common.setKeyMapRange(-1, -1);
                    Toast.makeText(context, R.string.info_key_map_error, Toast.LENGTH_LONG).show();
                }
                mIsCreatingKeyMap = false;
            });
        }).start();
    }

    private void keyMapCreated(MCReader reader) {
        // LOW: Return key map in intent.
        if (reader.getKeyMap().size() == 0) {
            Common.setKeyMap(null);
            // Error. No valid key found.
            Toast.makeText(this, R.string.info_no_key_found, Toast.LENGTH_LONG).show();
        } else {
            Common.setKeyMap(reader.getKeyMap());
//            Intent intent = new Intent();
//            intent.putExtra(EXTRA_KEY_MAP, mMCReader);
//            setResult(Activity.RESULT_OK, intent);

            readTag();
            //setResult(Activity.RESULT_OK);
            //finish();
        }

    }


    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support MIFARE Classic, then run {@link TagInfoTool}.
     * @see Common#treatAsNewTag(Intent, Context)
     * @see TagInfoTool
     */
    @Override
    public void onNewIntent(Intent intent) {
        int typeCheck = Common.treatAsNewTag(intent, this);
        Log.d(LOG_TAG, "onNewIntent -> override typeCheck = "+typeCheck);
        if (typeCheck == -1 || typeCheck == -2) {
            // Device or tag does not support MIFARE Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoTool.class);
            startActivity(i);
        }else{
            Log.d(LOG_TAG, "onNewIntent -> override");
            onCreateKeyMap(null);
        }

    }

/*
    private void readTag() {
        final MCReader reader = Common.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }
        new Thread(() -> {
            // Get key map from glob. variable.
            mRawDump = reader.readAsMuchAsPossible(
                    Common.getKeyMap());

            reader.close();

            mHandler.post(() -> Log.e(LOG_TAG, "readTag: "+ mRawDump));
        }).start();
    }*/



    /**
     * Triggered by {@link #onActivityResult(int, int, Intent)}
     * this method starts a worker thread that first reads the tag and then
     * calls { link createTagDump(SparseArray)}.
     */
    public void readTag() {
        final MCReader reader = Common.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }
        new Thread(() -> {
            // Get key map from glob. variable.
            mRawDump = reader.readAsMuchAsPossible(Common.getKeyMap());
            if(mRawDump == null){
                //throw new Exception("La carte n'est plus accesssible, veuillez la rapprocher du terminal...");
                setResult(RESULT_READ_INCOMPLETED);
                finish();
            }else{
                StringBuffer sb = new StringBuffer();
                /* Boucler sur les secteurs */
                for( int i = 0; i < mRawDump.size(); i++){
                    String [] rows = mRawDump.get(i);
                    if (rows != null && rows.length >=0){
                        //Récupére tous les blocks sauf le dernier de chaque secteur (ce block etant reservé au clés)
                        for (int j = 0; j < rows.length - 1; j++){
                            //Ne pas lire le premier Block du premier Secteur

                            String row = rows[j];

                            if(Common.isHexAnd16Byte(row,getApplicationContext()) && ! TextUtils.equals(row, Constantes._blankBlock)){
                                // Log.i("Hexadecimal >> sector "+i+", block "+j, row);
                                if ((i == 0) && (j == 0)){ //Factory Data
                                    /*sb.append("=FACTORY BLOCK=="); // TODO a recuperer*/
                                    Log.i("Factory Block " , "=FACTORY BLOCK==");
                                }else{
                                    String str = Utils.hexStringToAscii(row);
                                    sb.append(str);
                                    if(BuildConfig.DEBUG){
                                        Log.d(LOG_TAG,"hexStringToAscii >> sector "+i+", block "+j+", Size = "+str.length()+", value = "+str);
                                    }
                                }
                            }
                        }// end for
                    }
                }

                if(BuildConfig.DEBUG){
                    // Log.d("Trame encodée lue >> ", sb == null ? "Aucune donnée lue " : sb.toString()+"-Taille = "+String.valueOf(sb.toString().length()));
                }

                if (sb == null || TextUtils.isEmpty(sb.toString())){
                    Log.e(LOG_TAG, "La carte a t-elle été initialisée, Aucune informaton n'a été lue !!");
                    setResult(RESULT_READ_BLANK);
                    finish();
                }

                cynodMsg = new CynodMsg();
                cynodMsg.parseMsg(sb.toString());
                Log.d(LOG_TAG, "sb.toString() => ["+sb.toString()+"]");
                cynodMsg.fillDataInBlock1K(sb.toString());
                Log.d(LOG_TAG, "dumpMsg()     => ["+cynodMsg.dumpMsg()+"]");
                cynodMsg.setmUID(Common.getUID());
                intentResult.putExtra("cynodMsg", cynodMsg);
                intentResult.putExtra("trame", sb.toString());

                /** **/
                /*try{
                    Carte carte =  new Carte(cynodMsg);
                    Log.e(LOG_TAG, "CARTE => "+carte.toString());
                }catch (Exception ex){
                    ex.printStackTrace();
                    Log.e(LOG_TAG, "EXCEPTION ON -> "+ex.getLocalizedMessage());
                }*/

                if(BuildConfig.DEBUG){
                    Log.i("## CURRENT UID ##", new String(cynodMsg.getmUID()));
                }

            }
            reader.close();

            mHandler.post(() -> respondRead());  //createTagDump(mRawDump));
        }).start();
    }



    public void respondRead(){
        if(cynodMsg == null){
            setResult(RESULT_READ_BLANK);
        }else{
            setResult(Activity.RESULT_OK, intentResult);
        }
        finish();
    }


}