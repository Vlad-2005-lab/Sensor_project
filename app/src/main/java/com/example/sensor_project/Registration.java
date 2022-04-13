package com.example.sensor_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registration extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registrate_new_user(View view) throws NoSuchAlgorithmException {
        String login = ((EditText) findViewById(R.id.login_r)).getText().toString();
        String mail = ((EditText) findViewById(R.id.mail_r)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_r)).getText().toString();
        String password_r = ((EditText) findViewById(R.id.password_repeat)).getText().toString();
        if (password.equals(password_r) && password.length() >= 8) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            password = bytesToHex(encodedhash);

            AsyncRequest a = new AsyncRequest();
            String ans = a.doInBackground(login, password, mail);
            if (ans.equals("invalid mail")) {
                ((EditText) findViewById(R.id.mail_r)).setText("");
                Toast.makeText(this, "Вы ввели неправильную почту", Toast.LENGTH_LONG).show();
            } else if (ans.equals("busy")) {
                ((EditText) findViewById(R.id.login_r)).setText("");
                Toast.makeText(this, "К сожалению, такой логин занят", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, Enter.class);
                startActivity(intent);
                overridePendingTransition(R.anim.down, R.anim.down1);
                this.finish();
            }
        } else if (password.equals(password_r) && password.length() < 8) {
            ((EditText) findViewById(R.id.password_r)).setText("");
            ((EditText) findViewById(R.id.password_repeat)).setText("");
            Toast.makeText(this, "Вы ввели слишком короткий пароль", Toast.LENGTH_LONG).show();
        } else {
            ((EditText) findViewById(R.id.password_r)).setText("");
            ((EditText) findViewById(R.id.password_repeat)).setText("");
            Toast.makeText(this, "Вы ввели разные пароли", Toast.LENGTH_LONG).show();
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.down,R.anim.down1);
    }

    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        String domen = "a339-178-72-68-143.ngrok.io";

        @Override
        protected String doInBackground(String... arg) {
            String url = "https://" + domen + "/create_user?l=" + arg[0] + "&p=" + arg[1] + "&m=" + arg[2];
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
