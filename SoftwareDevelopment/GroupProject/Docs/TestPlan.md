# Test Plan

**Author**: Team 13 (Robert, Zhen, Xupin, Ayush)

| Version |Description      |Date         |
|---------|-----------------|-------------|
| V1      | Initial version ||
| V2      | Final version  | July 13 |
## 1 Testing Strategy

### 1.1 Overall strategy

#### 1.1.1 System Testing Strategy

System testing will be performed on the complete and integrated system to make sure that it meets the specified requirements. We will discover any inconsistencies between the interaction of modules. We will conduct system tests by exercising each use case on an Android emulator or actual hardware.

System test cases will be prepared and executed by User.

#### 1.1.2 Regression Testing Strategy

Regression testing will be conducted to make sure that the new system still performs as it did previously after new changes are incorporated. In addition to testing the new code, other modules will also be tested to insure the changes did not affect prior functionality.

Regression test cases will be prepared and executed by User.

### 1.2 Test Selection

Category-partition method or model-based testing from Blackbox testing may be used as the main methods in each testing levels mentioned above. While white-box techniques like branch coverage and condition coverage might also be used to make sure that every situation has been considered.

### 1.3 Adequacy Criterion

Test cases will be attempted to cover all possible scenarios specified in the use cases and all functional requirements.

### 1.4 Bug Tracking

Bugs and enhancement will be reported to team members and discussed as a group. GitHub issues will be used to track the bugs and enhancement requests as necessary. Detailed discussion will be conducted on the Zoom room or through email.

### 1.5 Technology

JUnit is used to build the test cases and Espresso testing framework is used to build UI tests.

End user system tests will be conducted manually by following the procedures prescribed in the applicable test cases.


## 2 Test Cases

|Case #| Name/Purpose | Steps  |  Expected Result  |  Actual Result  |  Pass/Fail   | Additional Information |
|-----|:------------ |:--------------- |:--------------- |:------------ |:--------------- | :--------------- |
|1| Add a new player | Click "Add new player" button. Input a player's first name, last name, username and email address. | new player is created.  | As expected | Pass |  |
|2| Log in as a player | Input existing player's username. Click log in button. | Log in as a existing player. |  As expected | Pass  |  |
|3| Create a cryptogram | Click "Create a cryptogram" button. Input a unique puzzle name, a solution phrase. Choose different letters to pair with letters in the puzzle.  | The encoded phrase is displayed to view. |  As expected | Pass  |  |
|4| Enter the maximum number of allowed attempts | Enter the maximum number of allowed attempts into empty space. | No change is expected. |  As expected | Pass  |  |
|5| Save the new cryptogram | Click the "Save" button. | The new cryptogram is added . A confirmation message is shown. |  As expected | Pass |  |
|6| Only encode alphabetic characters |  After choosing to solve a puzzle from the list of unsolved cryptograms, view the encoded phrase. | The space and punctuation should be kept as same. | As expected | Pass | |
|7| View the list of unsolved cryptograms | Click the "List of unsolved cryptograms" after logging in. | A list of unsolved puzzle's name and the number of incorrect solution attempts (if any) for that player are shown. |  As expected | Pass  |  |
|8| Choose a cryptogram to solve | Select a puzzle from viewing the list of unsolved   cryptogram page. | Chosen cryptogram and number of incorrect solution submissions (starting at 0 for a cryptogram that has not previously been attempted) are shown. |  As expected | Pass  |  |
|9| Attempt to solve a cryptogram | After viewing the cryptogram and number of incorrect solution submissions, click the "Attempt" button. Matching the replacement and encrypted letters together.| The resulting potential solution is displayed.  | As expected | Pass  |  |
|10| Submit the solution | After viewing the potential solution, click the "Submit" button. | Displaying a result message showing that the solution was successful, or incrementing the number of incorrect solution submissions if it was unsuccessful. | As expected | Pass | |
|11| Reach the maximum allowed attempts | Submitting a wrong solution and reach the allowed maximum allowed attempt for that puzzle. |a message is showing that the cryptogram game was lost, and this cryptogram will be moved to the completed list | As expected | Pass
|12| View the list of Completed cryptograms | Click the "List of completed cryptograms" after logging in. | A list of unsolved puzzle's name, whether it was successfully solved, and the date it was completed. |  As expected | Pass  |  |
|13| View the list of cryptogram statistics | Click the "List of cryptogram statistics" after logging in. | A list of cryptogram names is shown in order of creation. |  As expected | Pass  |  |
|14| View the statistics of a chosen cryptogram | After viewing the list of statistics, click one of the names. | Displaying the date it was created, the number of players who have solved the cryptogram, and the username of the first three players to solve the cryptogram. |  As expected | Pass  |  |
|15| Return to the list of unsolved cryptogram during solving a puzzle | During the solving of a puzzle, click the "List of unsolved cryptogram" button. | A list of unsolved puzzle's name and the number of incorrect solution attempts (if any) for that player are shown. |  As expected | Pass  |  |