package com.djamil.dynamicform;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.djamil.authenticate_utils.interfaces.OnResultAuth;
import com.djamil.contactlist.ContactList;
import com.djamil.authenticate_utils.Authenticate;
import com.djamil.contactlist.ContactsInfo;
import com.djamil.contactlist.interfaces.OnClickCantactListener;
import com.suntelecoms.authenticate.activity.AuthenticateActivity;
import com.suntelecoms.authenticate.pinlockview.OnAuthListener;
//import com.suntelecoms.library_mifare.Activities.ReadAllSectors;
//import com.suntelecoms.library_mifare.Activities.WaitForReadCard;


public class MainActivity extends AppCompatActivity implements OnAuthListener {
    private static final String TAG = "MainActivity";

    private ContactList contactList;
    private TextView contactResult;
    private static Authenticate authenticate;

    private static final String FONT_TEXT = "font/ALEAWB.TTF";
    private static final String FONT_NUMBER = "font/BLKCHCRY.TTF";

    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Intent intent = AuthenticateActivity.getIntent(MainActivity.this, false, null, null);
        //AuthenticateActivity.Companion.setGoneBtnBack(true);
        AuthenticateActivity.Companion.setIcon(R.drawable.logo_aicha);
        AuthenticateActivity.Companion.setOnAuthListener(MainActivity.this);
        AuthenticateActivity.Companion.setShuffle(true);
        AuthenticateActivity.Companion.setCloseAfterAttempts(false);
        AuthenticateActivity.Companion.setUseFingerPrint(true);
       // startActivity(intent);

        contactResult = findViewById(R.id.contact_result);
        authenticate  = findViewById(R.id.dynamic_key);

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
            authenticate.useFingerPrintForAuth(false);
            Authenticate.Result result = authenticate.checkFingerPrint(this);
            Log.e(TAG, "onCreate: "+result.toString());
        }

        contactList = ContactList.getInstance(this);
        contactList.setOnClickCantactListener((v, contactsInfo) -> {
            Log.e(TAG, "onClickCantact: "+ contactsInfo.getDisplayName());
            contactResult.setText(contactsInfo.getDisplayName().concat("\n"+contactsInfo.getPhoneNumber()));
        });

        findViewById(R.id.dynamic_form).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FormulaireActivity.class));

            }
        });

        findViewById(R.id.keyboard_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    authenticate.useFingerPrintForAuth(false);
                }
            }
        });

        findViewById(R.id.contact_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactList.showContactList();
            }
        });
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


        setPinAndFont.setOnClickListener(v -> {
            Intent intent2 = AuthenticateActivity.getIntent(MainActivity.this, false, null, null);
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
            Intent intent1 = AuthenticateActivity.getIntent(MainActivity.this, true, null, null);
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
                Toast.makeText(MainActivity.this, "Read DOne", Toast.LENGTH_LONG).show();
                break;
        }
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

    @Override
    public void onSuccess(String pin, boolean authWithFinger, boolean success) {
        Log.e(TAG, "onSuccess: " );
    }

    @Override
    public void onError(String msg) {

    }
}
