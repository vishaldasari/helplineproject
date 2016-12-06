package org.vishal.helplineapp.model;

import java.io.File;

/**
 * Created by Vishal on 11/17/2016.
 */

public class Issue {

    private String m_name;
    private String m_reporter;
    private String m_description;
    private String m_number;
    private String m_date;
    private String m_phoneNumber;
    private File m_callRecording;

    public Issue() {}

    public Issue (String name,String number, String reporter , String description, String date, String phoneNumber  ) {
        setName(name);
        setNumber(number);
        setReporter(reporter);
        setDescription(description);
        setDate(date);
        setPhoneNumber(phoneNumber);
        setCallRecording(null);
    }
    public Issue (String name,String number, String reporter , String description, String date, String phoneNumber, File callRecording ) {
        setName(name);
        setNumber(number);
        setReporter(reporter);
        setDescription(description);
        setDate(date);
        setPhoneNumber(phoneNumber);
        setCallRecording(callRecording);
    }





    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public String getReporter() {
        return m_reporter;
    }

    public void setReporter(String m_reporter) {
        this.m_reporter = m_reporter;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String m_description) {
        this.m_description = m_description;
    }

    public String getNumber() {
        return m_number;
    }

    public void setNumber(String m_number) {
        this.m_number = m_number;
    }

    public String getDate() {
        return m_date;
    }

    public void setDate(String date) {
        this.m_date = date;
    }

    public String getPhoneNumber() {
        return m_phoneNumber;
    }

    public void setPhoneNumber(String m_phoneNumber) {
        this.m_phoneNumber = m_phoneNumber;
    }

    public File getCallRecording() {
        return m_callRecording;
    }

    public void setCallRecording(File m_callRecording) {
        this.m_callRecording = m_callRecording;
    }
}
