package com.djamil.suntelecom.librairies

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import com.djamil.SweetAlert.SweetAlertDialog
import com.djamil.authenticate_utils.Authenticate
import com.djamil.authenticate_utils.interfaces.OnResultAuth
import com.djamil.contactlist.ContactList
import com.djamil.contactlist.ContactList.Companion.getInstance
import com.djamil.contactlist.ContactsInfo
import com.djamil.contactlist.interfaces.OnClickContactListener
import com.djamil.suntelecom.truetime.TrueTimeRx
import com.djamil.utils.DateUtils
import com.djamil.utils.DateUtils.Companion.dateToString
import com.djamil.utils.DateUtils.Companion.getDateNow
import com.djamil.utils.DateUtils.Companion.strToDate
import com.djamil.utils.DateUtils.Companion.yearsBetweenDates
import com.example.elcapi.jnielc
import com.google.gson.Gson
import com.suntelecoms.authenticate.activity.AuthenticateActivity
import com.suntelecoms.authenticate.activity.AuthenticateActivity.Companion.closeAfterAttempts
import com.suntelecoms.authenticate.activity.AuthenticateActivity.Companion.getIntent
import com.suntelecoms.authenticate.activity.AuthenticateActivity.Companion.icon
import com.suntelecoms.authenticate.activity.AuthenticateActivity.Companion.onAuthListener
import com.suntelecoms.authenticate.activity.AuthenticateActivity.Companion.shuffle
import com.suntelecoms.authenticate.activity.AuthenticateActivity.Companion.useFingerPrint
import com.suntelecoms.authenticate.pinlockview.OnAuthListener
import com.suntelecoms.timeline.TimelineView
import com.suntelecoms.timeline.models.ItemTimeline
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity2 : AppCompatActivity(), OnAuthListener {
    private val TAG = "MainActivity"

    private var contactList: ContactList? = null
    private var contactResult: TextView? = null
    private  var trueTime:TextView? = null
    private var authenticate: Authenticate? = null
    private val btnkeyboardview: EditText? = null

    private val FONT_TEXT = "font/ALEAWB.TTF"
    private val FONT_NUMBER = "font/BLKCHCRY.TTF"

    private val REQUEST_CODE = 123
    private val seek_red_right = 0xa1
    private val seek_green_right = 0xa2
    private val seek_blue_right = 0xa3
    private val seek_green_blue_right = 0xa4
    private val seek_red_blue_right = 0xa5
    private val seek_red_green_right = 0xa6
    private val seek_all_right = 0xa7

    private val seek_red_left = 0xb1
    private val seek_green_left = 0xb2
    private val seek_blue_left = 0xb3
    private val seek_green_blue_left = 0xb4
    private val seek_red_blue_left = 0xb5
    private val seek_red_green_left = 0xb6
    private val seek_all_left = 0xb7
    var timelineView: TimelineView? = null


    fun getDayStringOld(date: Date?, locale: Locale?, patern: String?): String? {
        val formatter: DateFormat = SimpleDateFormat(patern, locale)
        return formatter.format(date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//        val load = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
//        load.show()
//        ContactList.getInstance(this).showContactList()
        timelineView = findViewById(R.id.timeline)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val seekBar_left = findViewById<SeekBar>(R.id.seekBar_left)
        val seekBar_right = findViewById<SeekBar>(R.id.seekBar_right)
        trueTime = findViewById(R.id.true_time)

        Log.e(TAG, "onCreate: DIffADate => " + yearsBetweenDates(
            strToDate("10/10/2013", "dd/MM/yyyy"), strToDate("10/10/2019", "dd/MM/yyyy")
            )
        )

        trueTime?.text = dateToString(TrueTimeRx.now(), DateUtils.FORMAT_FRENCH_MEDIUM)
        seekLed(seekBar, seekBar_left, seekBar_right)

        val text = "980828454//00110|Mounirou|TANDIANG|Responsable Support|SUPPORT TECHNIQUE//"

        val split = text.split("//").toTypedArray()

        Log.e(TAG, "onCreate => 1: " + split.size)
        Log.e(TAG, "onCreate => 2: " + split.contentToString())

        val split1 = split[1].split("\\|").toTypedArray()
        //String [] split1 = split[1].split("\\|");
        //String [] split1 = split[1].split("\\|");
        Log.e(TAG, "onCreate => 3: " + split.contentToString())
        Log.e(TAG, "onCreate => 4: " + split1.contentToString())
        Log.e(TAG, "onCreate => 5: " + split1[0])
        Log.e(TAG, "onCreate => 6: " + text.split("//").toTypedArray()[1].split("\\|").toTypedArray()[0])

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
        contactResult = findViewById(R.id.contact_result)
        authenticate = findViewById(R.id.dynamic_key)
        val editText = findViewById<EditText>(R.id.editText)
        authenticate?.setEditText(editText)
        authenticate?.setDoneBtn(findViewById(R.id.valider))
        //btnkeyboardview  = findViewById(R.id.keyboard_view1);

        //btnkeyboardview  = findViewById(R.id.keyboard_view1);
        authenticate?.setSecret("0000", false)
            ?.setOnResultAuth(object : OnResultAuth {
                override fun onAuthError(errorCode: Int) {}
                override fun onAuthFailed(typeAuth: Int) {}
                override fun onAuthSucceeded(pwd: String?, pwdMd5: String?) {}
                override fun onAttempts(nbAttempts: Int) {
                    Log.e(TAG, "onAttempts: $nbAttempts")
                }

                override fun onDoneClicked(pwd: String?, pwdMd5: String?, isSuccess: Boolean) {
                    if (!isSuccess) {
                        authenticate?.setMsgError("Unauthorized")
                    }
                }
            })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            authenticate?.useFingerPrintForAuth(true)
            val result = Authenticate.checkFingerPrint(this)
            Log.e(TAG, "onCreate: $result")
        }

        contactList = getInstance(this)
        contactList?.setOnClickContactListener(object : OnClickContactListener {
            override fun onClickContact(v: View, contactsInfo: ContactsInfo) {
                Log.e(TAG,
                    "onClickCantact: " + contactsInfo.displayName)
                contactResult?.text = """
                            ${contactsInfo.displayName}
                            ${contactsInfo.phoneNumber}
                            """.trimIndent()
            }

            override fun onSelectClickContact(contactsInfo: ArrayList<ContactsInfo>) {
                Log.e(TAG, "onClickCantact: $contactsInfo")
            }
        })

        val btn = findViewById<Button>(R.id.dynamic_form)
        //    btn.setText(getDayStringOld(new Date(), Locale.FRENCH, "EEE | dd | MMMM | yyyy"));
        //    btn.setText(getDayStringOld(new Date(), Locale.FRENCH, "EEE | dd | MMMM | yyyy"));
        findViewById<View>(R.id.dynamic_form).setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    this@MainActivity2,
                    FormulaireActivity::class.java
                )
            )
        }

        findViewById<View>(R.id.keyboard_view).setOnClickListener { view: View? ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                authenticate?.useFingerPrintForAuth(false)
            }
        }
        findViewById<View>(R.id.contact_list).setOnClickListener { view: View? ->
            contactList?.showContactList(true, 2, "")
        }
        val normal = findViewById<Button>(R.id.normal)
        val setPin = findViewById<Button>(R.id.setPin)
        val setFont = findViewById<Button>(R.id.setFont)
        val setPinAndFont = findViewById<Button>(R.id.setPinAndFont)

        normal.setOnClickListener { v: View? ->

            // start the activity, It handles the setting and checking
            val intent12 = Intent(this@MainActivity2, AuthenticateActivity::class.java)
//                startActivity(intent);

            // for handling back press
            startActivityForResult(intent12, REQUEST_CODE)
        }

        loadTimeline()

        setPinAndFont.setOnClickListener { v: View? ->
            val intent2 =
                getIntent(this@MainActivity2, false, null, null, 6)
            //AuthenticateActivity.Companion.setGoneBtnBack(true);
            icon = R.drawable.logo_aicha
            onAuthListener = this@MainActivity2
            shuffle = true
            closeAfterAttempts = true
            useFingerPrint = true
            startActivity(intent2)
        }

        setPin.setOnClickListener { v: View? ->

            // set pin instead of checking it
            val intent1 =
                getIntent(this@MainActivity2, true, null, null, 6)
            //AuthenticateActivity.Companion.setGoneBtnBack(true);
            icon = R.drawable.logo_aicha
            onAuthListener = this@MainActivity2
            shuffle = false
            closeAfterAttempts = true
            useFingerPrint = true
            startActivity(intent1)
        }

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
        val intent = getIntent(this@MainActivity2, false, null, null, 6)
        //AuthenticateActivity.Companion.setGoneBtnBack(true);
        //AuthenticateActivity.Companion.setGoneBtnBack(true);
        icon = R.drawable.logo_aicha
        onAuthListener = this@MainActivity2
        shuffle = true
        closeAfterAttempts = false
        useFingerPrint = true
        findViewById<View>(R.id.authenticate).setOnClickListener { view: View? ->
            startActivity(intent)
        }

//        TakePhoto.Companion.faceIdentity(this)
    }


    override fun onSuccess(pin: String?, authWithFinger: Boolean, success: Boolean) {
        Log.e(TAG, "onSuccess: ")
        Handler().postDelayed(object : Runnable {
            override fun run() {
                run {
                    val `val` =
                        SweetAlertDialog(this@MainActivity2, SweetAlertDialog.PROGRESS_TYPE)
                    `val`.setCancelable(false)
                    `val`.setTitleText("Good job!")
                        .setContentText("You clicked the button!")
                        .show()
                }
            }
        }, 3000)
    }

    override fun onError(msg: String?) {
        Log.e(TAG, "onSuccess: ")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivityResult: ")
        when (requestCode) {
            REQUEST_CODE -> if (resultCode == AuthenticateActivity.RESULT_BACK_PRESSED) {
                Toast.makeText(this@MainActivity2, "back pressed", Toast.LENGTH_LONG).show()
            }
            234 -> Toast.makeText(this@MainActivity2, "Read Done", Toast.LENGTH_LONG).show()
        }
    }


    private fun seekLed(seekBar: SeekBar, seekBar_left: SeekBar, seekBar_right: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                jnielc.seekstart()
                jnielc.ledseek(seek_red_left, progress)
                jnielc.ledseek(seek_red_right, progress)
                jnielc.seekstop()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        seekBar_left.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                jnielc.seekstart()
                jnielc.ledseek(seek_blue_left, progress)
                jnielc.seekstop()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        seekBar_right.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                jnielc.seekstart()
                jnielc.ledseek(seek_green_right, progress)
                jnielc.seekstop()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }


    fun loadTimeline() {
        val cours = getDataList()
        Collections.shuffle(cours)
        Collections.sort(cours)
        val valll: MutableMap<Int, ItemTimeline> = HashMap()
        val hour = dateToString(TrueTimeRx.now(), "HH").toInt()
        val minute = dateToString(TrueTimeRx.now(), "mm").toInt()
        Log.e(TAG, "loadTimeline: hour ==> $hour")
        for (item in cours) {
            if (item!!.date.equals(getDateNow())) {
                val hourDeb: List<String> = item.heureDebut.split(":")
                val hourFin: List<String> = item.heureFin.split(":")
                val hourStart = hourDeb[0].toInt()
                val hourEnd =
                    hourFin[0].toInt() //!= 0 ? Integer.parseInt(hourFin[0]) + 1 : Integer.parseInt(hourFin[0]);
                var startHour = hourStart
                if (hourDeb[1].toInt() != 0) {
                    val itemLine = ItemTimeline(-1, -1, startHour, -1, 2, "#fd1717", 3)
                    valll[startHour] = itemLine
                    startHour++
                }
                while (startHour < hourFin[0].toInt()) {
                    val itemLine = ItemTimeline(-1, -1, startHour, -1, 0, "#fd1717", 3)
                    valll[startHour] = itemLine
                    startHour++
                }
                if (hourFin[1].toInt() != 0) {
                    val itemLine = ItemTimeline(-1, -1, hourFin[0].toInt(), -1, 0, "#fd1717", 1)
                    valll[hourFin[0].toInt()] = itemLine
                }
            }
        }
        var startHour = timelineView!!.startNumber
        while (startHour < hour) {
            val itemLine = ItemTimeline(-1, -1, startHour, -1, 0, "#e4e4e4", 3)
            valll[startHour] = itemLine
            startHour++
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
        }*/timelineView!!.refreshTimeline(valll)
    }


    override fun onDestroy() {
        contactList?.removeInstance()
        super.onDestroy()
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    fun getDataList(): List<DataItem?> {
        val data = """
             {
             "size": 6,
             "data": [
             {
             "objet": "Institutions africaines et environnement des affaires. / Semestre 3",
             "professeur": "P21234 - Souleymane BA",
             "date": "09/08/2021",
             "heure_debut": "08:00",
             "heure_fin": "10:00",
             "libelle_salle": "ADH-DHAHIROU /2/A (SALLE MBA)",
             "libelle_classe": "LP:BBA.2B.020",
             "libelle_cours": "Institutions africaines et environnement des affaires.",
             "volume_horaire_globale": 20,
             "volume_horaire_planifie": 9,
             "volume_horaire_restant": 11,
             "type": "Planning cours",
             "couleur": "#1385F7"
             },
             {
             "objet": "Pratique d'Entreprise (stage) / Semestre 2",
             "professeur": "P20005 - Magatte BA",
             "date": "09/08/2021",
             "heure_debut": "14:00",
             "heure_fin": "15:30",
             "libelle_salle": "ADH-DHAHIROU /2/A (SALLE MBA)",
             "libelle_classe": "LP:BBA.1A.020",
             "libelle_cours": "Pratique d'Entreprise (stage)",
             "volume_horaire_globale": 20,
             "volume_horaire_planifie": 13.5,
             "volume_horaire_restant": 6.5,
             "type": "Planning cours",
             "couleur": "#1385F7"
             },
             {
             "objet": "Stratégie marketing / Semestre 4",
             "professeur": "P20063 - Mame Salla Dior DIENG",
             "date": "09/08/2021",
             "heure_debut": "18:00",
             "heure_fin": "19:30",
             "libelle_salle": "ADH-DHAHIROU /2/A (SALLE MBA)",
             "libelle_classe": "LP:BBA.2B.020",
             "libelle_cours": "Stratégie marketing",
             "volume_horaire_globale": 30,
             "volume_horaire_planifie": 13,
             "volume_horaire_restant": 17,
             "type": "Planning cours",
             "couleur": "#1385F7"
             },
             {
             "objet": "Algorithmique / Semestre 4",
             "professeur": "P20294 - Fodé Camara",
             "date": "06/08/2021",
             "heure_debut": "08:00",
             "heure_fin": "10:00",
             "libelle_salle": "ADH-DHAHIROU /2/A (SALLE MBA)",
             "libelle_classe": "BIG-2(lmd)-2020 (A)",
             "libelle_cours": "Algorithmique",
             "volume_horaire_globale": 30,
             "volume_horaire_planifie": 7,
             "volume_horaire_restant": 23,
             "type": "Planning cours",
             "couleur": "#1385F7"
             },
             {
             "objet": "SenseAcademy / Semestre 3",
             "professeur": "P21622 - Abdoulaye Diarra",
             "date": "06/08/2021",
             "heure_debut": "14:00",
             "heure_fin": "16:30",
             "libelle_salle": "ADH-DHAHIROU /2/A (SALLE MBA)",
             "libelle_classe": "LP:BBA.2B.020",
             "libelle_cours": "SenseAcademy",
             "volume_horaire_globale": 20,
             "volume_horaire_planifie": 2.5,
             "volume_horaire_restant": 17.5,
             "type": "Planning cours",
             "couleur": "#1385F7"
             },
             {
             "objet": "Micro Economie de l'entreprise () / Semestriel",
             "professeur": "P21438 - Dr Souleymane Astou DIAGNE",
             "date": "06/08/2021",
             "heure_debut": "18:30",
             "heure_fin": "20:00",
             "libelle_salle": "ADH-DHAHIROU /2/A (SALLE MBA)",
             "libelle_classe": "BBA PRO S1-2020",
             "libelle_cours": "Micro Economie de l'entreprise ()",
             "volume_horaire_globale": 30,
             "volume_horaire_planifie": 21.5,
             "volume_horaire_restant": 8.5,
             "type": "Planning cours",
             "couleur": "#1385F7"
             }
             ],
             "photo": "null"
             }
             """.trimIndent()
        val gson = Gson() // Or use new GsonBuilder().create();
        val dataItemList = gson.fromJson(data, DataItemList::class.java)
        return dataItemList.data
    }
}