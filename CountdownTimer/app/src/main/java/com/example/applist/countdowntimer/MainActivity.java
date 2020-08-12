package com.example.applist.countdowntimer;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  ArrayList<Datum> data=new ArrayList<Datum>();

    private ArrayList<Datum> read(Context context, String filename){
        ArrayList<Datum> arrayList=new ArrayList<Datum>();
        try {
            FileInputStream fileInputStream=context.openFileInput(filename);
            InputStreamReader inInputStreamReader=new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader=new BufferedReader(inInputStreamReader);
            String tmp;

            DateFormat dateFormat=new SimpleDateFormat(getString(R.string.pattern));

            while((tmp=bufferedReader.readLine())!=null){
                Date tmpp=dateFormat.parse(tmp.split(",")[0]);
                Datum datum1 =new Datum(","+tmp.split(",")[1],tmpp);
                arrayList.add(datum1);

            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return arrayList;
    }
    private boolean create(Context context,String filename,ArrayList<Datum> arrayList){
        Date now=new Date();

        int j=0;
        while(j<arrayList.size()){
            if(arrayList.get(j).getDate().getTime()<now.getTime()){
                arrayList.remove(j);
                j--;
            }

            j++;
        }

        ArrayList<Datum> tempDatum;
        try{
            tempDatum=read(context,getString(R.string.filename));
        }catch (Exception e){
            e.printStackTrace();
            tempDatum=new ArrayList<Datum>();
        }
        j=0;
        while(j<tempDatum.size()){
            if(tempDatum.get(j).getDate().getTime()<now.getTime()){
                tempDatum.remove(j);
                j--;
            }

            j++;
        }
        arrayList.addAll(tempDatum);



        try {

            PrintWriter printWriter=new PrintWriter(context.getFilesDir().getAbsolutePath()+"/"+filename);


            if(arrayList.size()>0){
                for(int i=0;i<arrayList.size();i++){
                    printWriter.write(arrayList.get(i).toString());

                }

            }

            printWriter.close();
            arrayList.clear();
            tempDatum.clear();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFilePresent(Context context,String filename){
        String path=context.getFilesDir().getAbsolutePath()+"/"+filename;
        File file=new File(path);
        return file.exists();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/4531396/get-value-of-a-edit-text-field
        final EditText editText=(EditText) findViewById(R.id.edittext);
        Button save =(Button) findViewById(R.id.save_button);
        final Button startService= (Button) findViewById(R.id.start_service);
        final Button stopService= (Button) findViewById(R.id.stop_service);
        final Intent serviceIntent= new Intent(this,BgService.class);
        final TextView textView=(TextView) findViewById(R.id.textView);
        textView.setText("1- dd/MM/yyyy h:mm, information DAY/MONTH/YEAR HOUR:MINUTE this is the format includes / : this characters " +
                "and after need comma , to seperate data\n" +
                "example: 14/6/2020 14:33, Datum" );

        //https://stackoverflow.com/questions/35220895/android-get-time-from-edittext-inputtype-time
        //https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DateFormat dateFormat=new SimpleDateFormat(getString(R.string.pattern));
                    Date date;

                    String dob_var=(editText.getText().toString().split(",")[0]);
                    date=dateFormat.parse(dob_var);
                    String information=editText.getText().toString().split(",")[1];
                    Date date2=new Date(); //now Date
                    Log.d("date2", date2.toString());
                    Log.d("data",date.toString());
                    Datum datum=new Datum(","+information,date);
                    data.add(datum);
                    if(create(getBaseContext(),getString(R.string.filename),data)){
                        data.clear();
                    }else{
                        Log.d("Error","error");

                    }
                }catch (java.text.ParseException e){
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"Error false data format ",Toast.LENGTH_SHORT).show();
                }

            }
        });

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startService(serviceIntent);
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(serviceIntent);
            }
        });

    }
}

