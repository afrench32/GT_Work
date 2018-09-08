# Assignment 5
Robert Johns

## Structure Summary

* **Player**: contains data and operations for a player account for the cryptogram game.
* **Cryptogram**: contains data and operations for a cryptogram that a player can create or solve.
* **Attempted**: subclass of **Cryptogram**, represents a cryptogram that a player has attempted or solved.
* **ApplicationUI**: abstraction of user interface and related processes for the application.
* **CryptogramsDB**: abstraction of database used by application to store all cryptograms created by players.
* **PlayersDB**: abstraction of database used by application to store all player information and data.

## Requirements Handling

* **Requirement 1**

>A user will be able to choose to log in as a specific  player  or create a new player when starting the application". 

The functionalities to login to an account or create a new account will be handled by the user interface, be it graphical or text-based. Once an account is created or selected, an instance of the **Player** object will be loaded representing that player.

* **Requirement 2**

>The application will allow  players  to (1) create a cryptogram, (2) choose a cryptogram to solve, (3) solve cryptograms, (4) view their  list of completed cryptograms , and (5) view the  list of cryptogram statistics.

These capabilities are handled by operations within the **Player** class. In order:

(1) *createCryptogram()* will be called once the UI logic has obtained a valid cryptogram game from the player. The operation will add a new Cryptogram to **CryptogramsDB**

(2) *viewUnsolvedCryptograms()* will display a list of the cryptogram, with accompanying statistics, that the player has not solved or maxed out their attempts. This will be accomplished with a call to **CryptogramsDB**, which will check what it prints against the username (to ensure no cryptogram the user has created is displayed) and against the user attribute *attemptedCryptograms*, a list of Cryptograms the user has attempted (to ensure no solved or maxed out cryptograms are displayed)

(3) *solveCryptogram()* will obtain a cryptogram from **CryptogramsDB**. Once the cryptogram is obtained, the UI will take the user through a solving process, allowing editing of solution before submitting to check against correct answer.

(4) *viewCompletedCryptograms()* functions much like **displayUnsolvedCryptograms()**, but will only display cryptograms the user has solved or maxed out their attempts, using *attemptedCryptograms* and not **CryptogramsDB**.

(5) *viewCryptogramStatistics()* will call **CryptogramsDB** to display the created cryptograms, in the format described in a later requirement.

* **Requirement 3**

>A cryptogram will have an encoded phrase (encoded with a simple substitution cipher), a solution, and a maximum number of allowed solution attempts. A cryptogram will only encode alphabetic characters, but may include other characters (such as punctuation or white space) in the puzzle, which will remain unaltered. Capitalization is preserved when encoded.

These attributes are handed with attributes in the **Cryptogram** object. Those attributes are (in order given in the requirement): *cypher*, *solution*, and *maxAttempts*. The encoded phrase itself is not stored, but is generated using *solution* and *cypher* (a Map object mapping alphabetic characters to alphabetic characters). The displaying of the encoded phrase, and ensuring of valid cryptogram attributes, will be handled by the UI when **createCryptogram()** is called by a player.

* **Requirement 4**

>To add a player, the user will enter the following player information: a. A first name b. A last name c. A unique username d. An email

These are given as attributes in the **Player** object. In order, those attributes are: *firstName*, *lastName*, *userName*, and *email*. The obtaining of these attributes is handled by the UI. Uniqueness of username will be checked by a call to **PlayersDB**.

* **Requirement 5**

>To add a new cryptogram, a player will:
a. Enter a unique cryptogram puzzle name.
b. Enter a solution (unencoded) phrase.
c. Choose different letters to pair with every letter present in the phrase.
d. View the encoded phrase.
e. Enter a maximum number of allowed solution attempts.
f. Edit any of the above steps as necessary.
g. Save the complete cryptogram.
h. View a confirmation that the name assigned to the cryptogram is unique and
return to the main menu.

The bulk of these operations will be handled by the UI exclusively. A **Cryptogram** object will only be created once the user has entered, edited, and saved all of the information. The uniqueness of the *puzzleName* attribute will be ensured by a call to **CryptogramsDB**. 
 
* **Requirement 6**

>To choose and solve a cryptogram, a player will:
a. Choose a cryptogram from a list of all unsolved cryptograms.
b. View the chosen cryptogram and number of incorrect solution submissions
(starting at 0 for a cryptogram that has not previously been attempted).
c. Match the replacement and encrypted letters together, and view the resulting
potential solution.
d. When all letters in the puzzle are replaced and they are satisfied with the
potential solution, submit their answer.
e. Get a result indicating that the solution was successful, or incrementing the
number of incorrect solution submissions if it was unsuccessful.
f. At any point, the player may return to the list of unsolved cryptograms to try
another.
g. If the number of incorrect solution attempts increases to the maximum allowed
number of solution attempts, they will get a result that the cryptogram game was lost, and this cryptogram will be moved to the completed list.

These requirements are handled by different operations in **Player** (described under Requirement 2): *displayUnsolvedCryptograms()* will handle (a); *viewCryptogram()* will handle (b); and *solveCryptogram()* will handle the rest. The bulk of functionality in *solveCryptogram()* will be handled by the UI; **CryptogramsDB** will only be accessed to get the cryptogram initially and check the solution. To increment the attempts count or mark a puzzle as solved, **PlayersDB** and **CryptogramsDB** will be accessed to edit information in the *attemptedCryptograms* data structure for that player.

* **Requirement 7**

> The list of unsolved cryptograms will show the cryptogram’s name and the number of incorrect solution attempts (if any) for that player.

As described under Requirement 2, this functionality will be handled by the *displayUnsolvedCryptograms()* operation under **Player**. All data displayed will be kept in an instance of **Attempted** for that puzzle, stored in the *attemptedCryptograms* data structure. This operation will only display cryptograms that are not completed; a check against **Attempted**'s *solved* attribute, and of *currAttempts* and the parent attribute *maxAttempts* to determine if the player has completed the cryptogram.

* **Requirement 8**

>The list of completed cryptograms for that player will show the cryptogram’s name, whether it was successfully solved, and the date it was completed.

As described under Requirement 2, this functionality will be handled by the *viewCompletedCryptograms()* operation under **Player**. All data displayed will be kept in an instance of **Attempted** for that puzzle, stored in the *attemptedCryptograms* data structure. This operation will only display cryptograms that are completed; a check against **Attempted**'s *solved* attribute, and of *currAttempts* and the parent attribute *maxAttempts* to determine if the player has completed the cryptogram.

* **Requirement 9**

>The list of cryptogram statistics will display a list of cryptograms in order of creation. Choosing each cryptogram will display the date it was created, the number of players who have solved the cryptogram, and the username of the first three players to solve the cryptogram.

As described in Requirement 2, this requirement will be handled by the function *viewCryptogramStatistics()*, which performs a call to **CryptogramsDB**; which will print out all of the required pieces of information (which are attributes of **Cryptogram**) as desired.

* **Requirement 10**

>The User Interface (UI) must be intuitive and responsive.

This requirement is outside of the purview of this project, and is handled entirely by the UI.

* **Requirement 11**

>The performance of the game should be such that players does not experience any
considerable lag between their actions and the response of the game.

This requirement is outside of the purview of this project, and is handled entirely by the business logic, netcode, and other, lower-level aspects of the application.

## changelog
* 15-Jun-2018 created