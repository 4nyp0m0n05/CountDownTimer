package com.example.applist.countdowntimer;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
@SuppressWarnings("serial")
public class Datum implements Serializable{
    private String information;
    private Date date;
    private static String pattern="dd/MM/yyyy HH:mm";
    private DateFormat dateFormat;
    //https://stackoverflow.com/questions/5683728/convert-java-util-date-to-string
    public Datum(){

    }
    public Datum(String infor,Date dates){
        dateFormat=new SimpleDateFormat(pattern);
        information=infor;
        date=dates;
    }
    public Date getDate() {
        return date;
    }

    public String getInformation() {
        return information;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setInformation(String information) {
        this.information = information;
    }


    @Override
    public String toString() {

        return dateFormat.format(date)+" "+information.toString()+"\n";
    }
}
