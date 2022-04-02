package com.example.sensor_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Fon extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private long LAST_TIME = System.currentTimeMillis();
    private final Thread myThread = new MyThread();
    private ArrayList<Integer> list_id = new ArrayList<Integer>();
    private int user_id;
    private AsyncRequest a = new AsyncRequest();
    private int count = 100;
    NotificationManager nm;


    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        user_id = Integer.parseInt(intent.getStringExtra("id"));
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainMenu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Mysen")
                .setContentText("Я работаю в фоне, чтобы всегда вас предупредить")
                .setSmallIcon(R.mipmap.iconca)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        if (!myThread.isAlive())
            myThread.start();

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

            NotificationChannel serviceChannel1 = new NotificationChannel(
                    CHANNEL_ID + 1,
                    "HZ",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager1 = getSystemService(NotificationManager.class);
            manager1.createNotificationChannel(serviceChannel1);
        }
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            while (true){
                try{
                if (System.currentTimeMillis() - LAST_TIME > 5000){
                    String sensors = a.doInBackground("get_sensors", Integer.toString(user_id));
                    String[] arr = sensors.split(";");
                    list_id.clear();
                    for (String aaaa : arr) {
                        list_id.add(Integer.parseInt(aaaa));
                    }

                    for (int id_sensor: list_id){
                        String data = a.doInBackground("get_data_sensor", Integer.toString(id_sensor));
                        if (data.equals("red")){
                            System.out.println("+");
                            String name = a.doInBackground("get_sensor_name", Integer.toString(id_sensor));

                            Intent notificationIntent = new Intent(Fon.this, MainMenu.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(Fon.this,
                                    0, notificationIntent,
                                    PendingIntent.FLAG_CANCEL_CURRENT);

                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(Fon.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_baseline_delete_24)
                                            .setContentTitle("Mysen")
                                            .setContentText("Ваш датчик \"" + name + "\" имеет критический уровень")
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setContentIntent(contentIntent);

                            NotificationManagerCompat notificationManager =
                                    NotificationManagerCompat.from(Fon.this);
                            notificationManager.notify(2, builder.build());
                        }
                    }
                    LAST_TIME = System.currentTimeMillis();
                }} catch (Exception ex){
                    System.out.println(ex);
                    break;
                }
            }
        }
    }

    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            String url;
            if (arg[0].equals("get_sensors")) {
                url = "https://" + "350e-178-72-70-172.ngrok.io" + "/get_sensors?i=" + arg[1];
            } else if (arg[0].equals("get_sensor_name")) {
                url = "https://" + "350e-178-72-70-172.ngrok.io" + "/get_sensor_name?i=" + arg[1];
            } else {
                url = "https://" + "350e-178-72-70-172.ngrok.io" + "/get_data_sensor?i=" + arg[1];
            }
            StringBuffer response;
            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            } catch (Exception e) {
//                System.out.println(e);
                return "error";
            }
        }
    }
}