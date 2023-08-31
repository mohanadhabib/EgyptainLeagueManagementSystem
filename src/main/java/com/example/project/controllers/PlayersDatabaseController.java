package com.example.project.controllers;

import com.example.project.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayersDatabaseController extends DatabaseOperations{
    @Override
    public void setCon() throws SQLException {
        super.setCon();
    }
    @Override
    public ResultSet getData(String sqlQuery) throws SQLException {
        return super.getData("select players.name,num,age,type,players.score,rank,teamId,teams.name \n" +
                "from teams inner join players\n" +
                "on teams.id = players.teamId;");
    }
    @Override
    public ResultSet searchById(String sqlQuery,int id) throws SQLException {
        String sql = "select players.name,num,age,type,players.score,rank,teamId,teams.name from\n" +
                     "teams inner join players\n" +
                     "on teams.id = players.teamId where id = "+id;
        return super.searchById(sql,id);
    }
    public ResultSet searchByNameNumId(String name ,int number ,String team) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("select players.name,num,age,type,players.score,rank,teamId,teams.name\n" +
                "from teams inner join players \n" +
                "on teams.id = players.teamId \n" +
                "where num = ? \n" +
                "and players.name like ? \n" +
                "and teams.name = ? \n" +
                "COLLATE utf8mb4_general_ci;");
        try{
           stmt.setInt(1,number);
           stmt.setString(2,"%"+name+"%");
           stmt.setString(3,team);
       }catch (Exception e){
           e.printStackTrace();
       }
        return stmt.executeQuery();
    }
    public Player getSearchedPlayer(int number , String name , String team) throws SQLException{
        ResultSet set = searchByNameNumId(name,number,team);
        set.next();
        String playerType = set.getString(4);
        Player player = switch (playerType) {
            case "goal" -> new GoalKeeperPlayer();
            case "def" -> new DefenderPlayer();
            case "mid" -> new MidFielderPlayer();
            case "fwd" -> new ForwardPlayer();
            default -> null;
        };
        Team newTeam = new Team();
        player.setName(set.getString(1));
        player.setNumber(set.getInt(2));
        player.setAge(set.getInt(3));
        player.setScore(set.getInt(5));
        player.setType(playerType);
        player.setRank(set.getInt(6));
        newTeam.setId(set.getInt(7));
        newTeam.setName(set.getString(8));
        player.setTeam(newTeam);
        return player;
    }
    public ArrayList<Player> getTeamPlayers(int id) throws SQLException{
        String sql = "select players.name,num,age,type,players.score,rank,teamId,teams.name from\n" +
                     "teams inner join players\n" +
                     "on teams.id = players.teamId where id = "+id;
        ArrayList<Player> players = new ArrayList<>();
        ResultSet set = searchById(sql,id);
        setData(set,players);
        return players;
    }
    public ArrayList<Player> getPlayersInfo() throws SQLException{
        ResultSet set = getData("select players.name,num,age,type,players.score,rank,teamId,teams.name \n" +
                "from teams inner join players\n" +
                "on teams.id = players.teamId;");
        ArrayList<Player> players = new ArrayList<>();
        setData(set,players);
        return players;
    }
    public void setData(ResultSet set,ArrayList<Player> players) throws SQLException{
        String playerType;
        while (set.next()){
            playerType = set.getString(4);
            Team team = new Team();
            Player player = switch (playerType) {
                case "goal" -> new GoalKeeperPlayer();
                case "def" -> new DefenderPlayer();
                case "mid" -> new MidFielderPlayer();
                case "fwd" -> new ForwardPlayer();
                default -> null;
            };
            player.setName(set.getString(1));
            player.setNumber(set.getInt(2));
            player.setAge(set.getInt(3));
            player.setScore(set.getInt(5));
            player.setType(playerType);
            player.setRank(set.getInt(6));
            team.setId(set.getInt(7));
            team.setName(set.getString(8));
            player.setTeam(team);
            players.add(player);
        }
    }
    public void addData(Player player) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("insert into players (name, num ,age, type, score, teamId, `rank`) values (?,?,?,?,?,?,?);");
        stmt.setString(1,player.getName());
        stmt.setInt(2,player.getNumber());
        stmt.setInt(3,player.getAge());
        stmt.setString(4,player.getType());
        stmt.setInt(5,player.getScore());
        stmt.setInt(6,player.getTeam().getId());
        stmt.setInt(7,player.getRank());
        stmt.executeQuery();
    }
    public void deleteData(String name , int id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("delete from players where name = ? and teamId = ? ;");
        stmt.setString(1,name);
        stmt.setInt(2,id);
        stmt.executeQuery();
    }
    public void updateData(int age, int score , int rank , int num, String type, String name , int teamId) throws SQLException {
        String query = "update players set age = ? , score = ? , `rank` = ? , num = ? , type = ? where name = ? and teamId = ?";
        PreparedStatement stmt = DatabaseOperations.con.prepareStatement(query);
        stmt.setInt(1,age);
        stmt.setInt(2,score);
        stmt.setInt(3,rank);
        stmt.setInt(4,num);
        stmt.setString(5,type);
        stmt.setString(6,name);
        stmt.setInt(7,teamId);
        stmt.executeQuery();
    }
}
