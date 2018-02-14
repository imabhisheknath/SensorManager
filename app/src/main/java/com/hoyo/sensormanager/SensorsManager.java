package com.hoyo.sensormanager;

import android.content.ContentValues;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Abhishek on 01-02-2018.
 */

public abstract class SensorsManager {
    private SensorManager mSensorManager;
    Context context;

    public enum TYPE {
        ACC, ORI, GYRO, HEART_RATE, PEDOMETER
    }


    public SensorsManager(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }


    public abstract void Accelerometer(ContentValues contentValues);

    public abstract void OnError(ContentValues contentValues);

    public abstract void GyroScopeSensor(ContentValues contentValues);

    public abstract void HeartBeatSensor(ContentValues contentValues);

    public abstract void OriantationSensor(ContentValues contentValues);

    public abstract void PedometerSensor(ContentValues contentValues);


    // Handling the Listners


    public void setListner(TYPE... type) {
        for (int i = 0; i < type.length; i++) {
            switch (type[i]) {
                case ACC:
                    Log.d("mylog", "Alog");
                    Sensor mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                        mSensorManager.registerListener(accelometerListner, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY.REGISTER_ERROR, "we dont have an accelerometer!");
                        OnError(contentValues);
                    }

                    break;

                case GYRO:
                    Sensor mGyroScopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                    if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
                        mSensorManager.registerListener(gyroscopeSensorListner, mGyroScopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY.REGISTER_ERROR, "we dont have  gyroscope!");
                        OnError(contentValues);
                    }

                    break;

                case HEART_RATE:
                    if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
                        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
                        mSensorManager.registerListener(heartratesensorlistner, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY.REGISTER_ERROR, "we dont have  heartratesensor!");
                        OnError(contentValues);
                    }

                    break;


                case ORI:
                    if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {

                        Sensor mOriatationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
                        mSensorManager.registerListener(oriantationsensorlistner, mOriatationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY.REGISTER_ERROR, "we dont have an OriationSensor!");
                        OnError(contentValues);
                    }
                    break;


                case PEDOMETER:
                    if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {

                        Sensor mPedometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                        mSensorManager.registerListener(pedometerlistner, mPedometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY.REGISTER_ERROR, "we dont have Pedometersensor!");
                        OnError(contentValues);
                    }
                    break;

            }


        }

    }


    //unregister Sensors

    public void unregisterSensors(TYPE... types) {


        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
                case ACC:
                    mSensorManager.unregisterListener(accelometerListner);
                    break;
                case ORI:
                    mSensorManager.unregisterListener(oriantationsensorlistner);
                    break;
                case HEART_RATE:
                    mSensorManager.unregisterListener(heartratesensorlistner);
                    break;
                case GYRO:
                    mSensorManager.unregisterListener(gyroscopeSensorListner);
                    break;
                case PEDOMETER:
                    mSensorManager.unregisterListener(pedometerlistner);
                    break;


            }
        }

    }


    //AccMeter Sensor
    private SensorEventListener accelometerListner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            ContentValues contentValues = new ContentValues();
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            contentValues.put(KEY.ACC_X_VALUE, x);
            contentValues.put(KEY.ACC_Y_VALUE, y);
            contentValues.put(KEY.ACC_Z_VALUE, z);
            Accelerometer(contentValues);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //Gyroscope Sensor
    private SensorEventListener gyroscopeSensorListner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY.ROTATION_X, x);
            contentValues.put(KEY.ROTATION_Y, y);
            contentValues.put(KEY.ROTATION_Z, z);
            GyroScopeSensor(contentValues);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //HeartRateSensor
    private SensorEventListener heartratesensorlistner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            int a = (int) sensorEvent.values[0];
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY.HEART_RATE, a);
            HeartBeatSensor(contentValues);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //Oriantation Sensor
    private SensorEventListener oriantationsensorlistner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            ContentValues contentValues = new ContentValues();
            float azimuth = sensorEvent.values[0];
            float pitch = sensorEvent.values[1];
            float roll = sensorEvent.values[2];
            contentValues.put(KEY.AZIMUTH_ORIENTATION_ANGLE, azimuth);
            contentValues.put(KEY.PITCH_ORIENTATION_ANGLE, pitch);
            contentValues.put(KEY.ROLL_ORIENTATION_ANGLE, roll);
            OriantationSensor(contentValues);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //pedometer sensors
    private SensorEventListener pedometerlistner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY.PEDOMETER, String.valueOf(sensorEvent.values[0]));
            PedometerSensor(contentValues);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


}










