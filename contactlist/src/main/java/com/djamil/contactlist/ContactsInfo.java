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
    private Boolean checked = false;
    private Boolean duplicate = false;
    private int numberOfduplication = 0;

    public ContactsInfo() {
    }

    public ContactsInfo(String contactId, String displayName, List<String> phoneNumberList) {
        this.contactId = contactId;
        this.displayName = displayName;
        this.phoneNumberList = phoneNumberList;
    }

    public int getNumberOfduplication() {
        return numberOfduplication;
    }

    public void setNumberOfduplication(int numberOfduplication) {
        this.numberOfduplication = numberOfduplication;
    }
    public Boolean getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate) {
        this.duplicate = duplicate;
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "ContactsInfo{" +
                "indicatif='" + indicatif + '\'' +
                ", contactId='" + contactId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneNumberList=" + phoneNumberList +
                ", checked=" + checked +
                '}';
    }
}