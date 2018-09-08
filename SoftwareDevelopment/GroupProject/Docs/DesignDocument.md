# Design Document

**Author**: Team 13 

| Version |Description      |Date         |
|---------|-----------------|-------------|
| V1      | Initial version |28 June 2018|

## 1 Design Considerations

### 1.1 Assumptions
1. The user will interface with the system via touchscreen.
2. At the start, the users will always choose to log in as a 
player or create a new player.
3. The system will have adequate storage for the cryptogram's data.
4. The system will independently on one smart phone. 

### 1.2 Constraints

1. The system will be implemented with the Java programming language.
2. The system will run on an Android smart phone.
3. User input will be limited to the touchscreen interface.

### 1.3 System Environment

The system must operate in the Android environment. The system libraries will be used for displaying the user interface, responding to user input, and performing calculations necessary for encoding the cryptograms. 

## 2 Architectural Design
The overall architectural style will be an Event-Driven design. The 
system will respond to user input through the GUI. The cryptograms data and players data will accessed through CryptogramsDB and PlayersDB interfaces, respectively, either locally or remotely and as such will utilize a client-server design. 

### 2.1 Component Diagram
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/ComponentDiagram.png)

### 2.2 Deployment Diagram
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/DeploymentDiagram.png)

## 3 Low-Level Design
The software components of the system will consist at a low level of classes show in the following UML diagram: the Login Server component is implemented by Player and PlayersDB classes; the Application Server component is implemented by Cryptogram, Attempted, and Completed classes; Database Server compoment is implemented by CryptogramsDB class. 

### 3.1 Class Diagram
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/team-design.png)

### 3.2 Other Diagrams
Sequence diagram for a player attempt to solve a cryptogram.

![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/SequenceDiagram.png)

## 4 User Interface Design
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/login.png)
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/choose_list.png)
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/list_unsolved.png)
![component](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/view_puzzle.png)
