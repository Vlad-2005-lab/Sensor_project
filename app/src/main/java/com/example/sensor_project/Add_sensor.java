package com.example.sensor_project;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Add_sensor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sensor);
    }

    public void add_new_sensor_to_user(View view) {
        EditText id_sensor_edittext = (EditText) findViewById(R.id.sensor_id);
        EditText name_sensor_edittext = (EditText) findViewById(R.id.name_sensor);
        String id_sensor = id_sensor_edittext.getText().toString();
        String name_sensor = name_sensor_edittext.getText().toString();

        AsyncRequest a = new AsyncRequest();
        String ans = a.doInBackground("exist_sensor", id_sensor);

        if (ans.equals("not exist")) {
            id_sensor_edittext.setText("");
            Toast.makeText(getApplicationContext(), R.string.does_not_exist_sensor, Toast.LENGTH_SHORT).show();
        } else {
            Bundle arguments = getIntent().getExtras();
            String id = arguments.get("id").toString();

            AsyncRequest b = new AsyncRequest();
            ans = b.doInBackground("add_sensor_to_user", id, id_sensor, name_sensor);

            if (ans.equals("unauthorized")) {
                Toast.makeText(getApplicationContext(), R.string.nobody_knows, Toast.LENGTH_SHORT).show();
                this.deleteDatabase("myDB");
                Bundle bundle = null;

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View v = findViewById(R.id.textView6);
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
            } else {
                Intent intent = new Intent(this, MainMenu.class);
                intent.putExtra("id", id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }


    public void back(View view){
        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("id").toString();
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }


    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        String domen = "a339-178-72-68-143.ngrok.io";

        @Override
        protected String doInBackground(String... arg) {
            String url;
            if (arg[0].equals("exist_sensor")) {
                url = "https://" + domen + "/exist_sensor?i=" + arg[1];
            } else {
                url = "https://" + domen + "/add_sensor_to_user?i=" + arg[1] + "&is=" + arg[2] + "&n=" + arg[3];
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
