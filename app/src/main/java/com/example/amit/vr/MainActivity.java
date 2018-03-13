package com.example.amit.vr;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends Activity implements SensorEventListener {

    EditText txtAddress;
    String temp;
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private String IPAddress;
    private boolean connected = false;
    //private Server server;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtAddress = findViewById(R.id.serverIpEditText);
        // Get the sensors manager.
        sensorManager = (SensorManager) getSystemService(MainActivity.SENSOR_SERVICE);

        // Get the sensors.
        if (sensorManager != null) {
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int x = (int) (event.values[0] * 100);
        int y = (int) (event.values[1] * 100);
        int z = (int) (event.values[2] * 100);

        temp = Integer.toString(x) + "@" + Integer.toString(y) + "@" + Integer.toString(z);
        //Log.d("Sensor Data", temp);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void OnConnect(View view) {
        if (!connected) {
            IPAddress = txtAddress.getText().toString();
            try {
                Thread cThread = new Thread(new ClientThread());
                cThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("Button", "Pressed");
    }

    void DisConnect(View view) {
        connected = false;
        Log.d("Button", "unPressed");
    }

    private class ClientThread implements Runnable {
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(IPAddress);
                socket = new Socket(serverAddress, 21567);
                connected = true;
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.printf(temp);
                out.flush();
                Thread.sleep(20);
                while (connected) {
                    out.printf(temp);
                    out.flush();
                    Thread.sleep(2);


                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connected = false;
                    socket.close();
                    Log.d("Button", "quit");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}