# Project 3
#
# This defines a DistanceVector (specialization of the Node class)
# that can run the Bellman-Ford algorithm. The TODOs are all related 
# to implementing BF. Students should modify this file as necessary,
# guided by the TODO comments and the assignment instructions. This 
# is the only file that needs to be modified to complete the project.
#
# Student code should NOT access the following members, otherwise they may violate
# the spirit of the project:
#
# topolink (parameter passed to initialization function)
# self.topology (link to the greater topology structure used for message passing)
#
# Copyright 2017 Michael D. Brown
# Based on prior work by Dave Lillethun, Sean Donovan, and Jeffrey Randow.
        											
from Node import *
from helpers import *

class DistanceVector(Node):
    
    def __init__(self, name, topolink, outgoing_links, incoming_links):
        ''' Constructor. This is run once when the DistanceVector object is
        created at the beginning of the simulation. Initializing data structure(s)
        specific to a DV node is done here.'''

        super(DistanceVector, self).__init__(name, topolink, outgoing_links, incoming_links)
        
        #TODO: Create any necessary data structure(s) to contain the Node's internal state / distance vector data
        # structure of table: {nodeName : [distance,path]}
        self.table = {}
        self.table[self.name] = 0
        self.paths = {}
        self.paths[self.name] = ""
        

    def send_initial_messages(self):
        ''' This is run once at the beginning of the simulation, after all
        DistanceVector objects are created and their links to each other are
        established, but before any of the rest of the simulation begins. You
        can have nodes send out their initial DV advertisements here. 

        Remember that links points to a list of Neighbor data structure.  Access
        the elements with .name or .weight '''

        # TODO - Each node needs to build a message and send it to each of its neighbors
        # HINT: Take a look at the skeleton methods provided for you in Node.py
        for neighbor in self.incoming_links:
            # structure of message: (self.name, self.table)
            self.send_msg((self.name, self.table, self.paths), neighbor.name)
        
        """ 
        Removed: not in BF algorithm
        for neighbor in self.outgoing_links:
            # question: also include parent node (last hop) information?
            self.table[neighbor.name] = int(neighbor.weight)        
        """

    def process_BF(self):
        ''' This is run continuously (repeatedly) during the simulation. DV
        messages from other nodes are received here, processed, and any new DV
        messages that need to be sent to other nodes as a result are sent. '''
        
        # Implement the Bellman-Ford algorithm here.  It must accomplish two tasks below:
        # TODO 1. Process queued messages 
        
        # flag: set to true if we have new things
        newStuff = False
        
        # for message from each source
        for message in self.messages:
            source = message[0]
            # if we don't have source in the table, add source to table
            if source not in self.table.keys():
                usToSource = self.get_outgoing_neighbor_weight(source)
                # THIS SHOULD NOT HAPPEN
                if usToSource == "Node Not Found":
                    usToSource = float("inf")
                else:
                    usToSource = int(usToSource)
                self.table[source] = usToSource
                self.paths[source] = source
                newStuff = True
            usToSource = self.table[source]
            # for each destination in the message's table
            for destination in message[1]:
                # if we are not the destination
                if destination != self.name:
                    sourceToDestination = message[1][destination]
                    # if we don't have the destination in the table, add it
                    if destination not in self.table.keys():
                        self.table[destination] = float("inf")
                        self.paths[destination] = ""
                        newStuff = True
                    usToDestination = self.table[destination]
                    tempPath = self.paths[source] + "," + message[2][destination]
                    if usToDestination > -99:
                        if usToSource == -99 and tempPath.split(",").count(destination) == 1:
                            self.table[destination] = -99
                            self.paths[destination] = tempPath
                            newStuff = True
                        elif sourceToDestination == -99:
                            self.table[destination] = -99
                            self.paths[destination] = tempPath
                            newStuff = True
                        elif usToSource + sourceToDestination < usToDestination and tempPath.split(",").count(destination) == 1:
                            self.table[destination] = max(-99, usToSource + sourceToDestination)
                            self.paths[destination] = tempPath
                            newStuff = True
                        elif self.get_outgoing_neighbor_weight(source) != "Node Not Found":
                            directToSource = int(self.get_outgoing_neighbor_weight(source))
                            tempPath = source + "," + message[2][destination]
                            if directToSource + sourceToDestination < usToDestination and tempPath.split(",").count(destination) == 1:
                                self.table[destination] = max(-99, directToSource + sourceToDestination)
                                self.paths[destination] = tempPath
                                newStuff = True
        
        
        # Empty queue
        self.messages = []    
        
        # TODO 2. Send neighbors updated distances  
        # if we made a change, send out messages
        if newStuff:
            for neighbor in self.neighbor_names:
                # structure of message: (self.name, self.table)
                self.send_msg((self.name, self.table, self.paths), neighbor)

    def log_distances(self):
        ''' This function is called immedately after process_BF each round.  It 
        prints distances to the console and the log file in the following format (no whitespace either end):
        
        A:A0,B1,C2
        
        Where:
        A is the node currently doing the logging (self),
        B and C are neighbors, with vector weights 1 and 2 respectively
        NOTE: A0 shows that the distance to self is 0 '''
        
        # TODO: Use the provided helper function add_entry() to accomplish this task (see helpers.py).
        # An example call that which prints the format example text above (hardcoded) is provided. 
        logStr = ""
        #for entry in sorted(self.table.keys()):
        for entry in sorted(self.table.keys()):
            if self.table[entry] < float('inf'):
                name = entry
                dist = self.table[entry]
                logStr += name + str(dist)
                # add a comma if it's not the last in the row
                #if entry != sorted(self.table.keys())[-1]:
                if entry != sorted(self.table.keys())[-1]:
                    logStr += ","
        add_entry(self.name, logStr)       