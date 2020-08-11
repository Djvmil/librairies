package com.djamil.contactlist;

import android.app.Activity;
import android.content.Intent;

import com.djamil.contactlist.interfaces.OnClickCantactListener;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class ContactList {
    private static ContactList instance;
    private static Activity activity;
    private static ContactListActivity contactListActivity;
    static OnClickCantactListener onClickCantactListener;

    private ContactList() {

    }

    public static ContactList getInstance(Activity act) {
        activity = act;
        contactListActivity = new ContactListActivity();
        if (instance == null)
            instance = new ContactList();

        return instance;
    }

    public void showContactList(){
        activity.startActivity(new Intent(activity, contactListActivity.getClass()));
    }

    public void setOnClickCantactListener(OnClickCantactListener onClickListener) {
        onClickCantactListener = onClickListener;
    }

    public void removeInstance() {
        activity.finish();
        activity = null;
        instance = null;
    }

}
