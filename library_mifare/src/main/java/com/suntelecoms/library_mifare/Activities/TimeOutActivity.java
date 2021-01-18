package com.suntelecoms.library_mifare.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.suntelecoms.library_mifare.R;
import com.suntelecoms.library_mifare.Util.Constantes;

/**
 * Created by sala on 14/11/19.
 */
public class TimeOutActivity extends BasicActivity {


    //   private int oneMiliSeconde = 1000;
    public static final int RESULT_CODE_TEMPS_INACTIF = 1;
    public String TAG = TimeOutActivity.class.getName();
    long compteur;
    TextView compteARebours;
    CountDownTimer countDownTimer;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    // public String DISCONNECT_TIMEOUT;
    // private String[] TIME_SPLIT;
    private int timeInactif;
    private int default_timeout_time = 60 * 3000;
    private Handler disconnectHandler = new Handler();
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

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopDisconnectTimer();
    }

    // classe permetant d'initer le garbage colector et de vider la cache

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case Constantes.REDIRECT_ACTIVITY:
                if (resultCode == TimeOutActivity.RESULT_CODE_TEMPS_INACTIF) {
                    finish();
                }
                break;
            default:
                // Toast.makeText(this, "Result ne correpond à", Toast.LENGTH_LONG).show();
                break;

        }
    }

    public void compteARebours() {

        alertDialogBuilder = new AlertDialog.Builder(TimeOutActivity.this);

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

                    //EncodageScreenActivity.mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    //EncodageScreenActivity.mReader    = new Reader(EncodageScreenActivity.mManager);

                    /*
                    if (NfcReaderUtil.mReader != null) {
                        NfcReaderUtil.mReader.close();
                    }
                    */

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
    }

}
