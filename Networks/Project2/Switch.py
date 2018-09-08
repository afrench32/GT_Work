# Switch.py
# Robert Johns
# Project 2
#
# Copyright 2016 Michael Brown, updated by Kelly Parks
#           Based on prior work by Sean Donovan, 2015
			
from Message import *
from StpSwitch import *

class Switch(StpSwitch):

    def __init__(self, idNum, topolink, neighbors):    
        # Invoke the super class constructor, making the follwing available:
        # -self.switchID
        # -self.links
        super(Switch, self).__init__(idNum, topolink, neighbors)
	
	# initially, thinks it itself is the root
	self.root = self.switchID
	self.dist = 0
	
	# data structure for ST info: dictionary
	#      {neighborID:[inST,perceivedRoot,DistanceToPerceivedRoot,
        #                                onTheirSPtoRoot, onOurSPtoRoot]}
	self.treeInfo = {}
	#add neighbor init info to tree: [True, -1, -1, True, False] for all
	for neighbor in self.links:
	    self.treeInfo[neighbor] = [True, -1, -1, True, False]
	
    def send_initial_messages(self):
        # Messages are sent via the superclass method send_message(Message msg)
	
	for neighbor in self.links:
	    msg = Message(self.switchID, 0, self.switchID, neighbor, False)
	    self.send_message(msg)
	
        return
        
    def process_message(self, message):
	
	# flag: if the message has new information, set to true and push info to neighbors
	newInfo = False
	
	# if received root is less than our root, update self.root and self.dist
	if message.root < self.root:
	    newInfo = True
	    self.root = message.root
	    self.dist = message.distance + 1
	
	# update treeInfo based on message
	# if their root is different from what we thought, update
	if message.root != self.treeInfo[message.origin][1]:
	    self.treeInfo[message.origin][1] = message.root
	    newInfo = True
	# if their distance is different from what we thought, update
	if message.distance != self.treeInfo[message.origin][2]:
	    self.treeInfo[message.origin][2] = message.distance
	    newInfo = True
	# if necessary change onTheirSPtoRoot
	if message.pathThrough != self.treeInfo[message.origin][3]:
	    self.treeInfo[message.origin][3] = message.pathThrough
	    newInfo = True
	
	# update our spanning tree for each neighbor
	for neighbor in self.links:
	    # if we have the same root
	    if self.treeInfo[neighbor][1] == self.root:
		# if they are on our SP to root, keep/add to tree
		if self.treeInfo[neighbor][2] < self.dist:
		    self.treeInfo[neighbor][4] = True
		    self.treeInfo[neighbor][0] = True
		# if they are not on SP to root 
		else:
		    # if we are on theirs keep/add to tree
		    if self.treeInfo[neighbor][3] == True:
			self.treeInfo[neighbor][4] = False
			self.treeInfo[neighbor][0] = True
		    # otherwise, drop them from tree
		    else:
			self.treeInfo[neighbor][4] = False
			self.treeInfo[neighbor][0] = False
	
	# account for tiebreakers 
	spTies = []
	# get all neighbors on potential SP
	for neighbor in self.links:
	    if self.treeInfo[neighbor][4] == True:
		spTies.append(neighbor)
	# remove all but smallest ID from spanning tree
	if len(spTies) > 0:
	    spTies.sort()
	    spTies.remove(spTies[0])
	    for neighbor in spTies:
		self.treeInfo[neighbor][0] = False
		self.treeInfo[neighbor][4] = False
	
	# if we got new info, send updated info to all neighbors
	if newInfo == True:
	    for neighbor in self.links:
		outMsg = Message(self.root, self.dist, self.switchID, neighbor,
		                 self.treeInfo[neighbor][4])
		self.send_message(outMsg)
        
        return
        
    def generate_logstring(self):
        # This function needs to return a logstring for this particular switch.
	# The string represents the active forwarding links for this switch and 
	# is invoked only after the simulaton is complete.
	
	logString = ""
	first = True
	
	for neighbor in sorted(self.links):
	    if self.treeInfo[neighbor][0] == True:
		if not first:
		    logString += ", "
		logString += str(self.switchID) + " - " + str(neighbor)
		first = False
	
        return logString
