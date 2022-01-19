package com.djamil.contactlist

import android.app.Activity
import android.content.Intent
import com.djamil.contactlist.ContactList
import com.djamil.contactlist.interfaces.OnClickContactListener
import com.djamil.contactlist.ContactListActivity

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
class ContactList private constructor() {
    fun showContactList() {
        activity!!.startActivity(Intent(activity, contactListActivity!!.javaClass))
    }

    fun showContactList(multipleContact: Boolean = false, limitContact: Int = -1, msgLimitContact: String = "") {
        multiContact = multipleContact
        limit = limitContact
        msgLimit = msgLimitContact
        activity!!.startActivity(Intent(activity, contactListActivity!!.javaClass))
    }

    fun setOnClickContactListener(onClickListener: OnClickContactListener?) {
        onClickContactListener = onClickListener
    }

    fun removeInstance() {
        activity = null
        instance = null
        contactListActivity?.finish()
        contactListActivity = null
    }

    companion object {
        private var activity: Activity? = null
        private var contactListActivity: ContactListActivity? = null
        private var instance: ContactList? = null
        var multiContact = false
        var limit = -1
        var msgLimit = ""
        var onClickContactListener: OnClickContactListener? = null

        @JvmStatic
        fun getInstance(act: Activity?): ContactList {
            activity = act
            contactListActivity = ContactListActivity()
            if (instance == null) instance = ContactList()
            return instance!!
        }
    }
}