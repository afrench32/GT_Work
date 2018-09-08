# Robert Johns     complextopo.py      August 25, 2017

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.link import TCLink
from mininet.util import custom
                                                                            
# Topology to be instantiated in Mininet
class ComplexTopo(Topo):
    "Mininet Complex Topology"

    def __init__(self, cpu=.1, max_queue_size=None, **params):

        # Initialize topo
        Topo.__init__(self, **params)

        # Set configurations for hosts and links
        hostConfig = {'cpu': cpu}
        ethernetLinkConfig = {'bw': 25, 'delay': '2ms', 'loss': 0, 
                              'max_queue_size': max_queue_size}
        wifiLinkConfig = {'bw': 10, 'delay': '6ms', 'loss': 3, 
                              'max_queue_size': max_queue_size}
        threeGeeLinkConfig = {'bw': 3, 'delay': '10ms', 'loss': 8, 
                              'max_queue_size': max_queue_size}
        
        # create hosts
        h1 = self.addHost('h1', **hostConfig)
        h2 = self.addHost('h2', **hostConfig)
        h3 = self.addHost('h3', **hostConfig)
        
        # create switches
        s1 = self.addSwitch('s1')
        s2 = self.addSwitch('s2')
        s3 = self.addSwitch('s3')
        s4 = self.addSwitch('s4')
        
        # wire h1 <-> s1 (ethernet)
        self.addLink(h1, s1, port1 = 0, port2 = 1, **ethernetLinkConfig)
        # wire s1 <-> s2 (ethernet)
        self.addLink(s1, s2, port1 = 2, port2 = 1, **ethernetLinkConfig)
        # wire s2 <-> s3 (ethernet)
        self.addLink(s2, s3, port1 = 2, port2 = 1, **ethernetLinkConfig)
        # wire s2 <-> s4 (ethernet)
        self.addLink(s2, s4, port1 = 3, port2 = 1, **ethernetLinkConfig)
        # wire s3 <-> h2 (wifi)
        self.addLink(s3, h2, port1 = 2, port2 = 0, **wifiLinkConfig)
        # wire s4 <-> h3 (3G)
        self.addLink(s4, h3, port1 = 2, port2 = 0, **threeGeeLinkConfig)
        
                