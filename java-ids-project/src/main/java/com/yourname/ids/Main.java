package com.yourname.ids;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: sudo java -jar java-ids-project.jar <interface-name> <mode>");
            System.out.println("Example: sudo java -jar java-ids-project.jar eth0 run");
            return;
        }
        String iface = args[0];
        System.out.println("Starting Java IDS on interface: " + iface);
        CaptureService capture = new CaptureService(iface);
        DetectionEngine engine = new DetectionEngine();
        AlertManager alert = new AlertManager("logs/ids_alerts.log");
        capture.setPacketHandler(packet -> {
            engine.processPacket(packet, alert);
        });
        capture.startCapture();
    }
}
