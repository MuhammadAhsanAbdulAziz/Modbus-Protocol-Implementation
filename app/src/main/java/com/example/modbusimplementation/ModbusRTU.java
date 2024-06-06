package com.example.modbusimplementation;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fazecast.jSerialComm.SerialPort;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.SerialParameters;

import java.io.File;

public class ModbusRTU extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modbus_rtu);


        String TAG = "Ahsan";

        new Thread(() -> {
            try {
                SerialPort serialPort = SerialPort.getCommPort("COM1");
                serialPort.setBaudRate(9600);
                serialPort.setNumDataBits(8);
                serialPort.setParity(SerialPort.EVEN_PARITY);
                serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);

                if (serialPort.openPort()) {
                    System.out.println("Serial port connection opened successfully.");

                    // Write to register
                    int registerAddress = 1;
                    int valueToWrite = 4;
                    byte[] writeData = {0, (byte) registerAddress, 0, (byte) valueToWrite};
                    serialPort.writeBytes(writeData, writeData.length);

                    System.out.println("Wrote value " + valueToWrite + " to register " + registerAddress);

                    // Read from register
                    byte[] readData = new byte[2]; // Assuming 2 bytes for the register value
                    serialPort.readBytes(readData, readData.length);

                    int readValue = ((readData[0] & 0xFF) << 8) | (readData[1] & 0xFF);
                    System.out.println("Read value " + readValue + " from register " + registerAddress);

                    // Close the connection
                    serialPort.closePort();
                    System.out.println("Disconnected from Modbus RTU server.");
                } else {
                    System.err.println("Failed to open serial port connection.");
                }

            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
            }
        }).start();
    }
}