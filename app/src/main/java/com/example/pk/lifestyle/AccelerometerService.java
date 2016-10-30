package com.example.pk.lifestyle;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AccelerometerService extends Service {
    private LocaleBinder localeBinder;

    public interface UpdateLifestyleListener {
        void updateLifestyle(float green, float red);
    }

    private UpdateLifestyleListener updateLifestyleListener;

    public void setUpdateLifestyleListener(UpdateLifestyleListener updateLifestyleListener) {
        this.updateLifestyleListener = updateLifestyleListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        localeBinder = new LocaleBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (updateLifestyleListener != null) {
            updateLifestyleListener.updateLifestyle(20, 80);
        }

        return localeBinder;
    }

    public class LocaleBinder extends Binder {

        public AccelerometerService getService() {
            return AccelerometerService.this;
        }
    }
}
