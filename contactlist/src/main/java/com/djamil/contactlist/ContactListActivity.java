package com.djamil.contactlist;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djamil.contactlist.interfaces.OnMultipleActive;
import com.djamil.fastscroll.FastScroller;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity implements OnMultipleActive {
    private static final String TAG = "ContactListActivity";

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ArrayList<ContactsInfo> contactsInfoList;
    RecyclerView recyclerView;
    FastScroller fastScroller;
    ContactAdapter adapter;
    ProgressDialog prd;
    TextView msg;
    private SearchView searchView;

    MenuItem action_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contacts");

        contactsInfoList = new ArrayList<>();
        showProgress();

        recyclerView = findViewById(R.id.contact_list);
        fastScroller = findViewById(R.id.fastscroll);
        msg = findViewById(R.id.msg);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fastScroller.setRecyclerView(recyclerView);

        adapter = new ContactAdapter(this, contactsInfoList);
        adapter.setOnMultipleActive(this);
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

        whiteNotificationBar(recyclerView);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        else if(item.getItemId() == R.id.action_validate){
            adapter.doneSelect();
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        action_menu = menu.findItem(R.id.action_validate);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

//        ImageView view = (ImageView) menu.findItem(R.id.action_validate).getActionView();
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.doneSelect(view);
//            }
//        });

        return true;
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

    @Override
    public void onBackPressed() {
        if(action_menu.isVisible()){
            action_menu.setVisible(false);
            adapter.cleanSelect();
            return;
        }
        else if (!searchView.isIconified()) { // close search view on back button pressed
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void isActive(Boolean isActive) {
        action_menu.setVisible(true);
    }

}