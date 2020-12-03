package com.example.myapplication;
    /*
    CHARAKTERYSTYKA KROKU
    Aby wykryć krok, pomiary przyspieszenia liniowego muszą przekroczyć górny i dolny próg.
    Czas między przekroczeniem górnego progu a spadkiem poniżej dolnego progu musi wynosić od 150 ms do 400 ms.
    Srednia długość kroku wynosi 0,74m = (0,7 + 0,78)/2
    */
import android.annotation.SuppressLint;
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
    float step = (float) 0.74;

    //współrzędne początkowe
    float x = 0;
    float y = 0;

    //zmienna czasowa
    long time = 0;
    long timeStart = 0;
    long timeStop = 0;

    //zmienna orientacyjna
    float azimuth = 0;

    //ilość kroków
    float stepCounter = 0;

    //dlugosc kroku
    double stepLength = 0.74; //[m]

    //koordynaty 2D
    double newX = 0;
    double newY = 0;
    double X = 0;
    double Y = 0;

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
    @SuppressLint("DefaultLocale")
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent != null){
            switch(sensorEvent.sensor.getType()){
                case Sensor.TYPE_ORIENTATION:
                    binding.orientationX.setText(String.format("%.03f",sensorEvent.values[0]));
                    orix = sensorEvent.values[0];
                    binding.orientationY.setText(String.format("%.03f",sensorEvent.values[1]));
                    oriy = sensorEvent.values[1];
                    binding.orientationZ.setText(String.format("%.03f",sensorEvent.values[2]));
                    oriz = sensorEvent.values[2];
                    break;

                case Sensor.TYPE_GRAVITY:
                    binding.gravityX.setText(String.format("%.03f",sensorEvent.values[0]));//pobranie i załadowanie wartości do layout
                    gravity[0] = sensorEvent.values[0];                                    //pobranie wartości do zmiennej
                    binding.gravityY.setText(String.format("%.03f",sensorEvent.values[1]));
                    gravity[1] = sensorEvent.values[1];
                    binding.gravityZ.setText(String.format("%.03f",sensorEvent.values[2]));
                    gravity[2] = sensorEvent.values[2];
                    break;

                case Sensor.TYPE_ACCELEROMETER:
                    binding.acceleratorX.setText(String.format("%.03f",sensorEvent.values[0]));
                    accx = sensorEvent.values[0];
                    binding.acceleratorY.setText(String.format("%.03f",sensorEvent.values[1]));
                    accy = sensorEvent.values[1];
                    binding.acceleratorZ.setText(String.format("%.03f",sensorEvent.values[2]));
                    accz = sensorEvent.values[2];

//przyspieszenie liniowe
                    //obliczenie linear_acceleration wg wzorów z artykułu
                    gravity_2[0] = alpha * gravity[0] + (1 - alpha) * accx;
                    gravity_2[1] = alpha * gravity[1] + (1 - alpha) * accy;
                    gravity_2[2] = alpha * gravity[2] + (1 - alpha) * accz;

                    linear_acceleration[0] = accx - gravity_2[0];
                    linear_acceleration[1] = accy - gravity_2[1];
                    linear_acceleration[2] = accz - gravity_2[2];

                    //załadowanie wartości linear_acceleration do layout
                    binding.linearAcceleratorX.setText(String.format("%.03f",linear_acceleration[0]));
                    binding.linearAcceleratorY.setText(String.format("%.03f",linear_acceleration[1]));
                    binding.linearAcceleratorZ.setText(String.format("%.03f",linear_acceleration[2]));

//wykrycie kroku
                    //przekroczenie górnego progu przyspieszenia oraz początek liczenie czasu kroku
                    if((linear_acceleration[1]>=0.5 && linear_acceleration[2]>=0.08)||(linear_acceleration[2]>=0.5 && linear_acceleration[0]>=0.5)){//||(linear_acceleration[0]>=0.5 && linear_acceleration[1]>=0.5&& linear_acceleration[2]>=0.5)){ //[m/s]
                        timeStart = System.currentTimeMillis();
                    }
                    //przekroczenie dolnego progu przyspieszenia oraz obliczenie czasu kroku
                    if((linear_acceleration[1]<=-0.5 && linear_acceleration[2]<=-0.08)||(linear_acceleration[2]<=-0.5 && linear_acceleration[0]<=-0.5)){//||(linear_acceleration[0]<=-0.5 && linear_acceleration[1]<=-0.5 && linear_acceleration[2]<=-0.5)){
                        timeStop = System.currentTimeMillis();
                        time = timeStop - timeStart;
                        timeStop = 0;
                        timeStart = 0;
                    }
                    //wykrycie kroku - odpowiednia ilość czasu od przekroczenia górnego progu do przekroczenia dolnego

                    if(time >= 150 && time <= 400){
                        stepCounter++;//zwiększenie licznika kroków
                        azimuth = orix;//pobranie azymutu z sensora
                        newX = (double) Math.sin(azimuth) * stepLength;//newX i newY - wpsółrzędne obliczone od punktu (0, 0);
                        newY = (double) Math.cos(azimuth) * stepLength;
                        X = newX + X;//następnie obliczane są "rzeczywiste" współrzędne X i Y - dodane nowej(new) do poprzedniej(bez new)
                        Y = newY + Y;
                        binding.stepCounter.setText(String.valueOf(stepCounter));//wyświetlanie danych na ekranie
                        binding.azimuth.setText(String.valueOf(azimuth));
                        binding.coordinateX.setText(String.valueOf(X));
                        binding.coordinateY.setText(String.valueOf(Y));
                        time = 0;//zerowanie liczników itd.
                        azimuth = 0;
                        newX = 0;
                        newY = 0;
                    }
                    if(stepCounter == 10)//liczenie kroków od 0 do 10 i od początku
                        stepCounter = 0;
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    binding.magnetX.setText(String.format("%.03f",sensorEvent.values[0]));
                    magx = sensorEvent.values[0];
                    binding.magnetY.setText(String.format("%.03f",sensorEvent.values[1]));
                    magy = sensorEvent.values[1];
                    binding.magnetZ.setText(String.format("%.03f",sensorEvent.values[2]));
                    magz = sensorEvent.values[2];
                    break;
            }
        }
    }

    public void onAccuracyChanged(@Nullable Sensor p0, int p1) {
    }

}