# Java IDS Project

Lightweight Java Intrusion Detection System (IDS) skeleton using pcap4j for packet capture and simple rule-based detection (port-scan, brute-force, DoS). This repo contains starter code, build files, scripts and sample logs so you can run and extend the project.

## Quickstart
1. Install Java 11+ and Maven.
2. Install libpcap on your OS (Linux: `sudo apt install libpcap-dev`).
3. Build with: `mvn clean package`.
4. Run with elevated privileges: `sudo java -jar target/java-ids-project.jar --interface <if-name>`.

See `docs/` for details.
