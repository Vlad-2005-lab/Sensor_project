package com.example.sensor_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
    Button code_button = (Button) findViewById(R.id.code_button);
    EditText da_code_edittext = (EditText) findViewById(R.id.edit_textCode);
    static String da_rightcode = "42069420";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void check_code(View view) {
        String da_code = da_code_edittext.getText().toString();
        if (da_code.equals(da_rightcode)) {
            Toast.makeText(getApplicationContext(), "Код правильный, а что дальше?", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Попробуй ещё раз", Toast.LENGTH_LONG).show();
        }
    }
}