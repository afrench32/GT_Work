#!/usr/bin/python
  
# Robert Johns     dynamictopo.py      August 25, 2017
# Project 1
# Creates a dynamic topology based on command line parameters and starts the Mininet Command Line Interface.

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.log import lg, output, setLogLevel
from mininet.node import CPULimitedHost
from mininet.link import TCLink
from mininet.cli import CLI
import argparse
import sys
import os

# Parse Command Line Arguments
parser = argparse.ArgumentParser(description="Dynamic Topologies")
parser.add_argument('--delay',
                    help="Latency of network links, in ms",
                    required=True)

parser.add_argument('--bw',
                    type=int,
                    help=("Bandwidth of the links in Mbps."
                    "Must be >= 1"),
                    required=True)

parser.add_argument('--z',
                    type=int,
                    help=("Number of zones to create."
                    "Must be >= 1"),
                    required=True)

parser.add_argument('--n',
                    type=int,
                    help=("Number of hosts to create in each zone."
                    "Must be >= 1"),
                    required=True)

args = parser.parse_args()

lg.setLogLevel('info')

# Topology to be instantiated in Mininet
class DynamicTopo(Topo):
    "Dynamic Topology"

    def __init__(self, n=1, delay='1ms', z=1, bw=1, cpu=.1, max_queue_size=None, **params):
        """Ring topology with z zones.
           n: number of hosts per zone
           cpu: system fraction for each host
           bw: link bandwidth in Mb/s
           delay: link latency (e.g. 10ms)"""

        # Initialize topo
        Topo.__init__(self, **params)

        #TODO: Create your Dynamic Mininet Topology here!
        #NOTE: You MUST label switches as s1, s2, ... sz
        #NOTE: You MUST label hosts as h1-1, h1-2, ... hz-n     
        #HINT: Use a loop to construct the topology in pieces.
        
        # set link and host configs for instance
        hostConfig = {'cpu': cpu}
        linkConfig = {'bw': bw, 'delay': delay, 'loss': 0,
                      'max_queue_size': max_queue_size}
        
        zCount = 0
        oldSwitch = None
        firstSwitch = None
        
        for i in range(z):
            zCount += 1
            tempSwitch = self.addSwitch('s' + str(zCount))
            # save reference to first switch so last switch can connect
            if zCount == 1:
                firstSwitch = tempSwitch
            hCount = 0
            # create and connect hosts
            for j in range(n):
                hCount += 1
                tempHost = self.addHost('h' + str(zCount) + '-' + str(hCount),
                                        **hostConfig)
                self.addLink(tempSwitch, tempHost, 
                             port1 = 1 + j, **linkConfig)
            # if not the first zone, connect switch i to switch i-1
            if oldSwitch != None:
                self.addLink(tempSwitch, oldSwitch,
                             port1 = n + 2, port2 = n + 1, **linkConfig)
            # if the last zone, connect switch z to switch 1
            if i == z - 1:
                self.addLink(tempSwitch, firstSwitch,
                             port1 = n + 1, port2 = n + 2, **linkConfig)
            oldSwitch = tempSwitch
            

        
def main():
    "Create specified topology and launch the command line interface"    
    topo = DynamicTopo(n=args.n, delay=args.delay, z=args.z, bw=args.bw)
    net = Mininet(topo=topo, host=CPULimitedHost, link=TCLink)
    net.start()

    #TODO: Since this topology contains a cycle, we must enable the Spanning Tree Protocol (STP) on each switch.
    #      This is done with the following line of code: s1.cmd('ovs-vsctl set bridge s1 stp-enable=true')
    #      Here, you will need to generate this line of code for each switch.
    #HINT: You will need to get the switch objects from the net object defined above.
    
    for i in range(args.z):
        name = 's' + str(i + 1)
        switch = net.getNodeByName(name)
        command = 'ovs-vsctl set bridge ' + name + " stp-enable=true"
        switch.cmd(command)
    
    CLI(net)
    net.stop()
    
if __name__ == '__main__':
    setLogLevel('info')
    main()
