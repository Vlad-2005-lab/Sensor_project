package com.example.sensor_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Change1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_1);
    }

    public void next_change(View view) {
        String logo = ((EditText) findViewById(R.id.login_c)).getText().toString();

        AsyncRequest a = new AsyncRequest();
        String ans = a.doInBackground(logo);

        if (ans.equals("no")) {
            Toast.makeText(this, "Такого пользователя нет", Toast.LENGTH_SHORT).show();
            ((EditText) findViewById(R.id.login_c)).setText("");
        } else {
            // создание объекта Intent для запуска SecondActivity
            Intent intent = new Intent(this, Change2.class);
            intent.putExtra("id", ans);
            // запуск SecondActivity
            startActivity(intent);
            overridePendingTransition(0, 0);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        String domen = "a339-178-72-68-143.ngrok.io";

        @Override
        protected String doInBackground(String... arg) {
            String url = "https://" + domen + "/exist_user?l=" + arg[0];
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
