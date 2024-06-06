package com.example.modbusimplementation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ModbusClient modbusClient = new ModbusClient("192.168.18.7",502); // Replace with your Modbus server IP and port
        String TAG = "Ahsan";
        new Thread(() -> {
            try {
                modbusClient.Connect();
                Log.d(TAG, "Connected to Modbus server");

                // Example: Write to register 100 (address 99, zero-based) with value 12345
                int registerAddress = 1;
                int valueToWrite = 4;
                modbusClient.WriteSingleRegister(registerAddress, valueToWrite);
                Log.d(TAG, "Wrote value " + valueToWrite + " to register " + (registerAddress + 1));

                // Read back the value from the same register
                int[] registerValues = modbusClient.ReadHoldingRegisters(registerAddress, 1);
                int readValue = registerValues[0];
                Log.d(TAG, "Read value " + readValue + " from register " + (registerAddress + 1));

                // Disconnect the client
                modbusClient.Disconnect();
                Log.d(TAG, "Disconnected from Modbus server");

            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
            }
        }).start();
    }
}