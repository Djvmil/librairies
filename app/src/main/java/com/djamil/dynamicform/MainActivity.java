package com.djamil.dynamicform;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.djamil.authenticate_utils.interfaces.OnResultAuth;
import com.djamil.contactlist.ContactList;
import com.djamil.authenticate_utils.Authenticate;
import com.djamil.contactlist.ContactsInfo;
import com.djamil.contactlist.interfaces.OnClickContactListener;
import com.djamil.utils.DateUtils;
import com.example.elcapi.jnielc;
import com.google.gson.Gson;
import com.instacart.library.truetime.TrueTimeRx;
import com.suntelecoms.authenticate.activity.AuthenticateActivity;
import com.suntelecoms.authenticate.pinlockview.OnAuthListener;
import com.suntelecoms.timeline.TimelineView;
import com.suntelecoms.timeline.models.ItemTimeline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
//import com.suntelecoms.library_mifare.Activities.ReadAllSectors;
//import com.suntelecoms.library_mifare.Activities.WaitForReadCard;


public class MainActivity extends AppCompatActivity implements OnAuthListener {
    private static final String TAG = "MainActivity";

    private ContactList contactList;
    private TextView contactResult, trueTime;
    private static Authenticate authenticate;
    private EditText btnkeyboardview;

    private static final String FONT_TEXT = "font/ALEAWB.TTF";
    private static final String FONT_NUMBER = "font/BLKCHCRY.TTF";

    private static final int REQUEST_CODE = 123;
    private static final int seek_red_right = 0xa1;
    private static final int seek_green_right = 0xa2;
    private static final int seek_blue_right = 0xa3;
    private static final int seek_green_blue_right = 0xa4;
    private static final int seek_red_blue_right = 0xa5;
    private static final int seek_red_green_right = 0xa6;
    private static final int seek_all_right = 0xa7;

    private static final int seek_red_left = 0xb1;
    private static final int seek_green_left = 0xb2;
    private static final int seek_blue_left = 0xb3;
    private static final int seek_green_blue_left = 0xb4;
    private static final int seek_red_blue_left = 0xb5;
    private static final int seek_red_green_left = 0xb6;
    private static final int seek_all_left = 0xb7;
    TimelineView timelineView;


    public static String getDayStringOld(Date date, Locale locale, String patern) {
        DateFormat formatter = new SimpleDateFormat(patern, locale);
        return formatter.format(date);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timelineView  = findViewById(R.id.timeline);
        SeekBar seekBar = findViewById(R.id.seekBar);
        SeekBar seekBar_left = findViewById(R.id.seekBar_left);
        SeekBar seekBar_right = findViewById(R.id.seekBar_right);
        trueTime = findViewById(R.id.true_time);


        trueTime.setText(DateUtils.Companion.dateToString(TrueTimeRx.now(), DateUtils.FORMAT_FRENCH_MEDIUM));
        seekLed(seekBar, seekBar_left, seekBar_right);

        String text = "980828454//00110|Mounirou|TANDIANG|Responsable Support|SUPPORT TECHNIQUE//";

        String []split = text.split("//");

        Log.e(TAG, "onCreate => 1: "+split.length);
        Log.e(TAG, "onCreate => 2: "+ Arrays.toString(split));

        String []split1 = split[1].split("\\|");
        //String [] split1 = split[1].split("\\|");
        Log.e(TAG, "onCreate => 3: "+ Arrays.toString(split));
        Log.e(TAG, "onCreate => 4: "+ Arrays.toString(split1));
        Log.e(TAG, "onCreate => 5: "+ split1[0]);
        Log.e(TAG, "onCreate => 6: "+ text.split("//")[1].split("\\|")[0]);


/*
        findViewById(R.id.dynamic_form).setOnClickListener(view -> {
            //startActivity(new Intent(MainActivity.this, MainMenu.class));
            Log.e(TAG, "onCreate: " );
            Intent readTag = new Intent(MainActivity.this, ReadAllSectors.class);
            //readTag.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(readTag, 234);

        });

        findViewById(R.id.contact_list).setOnClickListener(view ->
            startActivity(new Intent(MainActivity.this, WaitForReadCard.class))
        );*/



        contactResult = findViewById(R.id.contact_result);
        authenticate  = findViewById(R.id.dynamic_key);
        //btnkeyboardview  = findViewById(R.id.keyboard_view1);

        authenticate.setSecret("0000", false).setOnResultAuth(new OnResultAuth() {
            @Override
            public void onAuthError(int errorCode) {

            }

            @Override
            public void onAuthFailed(int typeAuth) {

            }

            @Override
            public void onAuthSucceeded(String pwd, String pwdMd5) {

            }

            @Override
            public void onAttempts(int nbAttempts) {
                Log.e(TAG, "onAttempts: "+nbAttempts );
            }

            @Override
            public void onDoneClicked(String pwd, String pwdMd5, boolean isSuccess) {
                if (!isSuccess){
                    authenticate.setMsgError("Unauthorized");
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            authenticate.useFingerPrintForAuth(true);
            Authenticate.Result result = authenticate.checkFingerPrint(this);
            Log.e(TAG, "onCreate: "+result.toString());
        }

        contactList = ContactList.getInstance(this);
        contactList.setOnClickContactListener(new OnClickContactListener() {
            @Override
            public void onClickContact(View v, ContactsInfo contactsInfo) {
                Log.e(TAG, "onClickCantact: "+ contactsInfo.getDisplayName());
                contactResult.setText(contactsInfo.getDisplayName().concat("\n"+contactsInfo.getPhoneNumber()));
            }

            @Override
            public void onSelectClickContact(ArrayList<ContactsInfo> contactsInfo) {
                Log.e(TAG, "onClickCantact: "+ contactsInfo.toString());

            }
        });

        Button btn = findViewById(R.id.dynamic_form);
        //    btn.setText(getDayStringOld(new Date(), Locale.FRENCH, "EEE | dd | MMMM | yyyy"));
        findViewById(R.id.dynamic_form).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, FormulaireActivity.class)));

        findViewById(R.id.keyboard_view).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                authenticate.useFingerPrintForAuth(false);
            }
        });
        findViewById(R.id.contact_list).setOnClickListener(view -> contactList.showContactList(true, 2, ""));
        Button normal = findViewById(R.id.normal);
        Button setPin = findViewById(R.id.setPin);
        Button setFont = findViewById(R.id.setFont);
        Button setPinAndFont = findViewById(R.id.setPinAndFont);

        normal.setOnClickListener(v -> {

            // start the activity, It handles the setting and checking
            Intent intent12 = new Intent(MainActivity.this, AuthenticateActivity.class);
//                startActivity(intent);

            // for handling back press
            startActivityForResult(intent12, REQUEST_CODE);
        });

        loadTimeline();

        setPinAndFont.setOnClickListener(v -> {
            Intent intent2 = AuthenticateActivity.Companion.getIntent(MainActivity.this, false, null, null, 6);
            //AuthenticateActivity.Companion.setGoneBtnBack(true);
            AuthenticateActivity.Companion.setIcon(R.drawable.logo_aicha);
            AuthenticateActivity.Companion.setOnAuthListener(MainActivity.this);
            AuthenticateActivity.Companion.setShuffle(true);
            AuthenticateActivity.Companion.setCloseAfterAttempts(true);
            AuthenticateActivity.Companion.setUseFingerPrint(true);
            startActivity(intent2);

        });

        setPin.setOnClickListener(v -> {

            // set pin instead of checking it
            Intent intent1 = AuthenticateActivity.Companion.getIntent(MainActivity.this, true, null, null, 6);
            //AuthenticateActivity.Companion.setGoneBtnBack(true);
            AuthenticateActivity.Companion.setIcon(R.drawable.logo_aicha);
            AuthenticateActivity.Companion.setOnAuthListener(MainActivity.this);
            AuthenticateActivity.Companion.setShuffle(false);
            AuthenticateActivity.Companion.setCloseAfterAttempts(true);
            AuthenticateActivity.Companion.setUseFingerPrint(true);
            startActivity(intent1);
        });

/*
        setFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // setting font for library
                Intent intent = AuthenticateActivity.getIntent(MainActivity.this, false, FONT_TEXT, FONT_NUMBER, MainActivity.this, false, R.drawable.logo_aicha, true, false);
                startActivity(intent);
            }
        });

        setPinAndFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // setting font for library and set pin instead of checking it
                Intent intent = AuthenticateActivity.getIntent(MainActivity.this, true, FONT_TEXT, FONT_NUMBER, MainActivity.this, false, R.drawable.logo_aicha, true, false);
                startActivity(intent);
            }
        });
*/

        Intent intent = AuthenticateActivity.Companion.getIntent(MainActivity.this, false, null, null, 6);
        //AuthenticateActivity.Companion.setGoneBtnBack(true);
        AuthenticateActivity.Companion.setIcon(R.drawable.logo_aicha);
        AuthenticateActivity.Companion.setOnAuthListener(MainActivity.this);
        AuthenticateActivity.Companion.setShuffle(true);
        AuthenticateActivity.Companion.setCloseAfterAttempts(false);
        AuthenticateActivity.Companion.setUseFingerPrint(true);
        findViewById(R.id.authenticate).setOnClickListener(view -> startActivity(intent));
    }

    @Override
    public void onSuccess(String pin, boolean authWithFinger, boolean success) {
        Log.e(TAG, "onSuccess: " );
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                {
                    SweetAlertDialog val = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    val.setCancelable(false);
                    val.setTitleText("Good job!")
                            .setContentText("You clicked the button!")
                            .show();
                }
            }
        }, 3000);
    }

    @Override
    public void onError(String msg) {
        Log.e(TAG, "onSuccess: " );

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult: " );
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == AuthenticateActivity.RESULT_BACK_PRESSED) {
                    Toast.makeText(MainActivity.this, "back pressed", Toast.LENGTH_LONG).show();
                }
                break;

            case 234:
                Toast.makeText(MainActivity.this, "Read Done", Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void seekLed(SeekBar seekBar, SeekBar seekBar_left, SeekBar seekBar_right) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                jnielc.seekstart();
                jnielc.ledseek(seek_red_left, progress);
                jnielc.ledseek(seek_red_right, progress);
                jnielc.seekstop();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_left.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                jnielc.seekstart();
                jnielc.ledseek(seek_blue_left, progress);
                jnielc.seekstop();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_right.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                jnielc.seekstart();
                jnielc.ledseek(seek_green_right, progress);
                jnielc.seekstop();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void loadTimeline(){

        List<DataItem> cours = getDataList();
        Collections.shuffle(cours);
        Collections.sort(cours);


        Map<Integer, ItemTimeline> val = new HashMap<>();
        int hour = Integer.parseInt(DateUtils.Companion.dateToString(TrueTimeRx.now(), "HH"));
        int minute = Integer.parseInt(DateUtils.Companion.dateToString(TrueTimeRx.now(), "mm"));

        Log.e(TAG, "loadTimeline: hour ==> "+hour );

        for (DataItem item: cours){
            if (item.getDate().equals(DateUtils.Companion.getDateNow())){

                String[] hourDeb = item.getHeureDebut().split(":");
                String[] hourFin = item.getHeureFin().split(":");
                int hourStart = Integer.parseInt(hourDeb[0]);
                int hourEnd = Integer.parseInt(hourFin[0]); //!= 0 ? Integer.parseInt(hourFin[0]) + 1 : Integer.parseInt(hourFin[0]);


                int startHour = hourStart;
                if (Integer.parseInt(hourDeb[1]) != 0){
                    ItemTimeline itemLine = new ItemTimeline(-1, -1, startHour, -1,2,"#fd1717",3);
                    val.put(startHour, itemLine);
                    startHour++;
                }

                while (startHour < Integer.parseInt(hourFin[0])){
                    ItemTimeline itemLine = new ItemTimeline(-1, -1, startHour, -1,0,"#fd1717",3);
                    val.put(startHour, itemLine);
                    startHour++;
                }

                if (Integer.parseInt(hourFin[1]) != 0){
                    ItemTimeline itemLine = new ItemTimeline(-1, -1, Integer.parseInt(hourFin[0]), -1,0,"#fd1717",1);
                    val.put(Integer.parseInt(hourFin[0]), itemLine);
                }

            }
        }

        int startHour = timelineView.getStartNumber();

        while (startHour < hour){
            ItemTimeline itemLine = new ItemTimeline(-1, -1, startHour, -1,0,"#e4e4e4",3);
            val.put(startHour, itemLine);
            startHour++;
        }
   /*     ItemTimeline itemLine = new ItemTimeline(-1, -1, startHour, -1,0,"#e4e4e4",3);

        if (minute >= 45){
            itemLine.setEndBlock(2);
            val.put(startHour, itemLine);
        }else if (minute >= 30){
            itemLine.setEndBlock(1);
            val.put(startHour, itemLine);
        }else if (minute >= 15){
            itemLine.setEndBlock(0);
            val.put(startHour, itemLine);
        }*/

        timelineView.refreshTimeline(val);
    }


    @Override
    protected void onDestroy() {
        if (contactList != null)
            contactList.removeInstance();

        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public List<DataItem> getDataList(){
        String data = "{\n" +
                "\"size\": 6,\n" +
                "\"data\": [\n" +
                "{\n" +
                "\"objet\": \"Institutions africaines et environnement des affaires. / Semestre 3\",\n" +
                "\"professeur\": \"P21234 - Souleymane BA\",\n" +
                "\"date\": \"09/08/2021\",\n" +
                "\"heure_debut\": \"08:00\",\n" +
                "\"heure_fin\": \"10:00\",\n" +
                "\"libelle_salle\": \"ADH-DHAHIROU /2/A (SALLE MBA)\",\n" +
                "\"libelle_classe\": \"LP:BBA.2B.020\",\n" +
                "\"libelle_cours\": \"Institutions africaines et environnement des affaires.\",\n" +
                "\"volume_horaire_globale\": 20,\n" +
                "\"volume_horaire_planifie\": 9,\n" +
                "\"volume_horaire_restant\": 11,\n" +
                "\"type\": \"Planning cours\",\n" +
                "\"couleur\": \"#1385F7\"\n" +
                "},\n" +
                "{\n" +
                "\"objet\": \"Pratique d'Entreprise (stage) / Semestre 2\",\n" +
                "\"professeur\": \"P20005 - Magatte BA\",\n" +
                "\"date\": \"09/08/2021\",\n" +
                "\"heure_debut\": \"14:00\",\n" +
                "\"heure_fin\": \"15:30\",\n" +
                "\"libelle_salle\": \"ADH-DHAHIROU /2/A (SALLE MBA)\",\n" +
                "\"libelle_classe\": \"LP:BBA.1A.020\",\n" +
                "\"libelle_cours\": \"Pratique d'Entreprise (stage)\",\n" +
                "\"volume_horaire_globale\": 20,\n" +
                "\"volume_horaire_planifie\": 13.5,\n" +
                "\"volume_horaire_restant\": 6.5,\n" +
                "\"type\": \"Planning cours\",\n" +
                "\"couleur\": \"#1385F7\"\n" +
                "},\n" +
                "{\n" +
                "\"objet\": \"Stratégie marketing / Semestre 4\",\n" +
                "\"professeur\": \"P20063 - Mame Salla Dior DIENG\",\n" +
                "\"date\": \"09/08/2021\",\n" +
                "\"heure_debut\": \"18:00\",\n" +
                "\"heure_fin\": \"19:30\",\n" +
                "\"libelle_salle\": \"ADH-DHAHIROU /2/A (SALLE MBA)\",\n" +
                "\"libelle_classe\": \"LP:BBA.2B.020\",\n" +
                "\"libelle_cours\": \"Stratégie marketing\",\n" +
                "\"volume_horaire_globale\": 30,\n" +
                "\"volume_horaire_planifie\": 13,\n" +
                "\"volume_horaire_restant\": 17,\n" +
                "\"type\": \"Planning cours\",\n" +
                "\"couleur\": \"#1385F7\"\n" +
                "},\n" +
                "{\n" +
                "\"objet\": \"Algorithmique / Semestre 4\",\n" +
                "\"professeur\": \"P20294 - Fodé Camara\",\n" +
                "\"date\": \"06/08/2021\",\n" +
                "\"heure_debut\": \"08:00\",\n" +
                "\"heure_fin\": \"10:00\",\n" +
                "\"libelle_salle\": \"ADH-DHAHIROU /2/A (SALLE MBA)\",\n" +
                "\"libelle_classe\": \"BIG-2(lmd)-2020 (A)\",\n" +
                "\"libelle_cours\": \"Algorithmique\",\n" +
                "\"volume_horaire_globale\": 30,\n" +
                "\"volume_horaire_planifie\": 7,\n" +
                "\"volume_horaire_restant\": 23,\n" +
                "\"type\": \"Planning cours\",\n" +
                "\"couleur\": \"#1385F7\"\n" +
                "},\n" +
                "{\n" +
                "\"objet\": \"SenseAcademy / Semestre 3\",\n" +
                "\"professeur\": \"P21622 - Abdoulaye Diarra\",\n" +
                "\"date\": \"06/08/2021\",\n" +
                "\"heure_debut\": \"14:00\",\n" +
                "\"heure_fin\": \"16:30\",\n" +
                "\"libelle_salle\": \"ADH-DHAHIROU /2/A (SALLE MBA)\",\n" +
                "\"libelle_classe\": \"LP:BBA.2B.020\",\n" +
                "\"libelle_cours\": \"SenseAcademy\",\n" +
                "\"volume_horaire_globale\": 20,\n" +
                "\"volume_horaire_planifie\": 2.5,\n" +
                "\"volume_horaire_restant\": 17.5,\n" +
                "\"type\": \"Planning cours\",\n" +
                "\"couleur\": \"#1385F7\"\n" +
                "},\n" +
                "{\n" +
                "\"objet\": \"Micro Economie de l'entreprise () / Semestriel\",\n" +
                "\"professeur\": \"P21438 - Dr Souleymane Astou DIAGNE\",\n" +
                "\"date\": \"06/08/2021\",\n" +
                "\"heure_debut\": \"18:30\",\n" +
                "\"heure_fin\": \"20:00\",\n" +
                "\"libelle_salle\": \"ADH-DHAHIROU /2/A (SALLE MBA)\",\n" +
                "\"libelle_classe\": \"BBA PRO S1-2020\",\n" +
                "\"libelle_cours\": \"Micro Economie de l'entreprise ()\",\n" +
                "\"volume_horaire_globale\": 30,\n" +
                "\"volume_horaire_planifie\": 21.5,\n" +
                "\"volume_horaire_restant\": 8.5,\n" +
                "\"type\": \"Planning cours\",\n" +
                "\"couleur\": \"#1385F7\"\n" +
                "}\n" +
                "],\n" +
                "\"photo\": \"null\"\n" +
                "}";

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        DataItemList dataItemList = gson.fromJson(data, DataItemList.class);

        return dataItemList.getData();

    }
}