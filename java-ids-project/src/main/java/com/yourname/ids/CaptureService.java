package com.yourname.ids;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CaptureService {
    private String nifName;
    private PcapHandle handle;
    private Consumer<Packet> handler;
    private ExecutorService pool = Executors.newFixedThreadPool(4);

    public CaptureService(String nifName) {
        this.nifName = nifName;
    }

    public void setPacketHandler(Consumer<Packet> handler) {
        this.handler = handler;
    }

    public void startCapture() throws PcapNativeException, NotOpenException {
        try {
            PcapNetworkInterface nif = Pcaps.getDevByName(nifName);
            if (nif == null) {
                System.err.println("Interface not found: " + nifName);
                return;
            }
            int snapLen = 65536;
            PromiscuousMode mode = PromiscuousMode.PROMISCUOUS;
            int timeout = 10;
            handle = nif.openLive(snapLen, mode, timeout);
            PacketListener listener = packet -> {
                pool.submit(() -> {
                    try {
                        if (handler != null) handler.accept(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            };
            System.out.println("Capture started on " + nifName + " - press Ctrl+C to stop");
            handle.loop(-1, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
