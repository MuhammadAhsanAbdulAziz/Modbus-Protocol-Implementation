package com.example.modbusimplementation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.digitalpetri.enip.EtherNetIpClient;
import com.digitalpetri.enip.EtherNetIpClientConfig;
import com.digitalpetri.enip.EtherNetIpShared;
import com.digitalpetri.enip.cip.epath.DataSegment;
import com.digitalpetri.enip.cip.epath.EPath;
import com.digitalpetri.enip.cip.epath.PortSegment;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class EthernetIP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethernet_ip);
        EtherNetIpClientConfig config = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            config = EtherNetIpClientConfig.builder("192.168.18.8")
                    .setSerialNumber(0x00)
                    .setVendorId(0x00)
                    .setTimeout(Duration.ofSeconds(2))
                    .build();
        }

        EtherNetIpClient client = new EtherNetIpClient(config);

        try {
            client.connect().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        client.listIdentity().whenComplete((li, ex) -> {
            if (li != null) {
                li.getIdentity().ifPresent(id -> {
                    System.out.println("productName=" + id.getProductName());
                    System.out.println("revisionMajor=" + id.getRevisionMajor());
                    System.out.println("revisionMinor=" + id.getRevisionMinor());
                });
            } else {
                ex.printStackTrace();
            }
        });

        try {
            client.disconnect().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

// Call this before application / JVM shutdown
        EtherNetIpShared.releaseSharedResources();
    }
}