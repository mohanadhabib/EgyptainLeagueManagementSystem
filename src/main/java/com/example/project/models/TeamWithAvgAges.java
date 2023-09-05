package com.example.project.models;

public class TeamWithAvgAges {
    private int teamId;
    private String teamName;
    private float avgOfAges;

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

    public float getAvgOfAges() {
        return avgOfAges;
    }

    public void setAvgOfAges(float avgOfAges) {
        this.avgOfAges = avgOfAges;
    }
}
