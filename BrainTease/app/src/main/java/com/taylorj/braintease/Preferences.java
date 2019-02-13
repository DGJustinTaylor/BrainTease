package com.taylorj.braintease;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Preferences extends AppCompatActivity
{
    private RadioButton rbtnlearn;
    private RadioButton rbtnlaugh;
    private RadioButton rbtnoff;
    private RadioGroup rgroup;
    private Button btnApply;
    private EditText mHour;
    private EditText mMinute;
    private TextView mHourErr;
    private TextView mMinErr;
    private Boolean hasSet = false;

    public static String message;

    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        rbtnlearn = (RadioButton) findViewById(R.id.rbtnLearn);
        rbtnlaugh = (RadioButton) findViewById(R.id.rbtnLaugh);
        rbtnoff = (RadioButton) findViewById(R.id.rbtnOff);
        rgroup = (RadioGroup) findViewById(R.id.rdgButtons);
        btnApply = (Button) findViewById(R.id.btnApply);
        mHour = (EditText) findViewById(R.id.etxtHour);
        mMinute = (EditText) findViewById(R.id.etxtMin);
        mHourErr = (TextView) findViewById(R.id.txtHourErr);
        mMinErr = (TextView) findViewById(R.id.txtMinErr);

        mPrefs = getSharedPreferences("myPref", 0);
        editor = mPrefs.edit();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(hasSet == false)
        {
            if(mPrefs.getBoolean("learn", false) == true)
            {
                rgroup.check(rbtnlearn.getId());
            }
            else if(mPrefs.getBoolean("laugh", false) == true)
            {
                rgroup.check(rbtnlaugh.getId());
            }
            else if(mPrefs.getBoolean("off", false) == true)
            {
                rgroup.check(rbtnoff.getId());
            }

            if(mPrefs.getInt("hour", 0) != 0)
            {
                StringBuilder sb = new StringBuilder();


                mHour.setText(sb.append(mPrefs.getInt("hour", 0)));
            }

            if(mPrefs.getInt("min", 0) != 0)
            {
                StringBuilder sb = new StringBuilder();


                mMinute.setText(sb.append(mPrefs.getInt("min", 0)));
            }
        }
    }

    private void saveInfo()
    {
        boolean goodHour = false;
        boolean goodMin = false;

        if(mHour.getText().toString().isEmpty())
        {
            mHourErr.setText("Required");
            mHourErr.setTextColor(Color.RED);
        }
        else if(!isNumeric(mHour.getText().toString()))
        {
            mHourErr.setText("Numeric input required");
            mHourErr.setTextColor(Color.RED);
        }
        else if(Double.parseDouble(mHour.getText().toString()) > 24 || Double.parseDouble(mHour.getText().toString()) < 0)
        {
            mHourErr.setText("Must be between 0 and 24");
            mHourErr.setTextColor(Color.RED);
        }
        else if (Double.parseDouble(mHour.getText().toString()) - Math.floor(Double.parseDouble(mHour.getText().toString())) != 0)
        {
            mHourErr.setText("Whole number required");
            mHourErr.setTextColor(Color.RED);
        }
        else
        {
            goodHour = true;
        }

        if(mMinute.getText().toString().isEmpty())
        {
            mMinErr.setText("Required");
            mMinErr.setTextColor(Color.RED);
        }
        else if(!isNumeric(mMinute.getText().toString()))
        {
            mMinErr.setText("Numeric input required");
            mMinErr.setTextColor(Color.RED);
        }
        else if(Double.parseDouble(mMinute.getText().toString()) > 59 || Double.parseDouble(mMinute.getText().toString()) < 0)
        {
            mMinErr.setText("Must be between 0 and 59");
            mMinErr.setTextColor(Color.RED);
        }
        else if (Double.parseDouble(mMinute.getText().toString()) - Math.floor(Double.parseDouble(mMinute.getText().toString())) != 0)
        {
            mMinErr.setText("Whole number required");
            mMinErr.setTextColor(Color.RED);
        }
        else
        {
            goodMin = true;
        }

        if(goodHour && goodMin)
        {
            int hour = Integer.parseInt(mHour.getText().toString());
            int minute = Integer.parseInt(mMinute.getText().toString());

            editor.putBoolean("learn", rbtnlearn.isChecked());
            editor.putBoolean("laugh", rbtnlaugh.isChecked());
            editor.putBoolean("off", rbtnoff.isChecked());

            editor.putInt("hour", hour);
            editor.putInt("min", minute);

            editor.commit();

            hasSet = true;

            if(mPrefs.getBoolean("learn", false) == true)
            {
                message = MainActivity.dbManager.singleFact();
                NotificationReceiver.on = true;
            }
            else if(mPrefs.getBoolean("laugh", false) == true)
            {
                message = MainActivity.dbManager.singleJoke();
                NotificationReceiver.on = true;
            }
            else if(mPrefs.getBoolean("off", false) == true)
            {
                NotificationReceiver.on = false;
            }

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, mPrefs.getInt("hour", 01));
            calendar.set(Calendar.MINUTE, mPrefs.getInt("min", 0));
            calendar.set(Calendar.SECOND, 0);

            if(calendar.getTime().compareTo(new Date()) < 0)
            {
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }

            Intent intent = new Intent(Preferences.this, NotificationReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(Preferences.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager aManager = (AlarmManager) Preferences.this.getSystemService(Preferences.this.ALARM_SERVICE);
            aManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

            Toast.makeText(Preferences.this, "Settings Applied!", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isNumeric(String num)
    {
        try
        {
            double d = Double.parseDouble(num);
        }
        catch (NumberFormatException | NullPointerException nfe)
        {
            return false;
        }

        return true;
    }
}
