package com.djamil.dynamicform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.djamil.authenticate_utils.interfaces.OnResultAuth;
import com.djamil.contactlist.ContactList;
import com.djamil.contactlist.ContactsInfo;
import com.djamil.contactlist.interfaces.OnClickCantactListener;
import com.djamil.authenticate_utils.Authenticate;
import com.mattprecious.swirl.SwirlView;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";

    private ContactList contactList;
    private TextView contactResult;
    private Authenticate authenticate;

     SwirlView swirlView;
     RadioGroup stateView;
     CompoundButton animateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swirlView   = findViewById(R.id.swirl);
        stateView   = findViewById(R.id.state);
        animateView = findViewById(R.id.animate);

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
        stateView.setOnCheckedChangeListener(this);
    }

    @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.off:
                swirlView.setState(SwirlView.State.OFF, animateView.isChecked());
                break;
            case R.id.on:
                swirlView.setState(SwirlView.State.ON, animateView.isChecked());
                break;
            case R.id.error:
                swirlView.setState(SwirlView.State.ERROR, animateView.isChecked());
                break;
            default:
                throw new IllegalArgumentException("Unexpected checkedId: " + checkedId);
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
}
