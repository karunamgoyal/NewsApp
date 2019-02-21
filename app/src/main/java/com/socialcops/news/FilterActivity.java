package com.socialcops.news;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button from;
    Button to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        from.setOnClickListener(this);
        to.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RadioGroup rg = findViewById(R.id.radioGroup);
                    final String value =
                            ((RadioButton) findViewById(rg.getCheckedRadioButtonId()))
                                    .getText().toString();
                    if (value != null && !value.equals(""))
                        Variables.SORT = value;

                    if (!from.getText().toString().equals("From")) {
                        Variables.FROM = from.getText().toString();
                    }
                    if (!to.getText().toString().equals("To")) {
                        Variables.TO = to.getText().toString();
                    }
                } catch (Exception e) {

                }
                onBackPressed();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClick(View v) {

        if (v == from) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String day1, month1;
                            if (dayOfMonth / 10 == 0)
                                day1 = "0" + dayOfMonth;
                            else
                                day1 = "" + dayOfMonth;
                            if ((monthOfYear + 1) / 10 == 0)
                                month1 = "0" + (monthOfYear + 1);
                            else
                                month1 = "" + (monthOfYear + 1);
                            from.setText(year + "-" + month1 + "-" + day1);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == to) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String year1, day1, month1;
                            if (dayOfMonth / 10 == 0)
                                day1 = "0" + dayOfMonth;
                            else
                                day1 = "" + dayOfMonth;
                            if ((monthOfYear + 1) / 10 == 0)
                                month1 = "0" + (monthOfYear + 1);
                            else
                                month1 = "" + (monthOfYear + 1);
                            to.setText(year + "-" + month1 + "-" + day1);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
