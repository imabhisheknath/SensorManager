# SensorManager
## SensorManager
---------------
How To Use

//add pemission for heart rate monitor

``` xml
<uses-permission android:name="android.permission.BODY_SENSORS"/>

```

```java 

//Add to your module App
  compile 'com.github.imabhisheknath:SensorManager:v1.0-beta'
  
  
  //initialize the sensor manager
  
  //you can refer key class for monitoring diffrent values from master module
  
   SensorsManager   sensorsManager = new SensorsManager(getApplicationContext()) {
            @Override
            void Accelerometer(ContentValues contentValues) {
                Log.d("myvaluesA", contentValues.getAsString(KEY.ACC_X_VALUE));
            }

            @Override
            void OnError(ContentValues contentValues) {
                Log.d("myerror", contentValues.getAsString(KEY.REGISTER_ERROR));
            }

            @Override
            void GyroScopeSensor(ContentValues contentValues) {
                Log.d("myvaluesG", contentValues.getAsString(KEY.ROTATION_X));
            }

            @Override
            void HeartBeatSensor(ContentValues contentValues) {
                Log.d("myvaluesH", contentValues.getAsString(KEY.HEART_RATE));
            }

            @Override
            void OriantationSensor(ContentValues contentValues) {
                Log.d("myvaluesO", contentValues.getAsString(KEY.AZIMUTH_ORIATION_ANGLE));
            }

            @Override
            void PedometerSensor(ContentValues contentValues) {
                Log.d("myvaluesP", contentValues.getAsString(KEY.PEDOMETER));
            }


        };
        
        //register  sensor which you want
        sensorsManager.setListner(SensorsManager.TYPE.ORI, SensorsManager.TYPE.ACC, SensorsManager.TYPE.GYRO, SensorsManager.TYPE.HEART_RATE, SensorsManager.TYPE.PEDOMETER);


//unregister sensor
 sensorsManager.unregisterSensors(SensorsManager.TYPE.ACC, SensorsManager.TYPE.GYRO, SensorsManager.TYPE.HEART_RATE, SensorsManager.TYPE.PEDOMETER);

```

