package com.example.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ActivityMainBinding binding;

    float accx;
    float accy;
    float accz;

    float magx;
    float magy;
    float magz;

    float orix;
    float oriy;
    float oriz;

    float alpha = (float) 0.8;
    float [] linear_acceleration = {0, 0, 0};
    float [] gravity = {0, 0, 0};
    float [] gravity_2 = {0, 0, 0};

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(this.getLayoutInflater());

        ConstraintLayout constraintLayout = binding.getRoot();
        this.setContentView(constraintLayout);

    }

    protected void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.setSensors();
    }

    private void setSensors() {
        Sensor acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acceleratorSensor != null) {
            sensorManager.registerListener(this, acceleratorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("ACCELERATOR", "NOT FOUND");
        }

        Sensor magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetSensor != null) {
            sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("MAGNET", "NOT FOUND");
        }

        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (orientationSensor != null) {
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("ORIENTATION", "NOT FOUND");
        }

        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (orientationSensor != null) {
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("GRAVITY", "NOT FOUND");
        }
    }

    //pobranie i wyświetlenie wartości z czujników
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent != null){
            switch(sensorEvent.sensor.getType()){
                case Sensor.TYPE_GRAVITY:
                    binding.gravityX.setText(String.valueOf(sensorEvent.values[0]));
                    gravity[0] = sensorEvent.values[0];
                    binding.gravityY.setText(String.valueOf(sensorEvent.values[1]));
                    gravity[1] = sensorEvent.values[1];
                    binding.gravityZ.setText(String.valueOf(sensorEvent.values[2]));
                    gravity[2] = sensorEvent.values[2];

                case Sensor.TYPE_ACCELEROMETER:
                    binding.acceleratorX.setText(String.valueOf(sensorEvent.values[0]));
                    accx = sensorEvent.values[0];
                    binding.acceleratorY.setText(String.valueOf(sensorEvent.values[1]));
                    accy = sensorEvent.values[1];
                    binding.acceleratorZ.setText(String.valueOf(sensorEvent.values[2]));
                    accz = sensorEvent.values[2];

                    gravity_2[0] = alpha * gravity[0] + (1 - alpha) * accx;
                    gravity_2[1] = alpha * gravity[1] + (1 - alpha) * accy;
                    gravity_2[2] = alpha * gravity[2] + (1 - alpha) * accz;

                    linear_acceleration[0] = accx - gravity_2[0];
                    linear_acceleration[1] = accy - gravity_2[1];
                    linear_acceleration[2] = accz - gravity_2[2];

                case Sensor.TYPE_MAGNETIC_FIELD:
                    binding.magnetX.setText(String.valueOf(sensorEvent.values[0]));
                    magx = sensorEvent.values[0];
                    binding.magnetY.setText(String.valueOf(sensorEvent.values[1]));
                    magy = sensorEvent.values[1];
                    binding.magnetZ.setText(String.valueOf(sensorEvent.values[2]));
                    magz = sensorEvent.values[2];

                case Sensor.TYPE_ORIENTATION:
                    binding.orientationX.setText(String.valueOf(sensorEvent.values[1]));
                    orix = sensorEvent.values[1];
                    binding.orientationY.setText(String.valueOf(sensorEvent.values[2]));
                    oriy = sensorEvent.values[2];
                    binding.orientationZ.setText(String.valueOf(sensorEvent.values[0]));
                    oriz = sensorEvent.values[0];

                    break;
            }
        }
    }

    public void onAccuracyChanged(@Nullable Sensor p0, int p1) {
    }

}