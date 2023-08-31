package com.example.project.controllers;

import com.example.project.models.CaptainPlayer;
import com.example.project.models.Player;
import com.example.project.models.Team;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamsDatabaseController extends DatabaseOperations {
    @Override
    public void setCon() throws SQLException {
        super.setCon();
    }
    @Override
    public ResultSet getData(String sqlQuery) throws SQLException {
        return super.getData("select * from teams;");
    }

    @Override
    public ResultSet searchById(String sqlQuery,int id) throws SQLException {
        String sql = "select * from teams where id = " + id;
        return super.searchById(sql,id);
    }

    @Override
    public ResultSet searchByName(String sqlQuery, String name) throws SQLException {
        String sql = "select * from teams where name = " + name;
        return super.searchByName(sql,name);
    }

    public ArrayList<Team> getTeamsInfo() throws SQLException{
        ResultSet set = getData("select * from teams;");
        ArrayList<Team> teams = new ArrayList<>();
        while (set.next()){
            Team team = new Team();
            Player captain = new CaptainPlayer();
            team.setId(set.getInt(1));
            team.setName(set.getString(2));
            captain.setName(set.getString(3));
            team.setCaptain(captain);
            team.setTotalScore(set.getInt(4));
            teams.add(team);
        }
        return teams;
    }
    public Team getSearchedTeamById(int id) throws SQLException{
        String sql = "select * from teams where id = " + id;
        ResultSet set = searchById(sql,id);
        set.next();
        Team team = new Team();
        Player captain = new CaptainPlayer();
        team.setId(set.getInt(1));
        team.setName(set.getString(2));
        captain.setName(set.getString(3));
        team.setCaptain(captain);
        team.setTotalScore(set.getInt(4));
        return team;
    }
    public Team getSearchedTeamByName(String name) throws SQLException{
        name = "'"+name+"'";
        String sql = "select * from teams where name = " + name;
        ResultSet set = searchByName(sql,name);
        set.next();
        Team team = new Team();
        Player captain = new CaptainPlayer();
        team.setId(set.getInt(1));
        team.setName(set.getString(2));
        captain.setName(set.getString(3));
        team.setCaptain(captain);
        team.setTotalScore(set.getInt(4));
        return team;
    }
    public void updateData(int id, String param1, int param2) throws SQLException {
        String query = "update teams set captain = ? , score = ? where id = ?";
        PreparedStatement stmt = DatabaseOperations.con.prepareStatement(query);
        stmt.setString(1,param1);
        stmt.setInt(2,param2);
        stmt.setInt(3,id);
        stmt.executeQuery();
    }

    public void deleteData(int id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("delete from teams where id = ? ;");
        stmt.setInt(1,id);
        stmt.executeQuery();
    }
    public void addData(Team team) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("insert into teams (id, name, captain, score) values (?,?,?,?)");
        stmt.setInt(1,team.getId());
        stmt.setString(2,team.getName());
        stmt.setString(3,team.getCaptain().getName());
        stmt.setInt(4,team.getTotalScore());
        stmt.executeQuery();
    }
}
