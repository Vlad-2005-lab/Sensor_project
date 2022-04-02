package com.example.sensor_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

public class Change3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save_new_password(View view) throws NoSuchAlgorithmException {
        EditText edit_password = (EditText) findViewById(R.id.new_password);
        EditText edit_password_repeat = (EditText) findViewById(R.id.new_password_repeat);
        String password = edit_password.getText().toString();
        String password_repeat = edit_password_repeat.getText().toString();

        Bundle arguments = getIntent().getExtras();
        String id = arguments.get("id").toString();

        if (password.equals(password_repeat) && password.length() >= 8) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            password = bytesToHex(encodedhash);

            AsyncRequest a = new AsyncRequest();
            String ans = a.doInBackground(id, password);
            Intent intent = new Intent(this, Enter.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            this.finish();
        } else if (password.equals(password_repeat) && password.length() < 8) {
            edit_password.setText("");
            edit_password_repeat.setText("");
            Toast.makeText(this, "Вы ввели слишком короткий пароль", Toast.LENGTH_LONG).show();
        } else {
            edit_password.setText("");
            edit_password_repeat.setText("");
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

    static class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            String url = "https://" + "350e-178-72-70-172.ngrok.io" + "/change_user_password?i=" + arg[0] + "&p=" + arg[1];
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
