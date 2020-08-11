package com.djamil.contactlist;

import java.util.List;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class ContactsInfo {
    private String indicatif;
    private String contactId;
    private String displayName;
    private String phoneNumber;
    private List<String> phoneNumberList;

    public ContactsInfo() {
    }

    public ContactsInfo(String contactId, String displayName, List<String> phoneNumberList) {
        this.contactId = contactId;
        this.displayName = displayName;
        this.phoneNumberList = phoneNumberList;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIndicatif() {
        return indicatif;
    }

    public void setIndicatif(String indicatif) {
        this.indicatif = indicatif;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }
}
