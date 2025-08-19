#!/bin/bash
# Simple simulation scripts that run from attacker (Kali)
TARGET_IP="$1"
if [ -z "$TARGET_IP" ]; then
  echo "Usage: ./simulate_scan.sh <target_ip>"
  exit 1
fi
# Fast SYN scan
nmap -sS -p1-500 -T4 $TARGET_IP
# Slow scan (low and slow)
nmap -sS -p1-500 --scan-delay 200ms $TARGET_IP
# Brute-force demo (use hydra if installed):
# hydra -l root -P /usr/share/wordlists/rockyou.txt ssh://$TARGET_IP
