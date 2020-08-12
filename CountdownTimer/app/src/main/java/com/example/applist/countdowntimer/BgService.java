package com.example.applist.countdowntimer;

import android.app.AlarmManager;
import android.app.Service ;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//https://stackoverflow.com/questions/34573109/how-to-make-an-android-app-to-always-run-in-background
public class BgService extends Service  {

    private int count=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
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
    public boolean isFilePresent(Context context,String filename){
        String path=context.getFilesDir().getAbsolutePath()+"/"+filename;
        File file=new File(path);
        return file.exists();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ArrayList<Datum> temporary=new ArrayList<Datum>();

        if(isFilePresent(this,getString(R.string.filename))){
            temporary=read(this,getString(R.string.filename));

        }
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Date date=new Date();
        for(int i=0;i<temporary.size();i++){
            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+((temporary.get(i).getDate().getTime()-date.getTime())),pendingIntenter(this,temporary.get(i)));

        }
        if(count>214748300){
            count=0; // i dont want int bugs
        }


        return super.onStartCommand(intent, flags, startId);
    }
    private PendingIntent pendingIntenter(Context context,Datum datum){
        Receiver receiver=new Receiver();
        IntentFilter intIntentFilter=new IntentFilter("ALARM_ACTION");
        registerReceiver(receiver,intIntentFilter);
        Intent intent=new Intent("ALARM_ACTION");
        intent.putExtra("datum",datum);
        return PendingIntent.getBroadcast(context,count++,intent,0);//count cant be same https://stackoverflow.com/questions/41873634/setting-alarm-for-multiple-times-in-android
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
