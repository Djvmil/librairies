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
import com.djamil.contactlist.ContactsInfo;
import com.djamil.contactlist.interfaces.OnClickCantactListener;
import com.djamil.authenticate_utils.Authenticate;
import com.suntelecoms.authenticate.activity.AuthenticateActivity;
import com.suntelecoms.authenticate.pinlockview.OnAuthListener;


public class MainActivity extends AppCompatActivity implements OnAuthListener {
    private static final String TAG = "MainActivity";

    private ContactList contactList;
    private TextView contactResult;
    private Authenticate authenticate;

    private static final String FONT_TEXT = "font/ALEAWB.TTF";
    private static final String FONT_NUMBER = "font/BLKCHCRY.TTF";

    private static final int REQUEST_CODE = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = AuthenticateActivity.getIntent(MainActivity.this, false, null, null);
        //AuthenticateActivity.Companion.setGoneBtnBack(true);
        AuthenticateActivity.Companion.setIcon(R.drawable.logo_aicha);
        AuthenticateActivity.Companion.setOnAuthListener(MainActivity.this);
        AuthenticateActivity.Companion.setShuffle(true);
        AuthenticateActivity.Companion.setUseFingerPrint(true);
        startActivity(intent);


        contactResult = findViewById(R.id.contact_result);
        authenticate  = findViewById(R.id.dynamic_key);
        authenticate.setOnResultAuth(new OnResultAuth() {
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
                    authenticate.setMsgError("Bakhoullllll");
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            authenticate.useFingerPrintForAuth(true);
            Authenticate.Result result = authenticate.checkFingerPrint(this);
            Log.e(TAG, "onCreate: "+result.toString());
        }

        contactList = ContactList.getInstance(this);
        contactList.setOnClickCantactListener(new OnClickCantactListener() {
            @Override
            public void onClickCantact(View v, ContactsInfo contactsInfo) {
                Log.e(TAG, "onClickCantact: "+ contactsInfo.getDisplayName());
                contactResult.setText(contactsInfo.getDisplayName().concat("\n"+contactsInfo.getPhoneNumber()));
            }
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
        Button normal = (Button) findViewById(R.id.normal);
        Button setPin = (Button) findViewById(R.id.setPin);
        Button setFont = (Button) findViewById(R.id.setFont);
        Button setPinAndFont = (Button) findViewById(R.id.setPinAndFont);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // start the activity, It handles the setting and checking
                Intent intent = new Intent(MainActivity.this, AuthenticateActivity.class);
//                startActivity(intent);

                // for handling back press
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        setPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set pin instead of checking it
                Intent intent = AuthenticateActivity.getIntent(MainActivity.this, true, null, null);
                //AuthenticateActivity.Companion.setGoneBtnBack(true);
                AuthenticateActivity.Companion.setIcon(R.drawable.logo_aicha);
                AuthenticateActivity.Companion.setOnAuthListener(MainActivity.this);
                AuthenticateActivity.Companion.setShuffle(true);
                AuthenticateActivity.Companion.setUseFingerPrint(true);
                startActivity(intent);

            }
        });

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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == AuthenticateActivity.RESULT_BACK_PRESSED) {
                    Toast.makeText(MainActivity.this, "back pressed", Toast.LENGTH_LONG).show();
                }
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
    public void onSuccess(String pin, boolean success) {

    }

    @Override
    public void onError(String msg) {

    }
}
