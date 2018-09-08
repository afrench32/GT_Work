# User Manual

**NAME**

A Cryptogram

**SYNOPSIS**

This application is a cryptogram game. This app has five major functions including creating a crypotogram, choosing a cryptogram to solove, solving cryptograms, viewing their list of completed cyrptograms, and viewing the list of cryptogram statistics. 

**DESCRIPTION AND DEMO**

![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/LandingPage.png)

This is the first log in screen displayed by the app. Clicking the "Login" as a specific user, or clicking "Add a new user" if the user had not be added to the database. If the user wants to be added to this game, he or she is required to input his/her first name, last name, a unique user name, and an email.

Clicking "LOGIN" will open the following screen:
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/login.png)

Clicking "CREATE ACCOUNT" will open the following screen:
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/CreateAccount.png)

After login or create a new account, the main menu page is shown as: 
![choose_list](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/choose_list.png)

At this main menu, the user can create a new puzzle, solve a puzzle made by other users, view different kinds of lists (incompleted puzzles, completed puzzles, and statistics), or logout. 
Clicking the "CREATE A CRYPTOGRAM" button will show the following screen:
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/CreatePuzzle.png)

A unique puzzle name, a solution, and the maximum number of allowed attempts are required. 
(1) By Clicking the "RAMDOMIZE CYPHER" button, the puzzle is made by ramdomly cyphering. The encoded puzzle will be shown above the "Attemps allowed" box. 
(2) The user can change the solution and click the "RE-ENCODE SOLUTION" button to change the cyphered puzzle to update with the new solution. 
(3) The user can also manually change the corresponding letters in the list. If a non-exist letter is edited, a warning message will remind user to avoid. After clicking "ENTER CUSTOM CYPHER" button, the encoded puzzle will appear.



At main menu, by clicking "SOLVE A CRYPTOGRAM" will show the following screen:
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/SolvePuzzleName.png)
A special feature in the app is the ability to remind the user when they enter a puzzle name which is created by themselves by message "Puzzle created by you!". When they enter some non-exist puzzle names, the app will remind them by message "Puzzle does not exists!".
When the user enter a puzzle name and click "SOLVE!" button, it shows the following screen:
![view_puzzle](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/mockup/view_puzzle.png)

The user needs to enter the corresponding letters and click "USE CYPHER" button to view the potential solution which is decoded by the user manual input. The the user upsets about the attempt, he/she can click "RESET CYPHER" to reset the inputs. After the user satisfies about the potential solution, by clicking "ENTER SOLUTION", the app will check if the answer is correct. If the answer is right, the puzzle is solved and the main menu will be shown. If the answer is wrong, the number of attempts will increases until reaching the allowed number. A message "Not correct! Give another try!" will be shown. When there is no more attempts allowed, a message "Sorry! You are out of attempts!" will be shown and the puzzle will be added to completed list. The main menu screen will appear. 
A success attempt will lead to the following screen:
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/Success.png)

At main menu, when the "VIEW CRYPTOGRAMS" button is click, it shows the following screen:
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/ViewLists.png)

The "VIEW INCOMPLETE CRYPTOGRAM" button will lead to a list of incompleted puzzles created by other users. The number of allowed attemps will also be shown along with the puzzle names. 
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/ViewIncomplete.png)

The "VIEW COMPLETE CRYPTOGRAM" button will lead to a list of completed puzzles done by the user. The status of success or fail of solving and the completed time will also be shown for each completed puzzle. 
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/ViewComplete.png)


The "VIEW CRYPTOGRAM STATISTICS" button will lead to a list of puzzles' statistics. They will be ranked by the time of creation. The number of solved will also be shown. The first three users who solved each puzzle will also be shown.  
![front_page](https://github.gatech.edu/gt-omscs-se-2018summer/6300Summer18Team13/blob/master/GroupProject/Docs/screenshots/Statistics.png)

The "CANCEL" button leads back to viewing the different lists. The "MAIN MENU" button return to main menu. The "LOG OUT" button log-outs the player's account and shows the starting screen of the app. 


**AUTHORS**

- Robert Johns
- Ayush Seth
- Zhen Tan
- Xupin Zhang

**REPORTING BUGS**

  Report bugs and suggestions to the GA Tech Computer Science Department attention: *OMSCS Summer 2018 CS 6300 Team 13.*

**COPYRIGHT**

  Copyright GA Tech 2018.



