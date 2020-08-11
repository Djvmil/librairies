package com.djamil.contactlist;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djamil.fastscroll.FastScroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity {
    private static final String TAG = "ContactListActivity";

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ArrayList<ContactsInfo> contactsInfoList;
    RecyclerView recyclerView;
    FastScroller fastScroller;
    ContactAdapter adapter;
    ProgressDialog prd;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        final ImageView img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contactsInfoList = new ArrayList<>();
        showProgress();


        recyclerView = findViewById(R.id.contact_list);
        fastScroller = findViewById(R.id.fastscroll);
        msg = findViewById(R.id.msg);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fastScroller.setRecyclerView(recyclerView);

        adapter = new ContactAdapter(this, contactsInfoList);
        recyclerView.setAdapter(adapter);
        fastScroller.setRecyclerView(recyclerView);
        try{
            requestContactPermission();
        }catch (Exception e){
            e.printStackTrace();
            if (prd != null )
                prd.dismiss();
            msg.setVisibility(View.VISIBLE);
            if (contactsInfoList.size() < 1){
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                    msg.setText("Vous avez désactivé une autorisation de contacts");
            }
        }
    }


    private void getContacts(){
        Map<Long, List<String>> phones = new HashMap<>();
        ContentResolver cr = getContentResolver();

// First build a mapping: contact-id > list of phones
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
        while (cur != null && cur.moveToNext()) {
            long contactId = cur.getLong(0);
            String phone = cur.getString(1);
            List<String> list;
            if (phones.containsKey(contactId)) {
                list = phones.get(contactId);
            } else {
                list = new ArrayList<>();
                phones.put(contactId, list);
            }

            if(list != null)
                list.add(phone);
        }

        if(cur != null)
            cur.close();

        // Next query for all contacts, and use the phones mapping
        cur = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME }, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (cur != null && cur.moveToNext()) {
            long id = cur.getLong(0);
            String name = cur.getString(1);
            List<String> contactPhones = phones.get(id);

            contactsInfoList.add(new ContactsInfo(String.valueOf(id), name, contactPhones));
            //addContact(id, name, contactPhones);
        }

        if (contactsInfoList.size() > 0)
            msg.setVisibility(View.GONE);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (prd != null )
                prd.dismiss();
        }
    }

    public void requestContactPermission() {
        Log.i(TAG, "requestContactPermission: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Accès aux contacts requis");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Veuillez autoriser l'accès aux contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                getContacts();
            }
        } else {
            getContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    Toast.makeText(this, "Vous avez désactivé une autorisation de contacts\n", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                prd = ProgressDialog.show(ContactListActivity.this,
                        "Chargement",
                        "Chargement...", true, false);
                prd.setCancelable(false);
            }
        });

    }


}