package edu.gatech.seclass.sdpcryptogram.vo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by a0s01bo on 05/07/18.
 */

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String userName;
    private String emailId;
    private String attemptedCryptograms;
    private String completedCryptogram;

    public User(String firstName, String lastName, String userName, String emailId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.emailId = emailId;

    }

    public User(String firstName, String lastName, String userName, String emailId, String attemptedCryptograms, String completedCryptogram) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.emailId = emailId;
        this.attemptedCryptograms = attemptedCryptograms;
        this.completedCryptogram = completedCryptogram;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAttemptedCryptograms() {
        return attemptedCryptograms;
    }

    public void setAttemptedCryptograms(String attemptedCryptograms) {
        this.attemptedCryptograms = attemptedCryptograms;
    }

    public String getCompletedCryptogram() {
        return completedCryptogram;
    }

    public void setCompletedCryptogram(String completedCryptogram) {
        this.completedCryptogram = completedCryptogram;
    }

    //TODO For now we will be storing ',' comma separated puzzle names in attempted and completed cryptograms.
    public static List<String> getCryptogramFromDb(String rawTextfromDb){
        List<String> result = Arrays.asList(rawTextfromDb.split("\\s*,\\s*"));
        return result;
    }

    public static String ListToString(List<String> stringList){
        StringBuilder result=new StringBuilder();
        for(String string:stringList){
            result.append(string);
            result.append(",");
        }
        return result.toString();
    }
}
