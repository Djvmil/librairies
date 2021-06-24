package com.djamil.contactlist;

import android.app.Activity;
import android.content.Intent;

import com.djamil.contactlist.interfaces.OnClickContactListener;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class ContactList {
    private static ContactList instance;
    private static Activity activity;
    private static ContactListActivity contactListActivity;
    static Boolean multiContact = true;
    static int limit;
    static OnClickContactListener onClickContactListener;

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

    public void showContactList(Boolean multipleContact, int limitContact){
        multiContact = multipleContact;
        limit = limitContact;
        activity.startActivity(new Intent(activity, contactListActivity.getClass()));
    }

    public void setOnClickContactListener(OnClickContactListener onClickListener) {
        onClickContactListener = onClickListener;
    }

    public void removeInstance() {
        activity = null;
        instance = null;
    }

}
