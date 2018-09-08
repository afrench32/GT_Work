# Design Information
## 1.	A user will be able to choose to log in as a specific player or create a new player when starting the application.  For simplicity, any authentication is optional, and you may assume there is a single system running the application.
 A Class called"Player" is added. It has operations Init(FirstName, LastName, Username, Email) to create new player or Login(Username) to log in as specific player. 


## 2.	The application will allow players to  (1) create a cryptogram, (2) choose a cryptogram to solve, (3) solve cryptograms, (4) view their list of completed cryptograms, and (5) view the list of cryptogram statistics.

A class "Cryptogram" is added. It utilizes operations Init(PuzzleName, Solution, EncodeParam, MaximumPlayNumber) to create new cryptogram. 
The "Player" class operation makeUnsolvedList() is used to list unsolved cryptograms. 
Operations in "Cryptograms" and "Play" could sovle the cryptograms.
"Player" class operation was used to makeCompletedList(), this operation is to view a list of completed cryptograms.
"Cryptogram" class was created to. It has the operation makeStatisticsList() and makeDetailedStatistics() to view the list of cryptogram statistics.


## 3.	A cryptogram will have an encoded phrase (encoded with a simple substitution cipher), a solution, and a maximum number of allowed solution Plays.  A cryptogram will only encode alphabetic characters, but may include other characters (such as punctuation or white space) in the puzzle, which will remain unaltered.  Capitalization is preserved when encoded.

In the "Cryptogram" class, attributes EncodedPhrase, Solution is the solution; MaximumPlayNumber is the maximum number of allowed solution Plays. 

## 4.	To add a player, the user will enter the following player information:
### a.	A first name
### b.	A last name
### c.	A unique username
### d.	An email

Four attributes was added in "Player" class.

## 5.	To add a new cryptogram, a player will:
### a.	Enter a unique cryptogram puzzle name.
The operation Init(PuzzleName, Solution, EncodeParam, MaximumPlayNumber) is added.
The attribute PuzzleName in "Cryptogram" class is added.

### b.	Enter a solution (unencoded) phrase.
Solution attribute was added in "Cryptogram" class. 

### c.	Choose different letters to pair with every letter present in the phrase.
Will be implemented in GUI.

### d.	View the encoded phrase.
To display the encoded phrase, operation ReturnEncodePhrase() in "Cryptogram" class is added.

### e.	Enter a maximum number of allowed solution Plays.
The attribute MaximumPlayNumber is added.

### f.	Edit any of the above steps as necessary.
Will be displayed in GUI.

### g.	Save the complete cryptogram.
To save the information. operation Init(PuzzleName, Solution, EncodeParam, MaximumPlayNumber) is added.

### h.	View a confirmation that the name assigned to the cryptogram is unique and return to the main menu.  
Display the return of the operation Init(PuzzleName, Solution, EncodeParam, MaximumPlayNumber).


## 6.	To choose and solve a cryptogram, a player will:
### a.	Choose a cryptogram from a list of all unsolved cryptograms.
The list of all unsolved ones are displayed by the operation makeUnsolvedList() in "Player" class to let user choose. 

### b.	View the chosen cryptogram and number of incorrect solution submissions (starting at 0 for a cryptogram that has not previously been Played).
The operation MakeUnsolvedList() will calculate the number of unsuccessful Plays by the player for each cryptogram. 

### c.	Match the replacement and encrypted letters together, and view the resulting potential solution.
The operation MatchEncodedPhrase() in "Cryptogram" class will return the resulting solution. 

### d.	When all letters in the puzzle are replaced and they are satisfied with the potential solution, submit their answer.
The operation Init(PuzzleName, Username, PlayDate, Answer) in "Play" class will generate an Play record with boolean IsSolved for recording if it is successful. 

### e.	Get a result indicating that the solution was successful, or incrementing the number of incorrect solution submissions if it was unsuccessful.
The operation Init(PuzzleName, Username, PlayDate, Answer) in "Play" class will generate a record with boolean IsSolved for recording. It will check the number of unsuccessful Plays by the player and compare with the attribute MaximumPlayNumber in "Cryptogram" class to determine if the cryptogram should be added to completed class and be removed from unsolved list. 
If the Play is successful, using operation AddNumberPlayerSolved() to add one to NumberPlayerSolved attribute and using operation AddSolvedPlayerUsername() to add the username to string array SolvedPlayerUsername attribute. Init(PuzzleName, Username, CompletedDate, IsSolved) operation in "Completed" class will add a new record in the class. 
If the number of Play reaches the MaximumPlayNumber attribute in "Cryptogram" class, it will also run Init(PuzzleName, Username, CompletedDate, IsSolved) operation in "Completed" class will add a new record in the class. 

### f.	At any point, the player may return to the list of unsolved cryptograms to try another.
The GUI will handle. The operation MakeUnsolvedList() in "Play" class can display the list. 

### g.	If the number of incorrect solution Plays increases to the maximum allowed number of solution Plays, they will get a result that the cryptogram game was lost, and this cryptogram will be moved to the completed list.
The operation Init(PuzzleName, Username, PlayDate, Answer) in "Play" class will generate an Play record with boolean IsSolved for recording if it is successful. It will check the number of unsuccessful Plays by the player and compare with the attribute MaximumPlayNumber in "Cryptogram" class to determine if the cryptogram should be added to completed class and be removed from unsolved list. 

## 7.	The list of unsolved cryptograms will show the cryptogram’s name and the number of incorrect solution Plays (if any) for that player.
The operation MakeUnsolvedList() in "Player" class can display the attributes PuzzleName in "Cryptogram" class and calculated number of unsuccessful Plays by the operation using "Play" class records. 

## 8.	The list of completed cryptograms for that player will show the cryptogram’s name, whether it was successfully solved, and the date it was completed.
In "Completed" class. The operation MakeCompletedList() in "Player" class was created to display the list with PuzzleName, IsSolved, and CompletedDate attributes.

## 9.	The list of cryptogram statistics will display a list of cryptograms in order of creation.  Choosing each cryptogram will display the date it was created, the number of players who have solved the cryptogram, and the username of the first three players to solve the cryptogram.
MakeStatisticsList() operation in "Cryptogram" class is functioned to display a list of all cryptograms. 
makeDetailedStatistics() operation will display attributes CreationDate, NumberPlayerSolved, and SolvedPlayerUsername. 

## 10.	The User Interface (UI) must be intuitive and responsive.
Will be implemented in GUI.

## 11.	The performance of the game should be such that players does not experience any considerable lag between their actions and the response of the game.
Will be implemented in GUI.
