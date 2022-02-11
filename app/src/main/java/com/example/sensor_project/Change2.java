package com.example.sensor_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;

public class Change2 extends AppCompatActivity {
//    static String da_rightcode = "42069420";
    static long time = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle arguments = getIntent().getExtras();
//        String id = arguments.get("id").toString();
        Toast.makeText(getApplicationContext(), "Вам на почту отправили код", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_change_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void resend(View view){
        if (System.currentTimeMillis() - time >= 30000){
            time = System.currentTimeMillis();
            Bundle arguments = getIntent().getExtras();
            String id = arguments.get("id").toString();
            AsyncRequest a = new AsyncRequest();
            String ans = a.doInBackground("resend_mail", id);
            Toast.makeText(getApplicationContext(), "Вам на почту отправили новый код", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Новый код можно будет получить через " +  (30 - (System.currentTimeMillis() - time) / 1000), Toast.LENGTH_SHORT).show();
        }
    }

    public void check_code(View view) {
        Button code_button = (Button) findViewById(R.id.next);
        EditText da_code_edittext = (EditText) findViewById(R.id.code);
        String da_code = da_code_edittext.getText().toString();

        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("id").toString();

        AsyncRequest a = new AsyncRequest();
        String ans = a.doInBackground("check_mail", id, da_code);

        if (ans.equals("yes")) {
            Toast.makeText(getApplicationContext(), "Код правильный", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Попробуйте ещё раз", Toast.LENGTH_LONG).show();
            da_code_edittext.setText("");
        }
    }

    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            String url;
            if (arg[0].equals("check_mail")){
                url = "https://watersensors.herokuapp.com" + "/check_mail?i=" + arg[1] + "&c=" + arg[2];
            } else {
                url = "https://watersensors.herokuapp.com" + "/resend_mail?i=" + arg[1];
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