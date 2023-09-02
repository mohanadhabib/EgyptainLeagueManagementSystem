package com.example.project.controllers;
import com.example.project.models.CaptainPlayer;
import com.example.project.models.Match;
import com.example.project.models.Player;
import com.example.project.models.Team;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MatchesDatabaseController extends DatabaseOperations{
    @Override
    public void setCon() throws SQLException {
        super.setCon();
    }
    @Override
    public ResultSet getData(String sqlQuery) throws SQLException {
        String sql = "select matches.id , matches.date , matches.referee , matches.score , matches.stadium , teams.name  from matches \n" +
                "inner join teammatch \n" +
                "on matches.id = teammatch.matchId \n" +
                "inner join teams \n" +
                "on teams.id = teammatch.teamId;";
        return super.getData(sql);
    }
    @Override
    public ResultSet searchByName(String sqlQuery, String name) throws SQLException {
        String sql = "select * from teams where name = " + name;
        return super.searchByName(sql,name);
    }
    public Team getSearchedTeam(String sqlQuery, String name) throws SQLException{
        String sql = "select * from teams where name = " + name;
        ResultSet set = searchByName(sql,name);
        Team team = new Team();
        Player captain = new CaptainPlayer();
        team.setId(set.getInt(1));
        team.setName(set.getString(2));
        captain.setName(set.getString(3));
        team.setCaptain(captain);
        team.setTotalScore(set.getInt(4));
        return team;
    }

    public ArrayList<Match> getTeamMatches(int id) throws SQLException{
        String sql = "select teams.id , teams.name , matches.id , matches.date , matches.referee , matches.score , matches.stadium from matches \n" +
                "inner join teammatch \n " +
                "on matches.id = teammatch.matchId \n " +
                "inner join teams \n " +
                "on teams.id = teammatch.teamId \n " +
                "where teams.id = "+id;
        ArrayList<Match> matches = new ArrayList<>();
        ResultSet set = con.createStatement().executeQuery(sql);
        while (set.next()){
            Match match = new Match();
            Team team = new Team();
            team.setId(set.getInt(1));
            team.setName(set.getString(2));
            match.setFirstTeam(team);
            match.setId(set.getInt(3));
            match.setDate(set.getDate(4));
            match.setFootballReferee(set.getString(5));
            match.setScore(set.getString(6));
            match.setStadiumName(set.getString(7));
            matches.add(match);
        }
        return matches;
    }
    public ArrayList<Match> getMatchesInfo() throws SQLException{
        String sql = "select matches.id , matches.date , matches.referee , matches.score , matches.stadium , teams.name  from matches \n" +
                "inner join teammatch \n" +
                "on matches.id = teammatch.matchId \n" +
                "inner join teams \n" +
                "on teams.id = teammatch.teamId;";
        ResultSet set = getData(sql);
        ArrayList<Match> matches = new ArrayList<>();
        Match match = new Match();
        int count = 1;
        while (set.next()){
            if(count % 2 != 0 ){
                match = new Match();
                match.setId(set.getInt(1));
                match.setDate(set.getDate(2));
                match.setFootballReferee(set.getString(3));
                match.setScore(set.getString(4));
                match.setStadiumName(set.getString(5));
                Team firstTeam = new Team();
                firstTeam.setName(set.getString(6));
                match.setFirstTeam(firstTeam);
            }
            else if(count % 2 == 0){
                Team secondTeam = new Team();
                secondTeam.setName(set.getString(6));
                match.setSecondTeam(secondTeam);
                matches.add(match);
            }
            count++;
        }
        return matches;
    }
    public void deleteData(int id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("delete from matches where id = ? ;");
        stmt.setInt(1,id);
        stmt.executeQuery();
        PreparedStatement stmt2 = con.prepareStatement("delete from teammatch where matchId = ? ;");
        stmt2.setInt(1,id);
        stmt2.executeQuery();
    }
    public void updateData(int id, Date date, String referee , String stadium) throws SQLException {
        String query = "update matches set date = ? , referee = ? , stadium = ? where id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setDate(1,date);
        stmt.setString(2,referee);
        stmt.setString(3,stadium);
        stmt.setInt(4,id);
        stmt.executeQuery();
    }
}