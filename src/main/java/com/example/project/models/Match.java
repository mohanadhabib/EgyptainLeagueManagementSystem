package com.example.project.models;

import java.sql.Date;

public class Match {
    private int id;
    private Date date;
    private Team firstTeam;
    private Team secondTeam;
    private String footballReferee;
    private String score;
    private String stadiumName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Team getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(Team firstTeam) {
        this.firstTeam = firstTeam;
    }

    public Team getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(Team secondTeam) {
        this.secondTeam = secondTeam;
    }

    public String getFootballReferee() {
        return footballReferee;
    }

    public void setFootballReferee(String footballReferee) {
        this.footballReferee = footballReferee;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }
}
