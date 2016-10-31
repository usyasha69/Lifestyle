package com.example.pk.lifestyle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;

public class AccelerometerService extends Service {

    public static final float WEIGHT_OF_COLOR_START_VALUE = 50.0f;
    public static final int SIZE_OF_AXIS_LIST = 20;
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;
    public static final float MAX_VALUE_OF_COLOR = 100.0f;


    /**
     * Listener for update UI in lifestyle activity.
     */
    public interface UpdateLifestyleListener {
        void updateLifestyle(float green, float red);
    }

    private LocaleBinder localeBinder;
    private Handler handler;
    private SensorManager sensorManager;

    /**
     * Listeners.
     */
    private UpdateLifestyleListener updateLifestyleListener;
    private SensorEventListener sensorEventListener;

    /**
     * Values of green and red color at activity.
     */
    private float green;
    private float red;

    /**
     * Lists with x,y and z axis.
     */
    private ArrayList<AxisModel> axisModels;

    public void setUpdateLifestyleListener(UpdateLifestyleListener updateLifestyleListener) {
        this.updateLifestyleListener = updateLifestyleListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        localeBinder = new LocaleBinder();
        handler = new Handler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        createSensorEventListener();
        sensorManager.registerListener(sensorEventListener
                , sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);

        return localeBinder;
    }

    private void createSensorEventListener() {
        green = WEIGHT_OF_COLOR_START_VALUE;
        red = WEIGHT_OF_COLOR_START_VALUE;

        axisModels = new ArrayList<>();

        final LifestyleQualifier lifestyleQualifier = new LifestyleQualifier();

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    if (axisModels.size() < SIZE_OF_AXIS_LIST) {

                        axisModels.add(new AxisModel(sensorEvent.values[X_AXIS]
                                , sensorEvent.values[Y_AXIS], sensorEvent.values[Z_AXIS]));

                    } else {

                        red = lifestyleQualifier.getColor(red, axisModels);

                        if (updateLifestyleListener != null) {
                            green = MAX_VALUE_OF_COLOR - red;
                            
                            updateLifestyleListener.updateLifestyle(green, red);
                        }

                        axisModels = new ArrayList<>();

                        axisModels.add(new AxisModel(sensorEvent.values[X_AXIS]
                                , sensorEvent.values[Y_AXIS], sensorEvent.values[Z_AXIS]));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    public void stopWork() {
        handler.removeCallbacksAndMessages(null);
        sensorManager.unregisterListener(sensorEventListener);
    }

    public class LocaleBinder extends Binder {

        public AccelerometerService getService() {
            return AccelerometerService.this;
        }
    }
}
