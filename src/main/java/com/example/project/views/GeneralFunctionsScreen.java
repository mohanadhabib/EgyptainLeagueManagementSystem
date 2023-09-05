package com.example.project.views;

import com.example.project.controllers.GeneralFunctionsController;
import com.example.project.models.GoalKeeperPlayer;
import com.example.project.models.Player;
import com.example.project.models.TeamByGoals;
import com.example.project.models.TeamWithAvgAges;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class GeneralFunctionsScreen extends Application {
    private Pane pane;
    private Scene scene;
    private GeneralFunctionsController controller;
    private Button[] btn;
    @Override
    public void start(Stage stage) {
        try {
            pane = new Pane();
            scene = new Scene(pane,750,600);
            controller = new GeneralFunctionsController();
            controller.setCon();
            mainPage();
            pane.getChildren().addAll(btn[0],btn[1],btn[2],btn[3]);
            stage.setScene(scene);
            stage.setTitle("General Page");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void mainPage() {
        btn = new Button[4];
        btn[0] = new Button("Show Top 3 GoalKeepers");
        btn[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[0].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[0].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[0].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[0].setOnAction(e-> showTopThreeGoalKeepers());
        btn[1] = new Button("Show Teams By Average Ages");
        btn[1].layoutXProperty().bind(pane.widthProperty().divide(2.10));
        btn[1].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[1].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[1].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[1].setOnAction(e-> showTeamsByAvgAges());
        btn[2] = new Button("Show Teams By Goals");
        btn[2].layoutXProperty().bind(pane.widthProperty().divide(1.35));
        btn[2].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[2].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[2].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[2].setOnAction(e-> showTeamsSortedByGoals());
        btn[3] = new Button("Show Top 3 Players");
        btn[3].layoutXProperty().bind(pane.widthProperty().divide(16));
        btn[3].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[3].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[3].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[3].setOnAction(e-> showTopThreePlayers());
    }
    public void showTopThreePlayers(){
        try{
            ArrayList<Player> players = controller.playersRanked();
            ObservableList<Player> list = FXCollections.observableArrayList(players);
            TableView<Player> tableView = new TableView<>(list);
            TableColumn<Player,Integer> idCol = new TableColumn<>("Team Id");
            idCol.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getTeam().getId()).asObject());
            TableColumn<Player,String> teamNameCol = new TableColumn<>("Team Name");
            teamNameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getTeam().getName()));
            TableColumn<Player,String> playerNameCol = new TableColumn<>("Player Name");
            playerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<Player,String> typeCol = new TableColumn<>("Player Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            TableColumn<Player,Integer> scoreCol = new TableColumn<>("Player Score");
            scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
            tableView.setItems(list);
            tableView.getColumns().addAll(idCol,teamNameCol,playerNameCol,typeCol,scoreCol);
            tableView.minWidthProperty().bind(pane.widthProperty());
            tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
            pane.getChildren().addAll(tableView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showTopThreeGoalKeepers(){
       try{
           ArrayList<GoalKeeperPlayer> goalKeeperPlayers = controller.goalkeeperRanked();
           ObservableList<GoalKeeperPlayer> list = FXCollections.observableArrayList(goalKeeperPlayers);
           TableView<GoalKeeperPlayer> goalKeeperTableView = new TableView<>(list);
           TableColumn<GoalKeeperPlayer,String> nameCol = new TableColumn<>("Name");
           nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
           TableColumn<GoalKeeperPlayer,Integer> scoreCol = new TableColumn<>("Score");
           scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
           goalKeeperTableView.setItems(list);
           goalKeeperTableView.getColumns().addAll(nameCol,scoreCol);
           goalKeeperTableView.minWidthProperty().bind(pane.widthProperty());
           goalKeeperTableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
           pane.getChildren().addAll(goalKeeperTableView);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public void showTeamsByAvgAges(){
        try{
            ArrayList<TeamWithAvgAges> teams = controller.getTeamsByAvgAges();
            ObservableList<TeamWithAvgAges> list = FXCollections.observableArrayList(teams);
            TableView<TeamWithAvgAges> tableView = new TableView<>(list);
            TableColumn<TeamWithAvgAges,Integer> idCol = new TableColumn<>("Team Id");
            idCol.setCellValueFactory(new PropertyValueFactory<>("teamId"));
            TableColumn<TeamWithAvgAges,String> nameCol = new TableColumn<>("Team Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
            TableColumn<TeamWithAvgAges,Float> averageAgeCol = new TableColumn<>("Average Age");
            averageAgeCol.setCellValueFactory(new PropertyValueFactory<>("avgOfAges"));
            tableView.setItems(list);
            tableView.getColumns().addAll(idCol,nameCol,averageAgeCol);
            tableView.minWidthProperty().bind(pane.widthProperty());
            tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
            pane.getChildren().addAll(tableView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showTeamsSortedByGoals(){
        try{
            ArrayList<TeamByGoals> teams = controller.getTeamsSortedByGoals();
            ObservableList<TeamByGoals> list = FXCollections.observableArrayList(teams);
            TableView<TeamByGoals> tableView = new TableView<>(list);
            TableColumn<TeamByGoals,Integer> idCol = new TableColumn<>("Team Id");
            idCol.setCellValueFactory(new PropertyValueFactory<>("teamId"));
            TableColumn<TeamByGoals,String> nameCol = new TableColumn<>("Team Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
            TableColumn<TeamByGoals,Integer> goalsCol = new TableColumn<>("Goals");
            goalsCol.setCellValueFactory(new PropertyValueFactory<>("scoreByGoals"));
            tableView.setItems(list);
            tableView.getColumns().addAll(idCol,nameCol,goalsCol);
            tableView.minWidthProperty().bind(pane.widthProperty());
            tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
            pane.getChildren().addAll(tableView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
