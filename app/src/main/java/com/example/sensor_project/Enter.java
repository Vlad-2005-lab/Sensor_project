package com.example.sensor_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Enter extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("sq", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
            this.finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void forget(View view){
        Intent intent = new Intent(this, Change.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void reg(View view){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
        overridePendingTransition(R.anim.top, R.anim.top1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void enter(View view) throws NoSuchAlgorithmException {
        EditText e1 = (EditText) findViewById(R.id.login);
        EditText e2 = (EditText) findViewById(R.id.password_r);
        String login = e1.getText().toString();
        String password = e2.getText().toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        password = bytesToHex(encodedhash);

        AsyncRequest a = new AsyncRequest();
//        System.out.println(password);
        String ans = a.doInBackground(login, password);;
        System.out.println(ans);
        if (ans.contains("not ok")){
            Toast.makeText(this, "Введены неверные данные", Toast.LENGTH_SHORT).show();
            e2.setText("");
        } else if (ans.contains("error")) {
            Toast.makeText(this, "Извините, сервер сейчас не доступен, попробуйте позже", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, MainMenu.class);
            ContentValues cv = new ContentValues();
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.query("sq", null, null, null, null, null, null);
            cv.put("yes", "1");
            cv.put("my_id", Integer.parseInt(ans));
            db.insert("sq", null, cv);
            cv.clear();
            startActivity(intent);
            this.finish();
        }
    }

    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table sq ("
                    + "id integer primary key autoincrement,"
                    + "yes text," + "my_id integer" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            String url = "https://watersensors.herokuapp.com" + "/check_user?l=" + arg[0] + "&p=" + arg[1];
            System.out.println(url);
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
                System.out.println("pizdez");
                System.out.println(e);
                return "error";
            }
        }
    }
}

