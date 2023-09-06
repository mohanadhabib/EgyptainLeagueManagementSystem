package com.example.project.controllers;

import com.example.project.models.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GeneralFunctionsController extends DatabaseOperations{
    @Override
    public void setCon() throws SQLException {
        super.setCon();
    }
    public ArrayList<GoalKeeperPlayer> getGoalKeepersInfo() throws SQLException{
        Statement stmt = con.createStatement();
        ArrayList<GoalKeeperPlayer> goalKeeperPlayers = new ArrayList<>();
        ResultSet set = stmt.executeQuery("select name,score from players \n " +
                "where type = 'goal';");
        while (set.next()){
            GoalKeeperPlayer goalKeeperPlayer = new GoalKeeperPlayer();
            goalKeeperPlayer.setName(set.getString(1));
            goalKeeperPlayer.setScore(set.getInt(2));
            goalKeeperPlayers.add(goalKeeperPlayer);
        }
        return goalKeeperPlayers;
    }
    public ArrayList<GoalKeeperPlayer> goalkeeperRanked () throws SQLException{
        ArrayList<GoalKeeperPlayer> goalkeepers = getGoalKeepersInfo();
        GoalKeeperPlayer temp = new GoalKeeperPlayer();
        int size =  goalkeepers.size();
        for (int i = 0 ; i < size ; i++) {
            for(int j = i + 1 ; j < size ; j++){
                if(goalkeepers.get(i).getScore() < goalkeepers.get(j).getScore()){
                    temp = goalkeepers.get(i);
                    goalkeepers.set(i,goalkeepers.get(j));
                    goalkeepers.set(j,temp);
                }
            }
        }
        ArrayList<GoalKeeperPlayer> rankedGoalkeepers = new ArrayList<>();
        rankedGoalkeepers.add(goalkeepers.get(0));
        rankedGoalkeepers.add(goalkeepers.get(1));
        rankedGoalkeepers.add(goalkeepers.get(2));
        return rankedGoalkeepers;
    }
    public ArrayList<TeamWithAvgAges> getTeamsByAvgAges()throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet set = stmt.executeQuery("select teams.id,teams.name,avg(players.age) from teams \n" +
                "inner join players \n" +
                "on teams.id = players.teamId \n" +
                "group by teams.id \n" +
                "order by avg(players.age);");
        ArrayList<TeamWithAvgAges> teams = new ArrayList<>();
        while (set.next()){
            TeamWithAvgAges team = new TeamWithAvgAges();
            team.setId(set.getInt(1));
            team.setName(set.getString(2));
            team.setAvgOfAges(set.getFloat(3));
            teams.add(team);
        }
        return teams;
    }
    public ArrayList<Player> getPlayersInfo() throws SQLException{
        Statement stmt = con.createStatement();
        ArrayList<Player> players = new ArrayList<>();
        ResultSet set = stmt.executeQuery("select players.teamId , teams.name , players.name , players.type ,players.score from players \n" +
                "inner join teams \n" +
                "on players.teamId = teams.id \n" +
                "where NOT players.type = 'goal';");
        while (set.next()){
            String type = set.getString(4);
            Player player = switch (type){
                case "mid" -> new MidFielderPlayer();
                case "def" -> new DefenderPlayer();
                case "fwd" -> new ForwardPlayer();
                default -> null;
            };
            Team team = new Team();
            team.setId(set.getInt(1));
            team.setName(set.getString(2));
            player.setTeam(team);
            player.setName(set.getString(3));
            player.setType(type);
            player.setScore(set.getInt(5));
            players.add(player);
        }
        return players;
    }
    public ArrayList<Player> playersRanked () throws SQLException{
        ArrayList<Player> players = getPlayersInfo();
        int size =  players.size();
        for (int i = 0 ; i < size ; i++) {
            Player temp ;
            for(int j = i + 1 ; j < size ; j++){
                if(players.get(i).getScore() < players.get(j).getScore()){
                    temp = players.get(i);
                    players.set(i,players.get(j));
                    players.set(j,temp);
                }
            }
        }
        ArrayList<Player> rankedPlayers = new ArrayList<>();
        rankedPlayers.add(players.get(0));
        rankedPlayers.add(players.get(1));
        rankedPlayers.add(players.get(2));
        return rankedPlayers;
    }
    public ArrayList<TeamByGoals> getTeamsSortedByGoals() throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet set = stmt.executeQuery("select teams.id, teams.name , SUM(players.score) \n" +
                "from teams \n" +
                "inner join players \n" +
                "on players.teamId = teams.id \n" +
                "group by teams.id\n" +
                "order by SUM(players.score) desc;");
        ArrayList<TeamByGoals> teams = new ArrayList<>();
        while (set.next()){
            TeamByGoals team = new TeamByGoals();
            team.setId(set.getInt(1));
            team.setName(set.getString(2));
            team.setScoreByGoals(set.getInt(3));
            teams.add(team);
        }
        return teams;
    }
}
