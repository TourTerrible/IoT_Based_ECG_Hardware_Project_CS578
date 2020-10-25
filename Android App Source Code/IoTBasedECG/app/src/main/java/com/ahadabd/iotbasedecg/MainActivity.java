package com.ahadabd.iotbasedecg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    CardView status,monitor,medicine, doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status=findViewById(R.id.healthstatus);
        monitor=findViewById(R.id.monitor);
        medicine=findViewById(R.id.medicine);
        doctor=findViewById(R.id.appointment);

        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Monitor.class);
                startActivity(i);
            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Medicine.class);
                startActivity(i);
            }
        });


        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Doctor.class);
                startActivity(i);
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if health is normal

                if(true){
                    Snackbar snackbar1 = Snackbar.make(v, "Health is normal", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }

                //if health is not normal
                else{
                    Snackbar snackbar1 = Snackbar.make(v, "Health is not normal", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            }
        });

    }
}
