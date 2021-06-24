package com.djamil.contactlist.interfaces;

import android.view.View;

import com.djamil.contactlist.ContactsInfo;

import java.util.ArrayList;

/**
 * Created by Djvmil_ on 6/9/20
 */

public interface OnClickContactListener {
    void onClickContact(View v, ContactsInfo contactsInfo);
    void onSelectClickContact(ArrayList<ContactsInfo> contactsInfo);
}
