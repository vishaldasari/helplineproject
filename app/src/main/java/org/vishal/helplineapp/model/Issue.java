package org.vishal.helplineapp.model;

/**
 * Created by Vishal on 11/17/2016.
 */

public class Issue {

    private String m_name;
    private String m_reporter;
    private String m_description;
    private String m_number;

    public Issue() {}

    public Issue (String name,String number, String reporter , String description  ) {
        setName(name);
        setNumber(number);
        setReporter(reporter);
        setDescription(description);
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
}
