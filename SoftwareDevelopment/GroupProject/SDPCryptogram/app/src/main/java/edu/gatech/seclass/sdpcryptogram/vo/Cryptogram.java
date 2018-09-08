package edu.gatech.seclass.sdpcryptogram.vo;

/**
 * Created by a0s01bo on 05/07/18.
 */

public class Cryptogram {

    private String encoded;
    private String decoded;
    private String createdBy;
    private String puzzleName;
    private String attemptsAllowed;
    private String dateCreated;

    public Cryptogram(String encoded, String decoded, String createdBy, String puzzleName, String attemptsAllowed, String dateCreated) {
        this.encoded = encoded;
        this.decoded = decoded;
        this.createdBy = createdBy;
        this.puzzleName = puzzleName;
        this.attemptsAllowed = attemptsAllowed;
        this.dateCreated = dateCreated;
    }

    public String getEncoded() {
        return this.encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getDecoded() {
        return this.decoded;
    }

    public void setDecoded(String decoded) {
        this.decoded = decoded;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPuzzleName() {
        return this.puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public String getAttemptsAllowed() {
        return this.attemptsAllowed;
    }

    public void setAttemptsAllowed(String attemptsAllowed) {
        this.attemptsAllowed = attemptsAllowed;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
