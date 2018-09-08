package edu.gatech.seclass.sdpcryptogram.vo;

public class Attempt {

    private String username;
    private String puzzleName;
    private int attemptsMade;
    private int attemptsAllowed;
    private boolean solved;
    private String dateCompleted;

    public Attempt(String username, String puzzleName, int attemptsMade, int attemptsAllowed,
                   boolean solved, String dateCompleted) {
        this.username = username;
        this.puzzleName = puzzleName;
        this.attemptsMade = attemptsMade;
        this.attemptsAllowed = attemptsAllowed;
        this.solved = solved;
        this.dateCompleted = dateCompleted;
    }

    public void SetUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public String getPuzzleName() {
        return this.puzzleName;
    }

    public void setAttemptsMade(int attemptsMade) {
        this.attemptsMade = attemptsMade;
    }

    public String getAttemptsMade() {
        return String.valueOf(this.attemptsMade);
    }

    public void setAttemptsAllowed(int attemptsAllowed) {
        this.attemptsAllowed = attemptsAllowed;
    }

    public String getAttemptsAllowed() {
        return String.valueOf(this.attemptsAllowed);
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getSolved() {
        return String.valueOf(this.solved);
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getDateCompleted() {
        return this.dateCompleted;
    }

}
