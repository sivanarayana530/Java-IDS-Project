package com.yourname.ids;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlertManager {
    private String logfile;

    public AlertManager(String logfile) {
        this.logfile = logfile;
        try (PrintWriter pw = new PrintWriter(new FileWriter(logfile, true))) {
            pw.println("--- IDS started at " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()) + " ---");
        } catch (Exception e) {}
    }

    public synchronized void raiseAlert(String type, String srcIp, String details) {
        String ts = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date());
        String line = String.format("[ALERT] %s - %s - src=%s - %s", ts, type, srcIp, details);
        System.out.println(line);
        try (PrintWriter pw = new PrintWriter(new FileWriter(logfile, true))) {
            pw.println(line);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
