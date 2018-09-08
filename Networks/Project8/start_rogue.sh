#!/bin/bash

echo "Killing any existing rogue AS"
./stop_rogue.sh

# TODO: change node name/number, and corresponding parts of string.
# 4 becomes 6
echo "Starting rogue AS"
sudo python run.py --node R4 --cmd "/usr/lib/quagga/zebra -f conf/zebra-R4.conf -d -i /tmp/zebra-R4.pid > logs/R4-zebra-stdout"
sudo python run.py --node R4 --cmd "/usr/lib/quagga/bgpd -f conf/bgpd-R4.conf -d -i /tmp/bgpd-R4.pid > logs/R4-bgpd-stdout"
