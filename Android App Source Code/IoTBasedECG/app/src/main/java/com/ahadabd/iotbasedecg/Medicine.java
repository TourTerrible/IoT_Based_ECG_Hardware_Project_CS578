package com.ahadabd.iotbasedecg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Medicine extends AppCompatActivity {

    TextView daysleft,datetv;
    EditText daysupdate;
    Button btn_update;
    private Handler handler;
    private Runnable runnable;
    DatePickerDialog picker;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        daysleft = findViewById(R.id.daysleft);
        daysupdate = findViewById(R.id.et_daysupdate);
        btn_update = findViewById(R.id.btnupdate);
        datetv=findViewById(R.id.datetv);
        sharedpreferences  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);

        String d= sharedpreferences.getString("Date",formattedDate);
        datetv.setText("Your medicine will finish on "+ d);

        Date futureDate= null;
        try {
            futureDate = dateFormat.parse(d);
            StartCounting(futureDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        daysupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Medicine.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                daysupdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                final String d=daysupdate.getText().toString();
                editor.putString("Date",d);
                editor.apply();
                datetv.setText("Your medicine will finish on "+ d);
                runnable =new Runnable()
                {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 1000);
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "yyyy-MM-dd");
                            Date futureDate= dateFormat.parse(d);
                            Date currentDate = new Date();
                            if (!currentDate.after(futureDate)) {
                                long diff = futureDate.getTime()
                                        - currentDate.getTime();
                                long days = diff / (24 * 60 * 60 * 1000);
                                diff -= days * (24 * 60 * 60 * 1000);
                                long hours = diff / (60 * 60 * 1000);
                                diff -= hours * (60 * 60 * 1000);
                                long minutes = diff / (60 * 1000);
                                diff -= minutes * (60 * 1000);
                                long seconds = diff / 1000;
                                daysleft.setText("" + String.format("%02d", days));
                            } else {
                                daysleft.setText("00");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(runnable, 1 * 1000);
                //handler.removeCallbacks(runnable);

//                SimpleDateFormat dateFormat = new SimpleDateFormat(
//                        "yyyy-MM-dd");
//                Date futureDate = null;
//                try {
//                    futureDate = dateFormat.parse(d);
//                    StartCounting(futureDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

            }
        });

    }

    private void StartCounting(final Date futureDate) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        daysleft.setText("" + String.format("%02d", days));
                    } else {
                        daysleft.setText("00");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }
}
