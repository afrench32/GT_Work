# Design Information
## 1.	A user will be able to choose to log in as a specific player or create a new player when starting the application.  For simplicity, any authentication is optional, and you may assume there is a single system running the application.

A class "Player" is added. It has operations Init(FirstName, LastName, Username, Email) to create new player and Login(Username) to log in as specific player. 


## 2.	The application will allow players to  (1) create a cryptogram, (2) choose a cryptogram to solve, (3) solve cryptograms, (4) view their list of completed cryptograms, and (5) view the list of cryptogram statistics.

A class "Cryptogram" is added. It uses operations Init(PuzzleName, Solution, EncodeParam, MaximumAttemptNumber) to create new cryptogram. 
It uses the "Player" class operation GenerateUnsolvedList() to list unsolved cryptograms. 
Operations in "Cryptograms" and "Attempt" can solve the cryptograms.
It uses the "Player" class operation GenerateCompletedList() to view list of completed cryptograms.
It uses "Cryptogram" class operation GenerateStatisticsList() and GenerateDetailedStatistics() to view the list of cryptogram statistics.


## 3.	A cryptogram will have an encoded phrase (encoded with a simple substitution cipher), a solution, and a maximum number of allowed solution attempts.  A cryptogram will only encode alphabetic characters, but may include other characters (such as punctuation or white space) in the puzzle, which will remain unaltered.  Capitalization is preserved when encoded.

In the "Cryptogram" class, attributes EncodedPhrase is the encoded phrase; Solution is the solution; MaximumAttemptNumber is the maximum number of allowed solution attempts. 

## 4.	To add a player, the user will enter the following player information:
### a.	A first name
### b.	A last name
### c.	A unique username
### d.	An email

There are four attributes in "Player" class according to these requirements. 

## 5.	To add a new cryptogram, a player will:
### a.	Enter a unique cryptogram puzzle name.
The operation Init(PuzzleName, Solution, EncodeParam, MaximumAttemptNumber) is added for requirements a-g.
The attribute PuzzleName in "Cryptogram" class is for this requirement. 

### b.	Enter a solution (unencoded) phrase.
Solution attribute in "Cryptogram" class. 

### c.	Choose different letters to pair with every letter present in the phrase.
Will be handled in GUI.

### d.	View the encoded phrase.
The operation ReturnEncodePhrase() in "Cryptogram" class is added to display the encoded phrase.

### e.	Enter a maximum number of allowed solution attempts.
The attribute MaximumAttemptNumber is for this requirement.

### f.	Edit any of the above steps as necessary.
Will be handled in GUI.

### g.	Save the complete cryptogram.
The operation Init(PuzzleName, Solution, EncodeParam, MaximumAttemptNumber) is added to save the information.

### h.	View a confirmation that the name assigned to the cryptogram is unique and return to the main menu.  
Display the return of the operation Init(PuzzleName, Solution, EncodeParam, MaximumAttemptNumber) to view.


## 6.	To choose and solve a cryptogram, a player will:
### a.	Choose a cryptogram from a list of all unsolved cryptograms.
Display the list of all unsolved ones by the operation GenerateUnsolvedList() in "Player" class to let user choose. 

### b.	View the chosen cryptogram and number of incorrect solution submissions (starting at 0 for a cryptogram that has not previously been attempted).
The operation GenerateUnsolvedList() will calculate the number of unsuccessful attempts by the player for each cryptogram. 

### c.	Match the replacement and encrypted letters together, and view the resulting potential solution.
The operation MatchEncodedPhrase() in "Cryptogram" class will return the resulting solution. 

### d.	When all letters in the puzzle are replaced and they are satisfied with the potential solution, submit their answer.
The operation Init(PuzzleName, Username, AttemptDate, Answer) in "Attempt" class will generate an attempt record with boolean IsSolved for recording if it is successful. 

### e.	Get a result indicating that the solution was successful, or incrementing the number of incorrect solution submissions if it was unsuccessful.
The operation Init(PuzzleName, Username, AttemptDate, Answer) in "Attempt" class will generate an attempt record with boolean IsSolved for recording. It will check the number of unsuccessful attempts by the player and compare with the attribute MaximumAttemptNumber in "Cryptogram" class to determine if the cryptogram should be added to completed class and be removed from unsolved list. 
If the attempt is successful, using operation AddNumberPlayerSolved() to add one to NumberPlayerSolved attribute and using operation AddSolvedPlayerUsername() to add the username to string array SolvedPlayerUsername attribute. Init(PuzzleName, Username, CompletedDate, IsSolved) operation in "Completed" class will add a new record in the class. 
If the number of attempt reaches the MaximumAttemptNumber attribute in "Cryptogram" class, it will also run Init(PuzzleName, Username, CompletedDate, IsSolved) operation in "Completed" class will add a new record in the class. 

### f.	At any point, the player may return to the list of unsolved cryptograms to try another.
The GUI will handle. The operation GenerateUnsolvedList() in "player" class can display the list. 

### g.	If the number of incorrect solution attempts increases to the maximum allowed number of solution attempts, they will get a result that the cryptogram game was lost, and this cryptogram will be moved to the completed list.
The operation Init(PuzzleName, Username, AttemptDate, Answer) in "Attempt" class will generate an attempt record with boolean IsSolved for recording if it is successful. It will check the number of unsuccessful attempts by the player and compare with the attribute MaximumAttemptNumber in "Cryptogram" class to determine if the cryptogram should be added to completed class and be removed from unsolved list. 

## 7.	The list of unsolved cryptograms will show the cryptogram’s name and the number of incorrect solution attempts (if any) for that player.
The operation GenerateUnsolvedList() in "Player" class can display the attributes PuzzleName in "Cryptogram" class and calculated number of unsuccessful attempts by the operation using "Attempt" class records. 

## 8.	The list of completed cryptograms for that player will show the cryptogram’s name, whether it was successfully solved, and the date it was completed.
The operation GenerateCompletedList() in "Player" class can display the list with PuzzleName, IsSolved, and CompletedDate attributes in "Completed" class. 

## 9.	The list of cryptogram statistics will display a list of cryptograms in order of creation.  Choosing each cryptogram will display the date it was created, the number of players who have solved the cryptogram, and the username of the first three players to solve the cryptogram.
GenerateStatisticsList() operation in "Cryptogram" class will be run to display the list of all cryptograms. 
GenerateDetailedStatistics() operation will display attributes CreationDate, NumberPlayerSolved, and SolvedPlayerUsername. 

## 10.	The User Interface (UI) must be intuitive and responsive.
This is not represented in my design, as it will be handled entirely within the GUI implementation.

## 11.	The performance of the game should be such that players does not experience any considerable lag between their actions and the response of the game.
This is not represented in my design, as it will be handled entirely within the GUI implementation.

