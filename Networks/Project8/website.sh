#!/bin/bash

# change router name/numbers. should be the one the rogue AS leeches onto
node=${1:-h1-1} # TODO: 1 becomes 5
bold=`tput bold`
normal=`tput sgr0`

while true; do
    out=`sudo python run.py --node $node --cmd "curl -s 13.0.1.1"` #13.0.1.1 becomes 1.0.0.8
    date=`date`
    echo $date -- $bold$out$normal
    sleep 1
done
