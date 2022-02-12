package com.example.sensor_project;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        Resources r = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );

        try {
        ScrollView scrollView = (ScrollView) findViewById(R.id.lent);
        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.ln);

        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("id").toString();
        AsyncRequest a = new AsyncRequest();
        String ans = a.doInBackground("get_sensors", id);

        if (ans.equals("unauthorized")){
            Toast.makeText(getApplicationContext(), R.string.does_not_exist_sensor, Toast.LENGTH_SHORT).show();
            this.deleteDatabase("myDB");
            Bundle bundle = null;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                View v = findViewById(R.id.textView5);
                if (v != null) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, v, getString(R.string.transit_logo));
                    bundle = options.toBundle();
                }
            }

            Intent intent = new Intent(this, Enter.class);
            if (bundle == null) {
                startActivity(intent);
            } else {
                startActivity(intent, bundle);
            }
            this.finish();
        }

        ArrayList<Integer> sensors = new ArrayList<Integer>();

        if (!ans.equals("")){
            String[] arr = ans.split(";");
            for (String aaaa: arr){
                sensors.add(Integer.parseInt(aaaa));
            }
        }
//
        for (int id_sensor: sensors){
            // добавление сенсоров
            System.out.println(id_sensor);
        }
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(), R.string.nobody_knows, Toast.LENGTH_SHORT).show();
            this.deleteDatabase("myDB");
            Bundle bundle = null;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                View v = findViewById(R.id.textView5);
                if (v != null) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, v, getString(R.string.transit_logo));
                    bundle = options.toBundle();
                }
            }

            Intent intent = new Intent(this, Enter.class);
            if (bundle == null) {
                startActivity(intent);
            } else {
                startActivity(intent, bundle);
            }
            this.finish();
        }
    }

    public void add_sensors(View view){
        Intent intent = new Intent(this, Add_sensor.class);
        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("id").toString();
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.mainmenu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.change:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainMenu.this);
//        builder.setTitle("tilte");
                                builder1.setMessage(R.string.message_change_password);
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // Кнопка ОК
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "Ты меняешь пароль",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                builder1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { // Кнопка cansel
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "Ты отказалася",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();
                                return true;
                            case R.id.change_m:
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainMenu.this);
//        builder.setTitle("tilte");
                                builder2.setMessage(R.string.message_change_mail);
                                builder2.setCancelable(true);
                                builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // Кнопка ОК
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "Ты меняешь почту",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { // Кнопка cansel
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "Ты отказался",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                AlertDialog dialog2 = builder2.create();
                                dialog2.show();
                                return true;
                            case R.id.exit:
                                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainMenu.this);
//        builder.setTitle("tilte");
                                builder3.setMessage(R.string.message_exit);
                                builder3.setCancelable(true);
                                builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // Кнопка ОК
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainMenu.this.deleteDatabase("myDB");
//
                                        Bundle bundle = null;

                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                            View v = findViewById(R.id.textView5);
                                            if (v != null) {
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainMenu.this, v, getString(R.string.transit_logo));
                                                bundle = options.toBundle();
                                            }
                                        }

                                        Intent intent = new Intent(MainMenu.this, Enter.class);
                                        if (bundle == null) {
                                            startActivity(intent);
                                        } else {
                                            startActivity(intent, bundle);
                                        }
                                        MainMenu.this.finish();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { // Кнопка cansel
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(getApplicationContext(),
//                                                "Ты остался",
//                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                AlertDialog dialog3 = builder3.create();
                                dialog3.show();
                                return true;
                            case R.id.exit_delete:
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(MainMenu.this);
//        builder.setTitle("tilte");
                                builder4.setMessage(R.string.message_exit_delete);
                                builder4.setCancelable(true);
                                builder4.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // Кнопка ОК
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "Ты вышел и удалил аккаунт",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                builder4.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { // Кнопка cansel
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "Ты остался",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                AlertDialog dialog4 = builder4.create();
                                dialog4.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(getApplicationContext(), "onDismiss",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        popupMenu.show();
    }


    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            String url;
            if (arg[0].equals("get_sensors")){
                url = "https://watersensors.herokuapp.com" + "/get_sensors?i=" + arg[1];
            } else {
                url = "https://watersensors.herokuapp.com" + "/get_data_sensor?i=" + arg[1];
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
