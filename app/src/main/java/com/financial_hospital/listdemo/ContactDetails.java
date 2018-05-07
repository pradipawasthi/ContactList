package com.financial_hospital.listdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pradip on 22-06-2017.
 */

public class ContactDetails implements Parcelable {

    private String fname;
    private String lname;
    private String mobile;

    public ContactDetails(){

    }



    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ContactDetails(Parcel in) {
        String data[] = new String[3];

        in.readStringArray(data);
        this.fname = data[0];
        this.lname = data[1];
        this.mobile = data[2];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.fname, this.lname, this.mobile});
    }

    public static final Creator<ContactDetails> CREATOR = new Creator<ContactDetails>() {
        @Override
        public ContactDetails createFromParcel(Parcel in) {
            return new ContactDetails(in);
        }

        @Override
        public ContactDetails[] newArray(int size) {
            return new ContactDetails[size];
        }
    };

}
