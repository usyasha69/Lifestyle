package com.example.pk.lifestyle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LifestyleActivity extends AppCompatActivity implements AccelerometerService.UpdateLifestyleListener {
    private LinearLayout green;
    private LinearLayout red;

    private boolean isBound;
    private AccelerometerService accelerometerService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            accelerometerService = ((AccelerometerService.LocaleBinder) iBinder).getService();
            accelerometerService.setUpdateLifestyleListener(LifestyleActivity.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle);

        green = (LinearLayout) findViewById(R.id.al_green);
        red = (LinearLayout) findViewById(R.id.al_red);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, AccelerometerService.class)
                , serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            accelerometerService.stopWork();
            accelerometerService.setUpdateLifestyleListener(null);
            unbindService(serviceConnection);
        }
    }

    @Override
    public void updateLifestyle(float greenShare, float redShare) {
        green.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, redShare));
        red.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, greenShare));
    }
}
