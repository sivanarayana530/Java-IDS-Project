package com.yourname.ids;

import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.IpV4Packet;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DetectionEngine {

    private Map<String, SrcActivity> activityMap = new ConcurrentHashMap<>();
    private final int PORT_THRESHOLD = 20; // distinct ports
    private final long WINDOW_MS = 60 * 1000L;

    public void processPacket(Packet packet, AlertManager alertManager) {
        try {
            if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
                IpV4Packet ip = packet.get(IpV4Packet.class);
                TcpPacket tcp = packet.get(TcpPacket.class);
                String src = ip.getHeader().getSrcAddr().getHostAddress();
                int dstPort = tcp.getHeader().getDstPort().valueAsInt();
                boolean syn = tcp.getHeader().getSyn();
                boolean ack = tcp.getHeader().getAck();

                if (syn && !ack) {
                    SrcActivity sa = activityMap.computeIfAbsent(src, k -> new SrcActivity());
                    sa.recordPort(dstPort, System.currentTimeMillis());
                    int distinct = sa.countDistinctPortsWithinWindow(WINDOW_MS);
                    if (distinct > PORT_THRESHOLD) {
                        alertManager.raiseAlert("PortScan", src, "scanned_ports="+distinct);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SrcActivity {
        private Map<Integer, Long> portTs = new ConcurrentHashMap<>();

        void recordPort(int port, long ts) {
            portTs.put(port, ts);
        }

        int countDistinctPortsWithinWindow(long windowMs) {
            long now = System.currentTimeMillis();
            portTs.entrySet().removeIf(en -> (now - en.getValue()) > windowMs);
            return portTs.size();
        }
    }
}
