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

    @Override
    public ResultSet searchByDate(String sqlQuery, Date date) throws SQLException {
        String sql = "select matches.id , matches.date , matches.referee , matches.score , matches.stadium , teams.name  from matches \n" +
                "inner join teammatch \n" +
                "on matches.id = teammatch.matchId \n" +
                "inner join teams \n" +
                "on teams.id = teammatch.teamId " +
                "where date = "+ "'" + date + "'";
        return super.searchByDate(sql, date);
    }
    public ArrayList<Match> getSearch(Date date) throws SQLException{
        String sql = "select matches.id , matches.date , matches.referee , matches.score , matches.stadium , teams.name  from matches \n" +
                "inner join teammatch \n" +
                "on matches.id = teammatch.matchId \n" +
                "inner join teams \n" +
                "on teams.id = teammatch.teamId " +
                "where date = "+ "'" + date + "'";
        ResultSet set = searchByDate(sql,date);
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

    public void addMatch(int id , Date date , String referee , String score , String stadium , int firstTeamId, int secondTeamId) throws SQLException{
        PreparedStatement stmt1 = con.prepareStatement("insert into matches(id, date, referee, score, stadium) values (?,?,?,?,?);");
        stmt1.setInt(1,id);
        stmt1.setDate(2,date);
        stmt1.setString(3,referee);
        stmt1.setString(4,score);
        stmt1.setString(5,stadium);
        stmt1.executeQuery();
        PreparedStatement stmt2 = con.prepareStatement("insert into teammatch (teamId, matchId) values (?,?);");
        stmt2.setInt(1,firstTeamId);
        stmt2.setInt(2,id);
        stmt2.executeQuery();
        PreparedStatement stmt3 = con.prepareStatement("insert into teammatch (teamId, matchId) values (?,?);");
        stmt3.setInt(1,secondTeamId);
        stmt3.setInt(2,id);
        stmt3.executeQuery();
    }
}