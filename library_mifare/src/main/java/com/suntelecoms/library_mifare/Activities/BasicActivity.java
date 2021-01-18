/*
 * Copyright 2013 Gerhard Klostermeier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.suntelecoms.library_mifare.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.suntelecoms.library_mifare.Common;

/**
 * An Activity implementing the NFC foreground dispatch system overwriting
 * onResume() and onPause(). New Intents will be treated as new Tags.
 * @see Common#enableNfcForegroundDispatch(Activity)
 * @see Common#disableNfcForegroundDispatch(Activity)
 * @see Common#treatAsNewTag(Intent, Context)
 * @author Gerhard Klostermeier
 *
 */
public abstract class BasicActivity extends Activity {
    private static final String LOG_TAG = BasicActivity.class.getSimpleName();
    /**
     * Enable NFC foreground dispatch system.
     * @see Common#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onResume() {
        super.onResume();
        Common.setPendingComponentName(this.getComponentName());
        Common.enableNfcForegroundDispatch(this);
        //resetDisconnectTimer();
    }

    /**
     * Disable NFC foreground dispatch system.
     * @see Common#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        Common.disableNfcForegroundDispatch(this);
        super.onPause();
        //stopDisconnectTimer();
    }

    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support MIFARE Classic, then run { link TagInfoTool}.
     * @see Common#treatAsNewTag(Intent, Context)
     * see TagInfoTool
     */
    @Override
    public void onNewIntent(Intent intent) {
        int typeCheck = Common.treatAsNewTag(intent, this);
        if (typeCheck == -1 || typeCheck == -2) {
            // Device or tag does not support MIFARE Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoTool.class);
            startActivity(i);
        }
    }







    /** DEB **/
 /*   public static final int RESULT_CODE_TEMPS_INACTIF = 1;
    public String TAG = TimeOutActivity.class.getName();
    long compteur;
    TextView compteARebours;
    CountDownTimer countDownTimer;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    // public String DISCONNECT_TIMEOUT;
    // private String[] TIME_SPLIT;
    private int timeInactif;
    private int default_timeout_time = 60 * 100000;
    private Handler disconnectHandler = new Handler();
    *//** END **//*

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            compteARebours();
        }
    };


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        timeInactif = default_timeout_time;
        Timer timertaskSystemGC = new Timer();
        timertaskSystemGC.schedule(new initSystemGC(), 5000, 5000);
    }

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, timeInactif);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetDisconnectTimer();
    }

    *//*@Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }*//*

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    *//*@Override
    public void onPause() {
        super.onPause();
        stopDisconnectTimer();
    }*//*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "-- onActivityResult : requestCode = "+requestCode+" --");
        switch (requestCode) {

            case Constantes.REDIRECT_ACTIVITY:
                if (resultCode == TimeOutActivity.RESULT_CODE_TEMPS_INACTIF) {
                    finish();
                }
                break;
            default:
                Toast.makeText(this, "Result ne correpond à", Toast.LENGTH_LONG).show();
                break;

        }
    }

    public void compteARebours() {
        Log.i(TAG, "-- compteARebours --");
        alertDialogBuilder = new AlertDialog.Builder(BasicActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.compte_a_rebours, null);
        alertDialogBuilder.setView(layout);


        Button btnContinuer = (Button) layout.findViewById(R.id.btnContinuer);
        Button btnAnnuler = (Button) layout.findViewById(R.id.btnAnnuler);
        compteARebours = (TextView) layout.findViewById(R.id.compteARebours);

        countDownTimer = new CountDownTimer(10000, 1000) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTick(long millisUntilFinished) {
                compteur = millisUntilFinished / 1000;

                compteARebours.setText(String.valueOf(compteur));
                //  alertDialog.setMessage("00:"+ (millisUntilFinished/1000));

                if (compteur == 5) {
                    compteARebours.setBackground(getResources().getDrawable(R.drawable.shape_circle_red));
                }

                if (compteur == 1) {
                    Intent intent = new Intent();
                    setResult(RESULT_CODE_TEMPS_INACTIF, intent);

//                        EncodageScreenActivity.mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//                        EncodageScreenActivity.mReader    = new Reader(EncodageScreenActivity.mManager);
                   *//* if (NfcReaderUtil.mReader != null) {
                        NfcReaderUtil.mReader.close();
                    }*//*

                    finish();

                }
            }

            @Override
            public void onFinish() {
                //  info.setVisibility(View.GONE);
            }
        }.start();


        alertDialog = alertDialogBuilder.create();

        btnContinuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                Intent intent = new Intent();
                setResult(RESULT_CODE_TEMPS_INACTIF, intent);
                finish();
            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                countDownTimer.cancel();
                resetDisconnectTimer();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertDialog.show();
                }
            });
        } catch (WindowManager.BadTokenException ex) {
            if (ex != null) {
                Log.d(TAG, "BadTokenException" + ex.getMessage());
            }
        }

    }

    class initSystemGC extends TimerTask {
        public void run() {
            Log.d(TAG, " initSystemGC ");
            deleteCache(getApplicationContext());
            System.gc();
        }
    }*/
}
