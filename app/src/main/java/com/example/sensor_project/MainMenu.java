package com.example.sensor_project;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        Resources r = this.getResources();
        int size_1_dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1,
                r.getDisplayMetrics()
        );
        int size_5_dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                5,
                r.getDisplayMetrics()
        );
        int size_10_dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                r.getDisplayMetrics()
        );
        int size_40_dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                40,
                r.getDisplayMetrics()
        );
        int size_60_dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60,
                r.getDisplayMetrics()
        );


        try {
            ScrollView scrollView = (ScrollView) findViewById(R.id.lent);
            LinearLayout mainlayout = (LinearLayout) findViewById(R.id.ln);

            Bundle arguments = getIntent().getExtras();
            String id = arguments.get("id").toString();
            AsyncRequest a = new AsyncRequest();
            String ans = a.doInBackground("get_sensors", id);

            if (ans.equals("unauthorized")) {
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

            if (!ans.equals("")) {
                String[] arr = ans.split(";");
                for (String aaaa : arr) {
                    sensors.add(Integer.parseInt(aaaa));
                }
            }
            for (int id_sensor : sensors) {
                String name = a.doInBackground("get_sensor_name", Integer.toString(id_sensor));
                String data = a.doInBackground("get_data_sensor", Integer.toString(id_sensor));

                LinearLayout linLayout = new LinearLayout(getApplicationContext());
                linLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, size_60_dp);
                linLayoutParam.topMargin = size_10_dp;
                linLayout.setLayoutParams(linLayoutParam);
                linLayout.setBackgroundResource(R.drawable.gray_sensor);
                linLayout.setGravity(Gravity.CENTER_VERTICAL);
                linLayout.setWeightSum(1);
                linLayout.setId(id_sensor);

                View status = new View(getApplicationContext());
                LinearLayout.LayoutParams status_param = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
                status_param.leftMargin = size_10_dp;
                status.setLayoutParams(status_param);
                switch (data) {
                    case "gray":
                        status.setBackgroundResource(R.drawable.gray);
                        break;
                    case "red":
                        status.setBackgroundResource(R.drawable.red);
                        break;
                    case "yellow":
                        status.setBackgroundResource(R.drawable.yellow);
                        break;
                    case "green":
                        status.setBackgroundResource(R.drawable.green);
                        break;
                }

                TextView name_view = new TextView(getApplicationContext());
                LinearLayout.LayoutParams name_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, size_40_dp);
                name_view_params.leftMargin = size_10_dp;
                name_view.setLayoutParams(name_view_params);
                name_view.setText(name);
                name_view.setTextColor(Color.rgb(0, 0, 0));
                name_view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                name_view.setTextSize(18);

                LinearLayout layout_for_edit = new LinearLayout(getApplicationContext());
                layout_for_edit.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layout_for_edit_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layout_for_edit.setLayoutParams(layout_for_edit_param);
                layout_for_edit.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

                ImageButton edit = new ImageButton(getApplicationContext());
                LinearLayout.LayoutParams status_param1 = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
                status_param1.rightMargin = size_10_dp;
                edit.setLayoutParams(status_param1);
                edit.setImageResource(R.drawable.edit);
                edit.setBackgroundResource(R.drawable.gray);
                edit.setScaleType(ImageView.ScaleType.FIT_CENTER);
                edit.setPadding(0, size_5_dp / 5 * 3, 0, 0);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout block = (LinearLayout) findViewById(id_sensor);
                        String name = ((TextView) block.getChildAt(block.getChildCount() - 2)).getText().toString();
                        block.removeViewAt(block.getChildCount() - 2);
//                    block.removeViewAt(0);

                        EditText edit_view = new EditText(getApplicationContext());
                        LinearLayout.LayoutParams edit_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, size_40_dp);
                        edit_view_params.leftMargin = size_10_dp;
                        edit_view_params.rightMargin = size_10_dp * 16 - size_1_dp * 8;
                        edit_view.setLayoutParams(edit_view_params);
                        edit_view.setText(name);
                        edit_view.setTextColor(Color.rgb(0, 0, 0));
                        edit_view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                        edit_view.setBackgroundResource(R.drawable.add_style);
                        edit_view.setTextSize(18);
                        edit_view.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        block.addView(edit_view, 1);

                        LinearLayout buttons = (LinearLayout) block.getChildAt(block.getChildCount() - 1);
                        buttons.removeAllViews();

                        LinearLayout.LayoutParams buttons_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        buttons_params.leftMargin = -size_10_dp * 15;
                        buttons.setLayoutParams(buttons_params);
                        buttons.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

                        ImageButton ok = new ImageButton(getApplicationContext());
                        LinearLayout.LayoutParams ok_param1 = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
                        ok_param1.rightMargin = size_5_dp;
                        ok.setLayoutParams(ok_param1);
                        ok.setImageResource(R.drawable.ic_baseline_check_24);
                        ok.setBackgroundResource(R.drawable.green);
                        ok.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        ok.setPadding(size_5_dp, size_5_dp, size_5_dp, size_5_dp);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout block = (LinearLayout) findViewById(id_sensor);
                                AsyncRequest c = new AsyncRequest();
                                String name = ((EditText) block.getChildAt(1)).getText().toString();
                                String ans = c.doInBackground("change_name_sensor", Integer.toString(id_sensor), name);
                                if (ans.equals("dont_exist")) {
                                    Toast.makeText(getApplicationContext(), R.string.no_sensor, Toast.LENGTH_SHORT).show();
                                    mainlayout.removeView(block);
                                } else {
                                    block.removeViewAt(1);
                                    ((LinearLayout) block.getChildAt(block.getChildCount() - 1)).removeAllViews();

                                    TextView name_view = new TextView(getApplicationContext());
                                    LinearLayout.LayoutParams name_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, size_40_dp);
                                    name_view_params.leftMargin = size_10_dp;
                                    name_view.setLayoutParams(name_view_params);
                                    name_view.setText(name);
                                    name_view.setTextColor(Color.rgb(0, 0, 0));
                                    name_view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                                    name_view.setTextSize(18);

                                    block.addView(name_view, 1);
                                    ((LinearLayout) block.getChildAt(block.getChildCount() - 1)).addView(edit);
                                }
                            }
                        });

                        ImageButton cancel = new ImageButton(getApplicationContext());
                        LinearLayout.LayoutParams cancel_param = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
                        cancel_param.rightMargin = size_5_dp;
                        cancel.setLayoutParams(cancel_param);
                        cancel.setImageResource(R.drawable.ic_baseline_clear_24);
                        cancel.setBackgroundResource(R.drawable.red);
                        cancel.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        cancel.setPadding(size_5_dp, size_5_dp, size_5_dp, size_5_dp);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout block = (LinearLayout) findViewById(id_sensor);
                                String name = a.doInBackground("get_sensor_name", Integer.toString(id_sensor));
                                block.removeViewAt(1);
                                ((LinearLayout) block.getChildAt(block.getChildCount() - 1)).removeAllViews();

                                TextView name_view = new TextView(getApplicationContext());
                                LinearLayout.LayoutParams name_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, size_40_dp);
                                name_view_params.leftMargin = size_10_dp;
                                name_view.setLayoutParams(name_view_params);
                                name_view.setText(name);
                                name_view.setTextColor(Color.rgb(0, 0, 0));
                                name_view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                                name_view.setTextSize(18);

                                block.addView(name_view, 1);
                                ((LinearLayout) block.getChildAt(block.getChildCount() - 1)).addView(edit);
                            }
                        });

                        ImageButton delete = new ImageButton(getApplicationContext());
                        LinearLayout.LayoutParams delete_param = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
                        delete_param.rightMargin = size_10_dp;
                        delete.setLayoutParams(delete_param);
                        delete.setImageResource(R.drawable.ic_baseline_delete_24);
                        delete.setBackgroundResource(R.drawable.red);
                        delete.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        delete.setPadding(size_5_dp, size_5_dp, size_5_dp, size_5_dp);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(MainMenu.this);
                                builder4.setMessage(R.string.message_delete_sensor);
                                builder4.setCancelable(true);
                                builder4.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // Кнопка ОК
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String ans = a.doInBackground("delete_sensor", id, Integer.toString(id_sensor));
                                        if (ans.equals("doesnt_exist")){
                                            Toast.makeText(getApplicationContext(), R.string.no_sensor, Toast.LENGTH_SHORT).show();
                                        }
                                        mainlayout.removeView(block);
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                builder4.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { // Кнопка cansel
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss(); // Отпускает диалоговое окно
                                    }
                                });
                                AlertDialog dialog4 = builder4.create();
                                dialog4.show();
                            }
                        });

                        buttons.addView(ok);
                        buttons.addView(cancel);
                        buttons.addView(delete);
                    }
                });

                linLayout.addView(status);
                linLayout.addView(name_view);
                layout_for_edit.addView(edit);
                linLayout.addView(layout_for_edit);
                mainlayout.addView(linLayout);
            }

            
        } catch (Exception ex) {
            System.out.println(ex);
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

//    public void clik(View v) {
//        LinearLayout block = (LinearLayout) findViewById(id_sensor);
//        String name = ((TextView) block.getChildAt(block.getChildCount() - 2)).getText().toString();
//        block.removeViewAt(block.getChildCount() - 2);
////                    block.removeViewAt(0);
//
//        EditText edit_view = new EditText(getApplicationContext());
//        LinearLayout.LayoutParams edit_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, size_40_dp);
//        edit_view_params.leftMargin = size_10_dp;
//        edit_view_params.rightMargin = size_10_dp * 16 - size_1_dp * 8;
//        edit_view.setLayoutParams(edit_view_params);
//        edit_view.setText(name);
//        edit_view.setTextColor(Color.rgb(0, 0, 0));
//        edit_view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//        edit_view.setBackgroundResource(R.drawable.add_style);
//        edit_view.setTextSize(18);
//        edit_view.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//        block.addView(edit_view, 1);
//
//        LinearLayout buttons = (LinearLayout) block.getChildAt(block.getChildCount() - 1);
//        buttons.removeAllViews();
//
//        LinearLayout.LayoutParams buttons_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        buttons_params.leftMargin = -size_10_dp * 15;
//        buttons.setLayoutParams(buttons_params);
//        buttons.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//
//        ImageButton ok = new ImageButton(getApplicationContext());
//        LinearLayout.LayoutParams ok_param1 = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
//        ok_param1.rightMargin = size_5_dp;
//        ok.setLayoutParams(ok_param1);
//        ok.setImageResource(R.drawable.ic_baseline_check_24);
//        ok.setBackgroundResource(R.drawable.green);
//        ok.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        ok.setPadding(size_5_dp, size_5_dp, size_5_dp, size_5_dp);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout block = (LinearLayout) findViewById(id_sensor);
//                AsyncRequest c = new AsyncRequest();
//                String name = ((EditText) block.getChildAt(1)).getText().toString();
//                String ans = c.doInBackground("change_name_sensor", Integer.toString(id_sensor), name);
//                if (ans.equals("dont_exist")) {
//                    Toast.makeText(getApplicationContext(), R.string.no_sensor, Toast.LENGTH_SHORT).show();
//                    mainlayout.removeView(block);
//                } else {
//                    block.removeViewAt(1);
//                    ((LinearLayout) block.getChildAt(block.getChildCount() - 1)).removeAllViews();
//
//                    TextView name_view = new TextView(getApplicationContext());
//                    LinearLayout.LayoutParams name_view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, size_40_dp);
//                    name_view_params.leftMargin = size_10_dp;
//                    name_view.setLayoutParams(name_view_params);
//                    name_view.setText(name);
//                    name_view.setTextColor(Color.rgb(0, 0, 0));
//                    name_view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
//                    name_view.setTextSize(18);
//
//                    block.addView(name_view, 1);
//
//                    LinearLayout layout_for_edit = new LinearLayout(getApplicationContext());
//                    layout_for_edit.setOrientation(LinearLayout.HORIZONTAL);
//                    LinearLayout.LayoutParams layout_for_edit_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    layout_for_edit.setLayoutParams(layout_for_edit_param);
//                    layout_for_edit.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
//
//                    ImageButton edit = new ImageButton(getApplicationContext());
//                    LinearLayout.LayoutParams status_param1 = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
//                    status_param1.rightMargin = size_10_dp;
//                    edit.setLayoutParams(status_param1);
//                    edit.setImageResource(R.drawable.edit);
//                    edit.setBackgroundResource(R.drawable.gray);
//                    edit.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    edit.setPadding(0, size_5_dp / 5 * 3, 0, 0);
//                    edit.setOnClickListener(this);
//                }
//            }
//        });
//
//        ImageButton cancel = new ImageButton(getApplicationContext());
//        LinearLayout.LayoutParams cancel_param = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
//        cancel_param.rightMargin = size_5_dp;
//        cancel.setLayoutParams(cancel_param);
//        cancel.setImageResource(R.drawable.ic_baseline_clear_24);
//        cancel.setBackgroundResource(R.drawable.red);
//        cancel.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        cancel.setPadding(size_5_dp, size_5_dp, size_5_dp, size_5_dp);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        ImageButton delete = new ImageButton(getApplicationContext());
//        LinearLayout.LayoutParams delete_param = new LinearLayout.LayoutParams(size_40_dp, size_40_dp);
//        delete_param.rightMargin = size_10_dp;
//        delete.setLayoutParams(delete_param);
//        delete.setImageResource(R.drawable.ic_baseline_delete_24);
//        delete.setBackgroundResource(R.drawable.red);
//        delete.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        delete.setPadding(size_5_dp, size_5_dp, size_5_dp, size_5_dp);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        buttons.addView(ok);
//        buttons.addView(cancel);
//        buttons.addView(delete);
//    }



    public void add_sensors(View view) {
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
        if (arg[0].equals("get_sensors")) {
            url = "https://watersensors.herokuapp.com" + "/get_sensors?i=" + arg[1];
        } else if (arg[0].equals("get_sensor_name")) {
            url = "https://watersensors.herokuapp.com" + "/get_sensor_name?i=" + arg[1];
        } else if (arg[0].equals("change_name_sensor")) {
            url = "https://watersensors.herokuapp.com" + "/change_name_sensor?i=" + arg[1] + "&n=" + arg[2];
        } else if (arg[0].equals("delete_sensor")) {
            url = "https://watersensors.herokuapp.com" + "/delete_sensor?i=" + arg[1] + "&s=" + arg[2];
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
