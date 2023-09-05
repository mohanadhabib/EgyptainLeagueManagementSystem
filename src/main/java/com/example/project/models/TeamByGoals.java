package com.example.project.models;

public class TeamByGoals {
    private int teamId;
    private String teamName;
    private int scoreByGoals;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getScoreByGoals() {
        return scoreByGoals;
    }

    public void setScoreByGoals(int scoreByGoals) {
        this.scoreByGoals = scoreByGoals;
    }
}
