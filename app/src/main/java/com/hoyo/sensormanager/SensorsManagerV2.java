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
 * Update by Praba
 */

public class SensorsManagerV2 {

    private SensorManager mSensorManager;
    Context context;

    private AccelerometerDataListener accelerometerDataListener;
    private GyroScopeDataListener gyroscopeDataListener;
    private HeartRateDataListener heartRateDataListener;
    private OrientationDataListener orientationDataListener;
    private PedometerDataListener pedometerDataListener;


    public interface SensorListener {
        void onDataChanged(ContentValues contentValues);
        void onError(String errorMessage);
    }

    public interface AccelerometerDataListener {
        void onDataChanged(float x, float y, float z);
        void onError(String errorMessage);
    }

    public interface GyroScopeDataListener {
        void onDataChanged(float x, float y, float z);
        void onError(String errorMessage);
    }

    public interface HeartRateDataListener {
        void onDataChanged(float rate);
        void onError(String errorMessage);
    }

    public interface OrientationDataListener {
        void onDataChanged(float azimuth, float pitch, float roll);
        void onError(String errorMessage);
    }

    public interface PedometerDataListener {
        void onDataChanged(float stepsCount);
        void onError(String errorMessage);
    }

    public enum SENSOR {
        ACCELEROMETER, ORIENTATION, GYROSCOPE, HEART_RATE, PEDOMETER
    }


    public SensorsManagerV2(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }


    /**
     *
     * @param listener
     * @return
     */

    public SensorsManagerV2 registerAccelerometerSensor(AccelerometerDataListener listener){
        this.accelerometerDataListener = listener;
        addSensor(SENSOR.ACCELEROMETER);
        return this;
    }


    /**
     *
     * @param listener
     * @return
     */

    public SensorsManagerV2 registerGyroscopeSensor(GyroScopeDataListener listener){
        this.gyroscopeDataListener = listener;
        addSensor(SENSOR.GYROSCOPE);
        return this;
    }

    /**
     *
     * @param listener
     * @return
     */

    public SensorsManagerV2 registerOrientationSensor(OrientationDataListener listener){
        this.orientationDataListener = listener;
        addSensor(SENSOR.ORIENTATION);
        return this;
    }


    /**
     *
     * @param listener
     * @return
     */

    public SensorsManagerV2 registerHeartRateSensor(HeartRateDataListener listener){
        this.heartRateDataListener = listener;
        addSensor(SENSOR.HEART_RATE);
        return this;
    }

    /**
     *
     * @param listener
     * @return
     */

    public SensorsManagerV2 registerPedometer(PedometerDataListener listener){
        this.pedometerDataListener = listener;
        addSensor(SENSOR.PEDOMETER);
        return this;
    }

    /**
     *
     * @param sensor
     */

    public void addSensor(SENSOR sensor){
        switch (sensor) {
            case ACCELEROMETER:
                Sensor mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (mAccelerometerSensor != null) {
                    mSensorManager.registerListener(accelerometerListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    Log.e("SensorManagerV2","Registering Accelerometer");
                } else {
                    if(accelerometerDataListener!=null){
                        accelerometerDataListener.onError("This Device Doesn't Support Accelerometer Sensor");
                    } else {
                        Log.e("SensorManagerV2","Error Registering Accelerometer, Unable to Push to the instance listener");
                    }
                }
                break;

            case GYROSCOPE:
                Sensor mGyroScopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                if (mGyroScopeSensor != null) {
                    mSensorManager.registerListener(gyroscopeSensorListener, mGyroScopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                   if(gyroscopeDataListener!=null){
                       gyroscopeDataListener.onError("This Device Doesn't Support Gyroscope Sensor");
                   }
                }

                break;

            case HEART_RATE:
                Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
                if (mHeartRateSensor != null) {
                    mSensorManager.registerListener(heartrateSensorListener, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
                } else {

                    if(heartRateDataListener!=null){
                        heartRateDataListener.onError("This Device Doesn't Support HeartRate Sensor");
                    }
                }

                break;


            case ORIENTATION:
                Sensor mOriatationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
                if (mOriatationSensor != null) {
                    mSensorManager.registerListener(orientationSensorListener, mOriatationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    if(orientationDataListener!=null){
                        orientationDataListener.onError("This Device Doesn't Support Orientation Sensor");
                    }
                }
                break;


            case PEDOMETER:
                Sensor mPedometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                if (mPedometerSensor != null) {
                    mSensorManager.registerListener(pedometerListener, mPedometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    if(pedometerDataListener!=null){
                        pedometerDataListener.onError("This Device Doesn't Support Pedometer Sensor");
                    }
                }
                break;

            default:
                Log.e("SensorManagerV2","Error : Unknown Sensor Registration Request -"+sensor);

        }
    }


    // Handling the Listners

   //unregister Sensors

    /**
     *
     * @param sensor
     */

    public void unregisterSensors(SENSOR... sensor) {

        for (int i = 0; i < sensor.length; i++) {
            switch (sensor[i]) {
                case ACCELEROMETER:
                    mSensorManager.unregisterListener(accelerometerListener);
                    break;
                case ORIENTATION:
                    mSensorManager.unregisterListener(orientationSensorListener);
                    break;
                case HEART_RATE:
                    mSensorManager.unregisterListener(heartrateSensorListener);
                    break;
                case GYROSCOPE:
                    mSensorManager.unregisterListener(gyroscopeSensorListener);
                    break;
                case PEDOMETER:
                    mSensorManager.unregisterListener(pedometerListener);
                    break;

            }
        }

    }


    //AccMeter Sensor
    private SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            if(accelerometerDataListener!=null){
                accelerometerDataListener.onDataChanged(values[0],values[1],values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //Gyroscope Sensor
    private SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            if(gyroscopeDataListener!=null){
                gyroscopeDataListener.onDataChanged(values[0],values[1],values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //HeartRateSensor
    private SensorEventListener heartrateSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(heartRateDataListener!=null){
                heartRateDataListener.onDataChanged(sensorEvent.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //Orientation Sensor
    private SensorEventListener orientationSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values = sensorEvent.values;
            if(orientationDataListener!=null){
                orientationDataListener.onDataChanged(values[0],values[1],values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    //pedometer sensors
    private SensorEventListener pedometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(pedometerDataListener!=null){
                pedometerDataListener.onDataChanged(sensorEvent.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


}










