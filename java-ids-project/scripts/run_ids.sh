#!/bin/bash
if [ "$EUID" -ne 0 ]; then
  echo "Please run as root (sudo)"
  exit 1
fi
IFACE="$1"
if [ -z "$IFACE" ]; then
  echo "Usage: sudo ./run_ids.sh <interface>"
  exit 1
fi
java -jar target/java-ids-project.jar $IFACE run
