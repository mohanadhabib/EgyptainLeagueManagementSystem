package com.example.project.views;

import com.example.project.controllers.MatchesDatabaseController;
import com.example.project.models.Match;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.ArrayList;

public class TeamMatchesScreen extends Application {
    private ArrayList<Match> matches;
    private ObservableList<Match> dataList;
    private Pane pane;
    private Scene scene;
    private MatchesDatabaseController controller;
    private TableView<Match> tableView;
    public static int teamId;
    @Override
    public void start(Stage stage) {
        try {
            pane = new Pane();
            scene = new Scene(pane,750,600);
            controller = new MatchesDatabaseController();
            controller.setCon();
            mainPage(teamId);
            pane.getChildren().addAll(tableView);
            stage.setScene(scene);
            stage.setTitle("Team Matches Info");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void mainPage(int teamId) {
        tableView = new TableView<>(dataList);
        TableColumn<Match,Integer> teamIdCol = new TableColumn<>("Team id");
        teamIdCol.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getFirstTeam().getId()).asObject());
        TableColumn<Match,String> nameCol  = new TableColumn<>("Team name");
        nameCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFirstTeam().getName()));
        TableColumn<Match,Integer> idCol = new TableColumn<>("Match Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Match, Date> dateCol  = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Match,String> refereeCol = new TableColumn<>("Referee");
        refereeCol.setCellValueFactory(new PropertyValueFactory<>("footballReferee"));
        TableColumn<Match,String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<Match,String> stadiumCol = new TableColumn<>("Stadium");
        stadiumCol.setCellValueFactory(new PropertyValueFactory<>("stadiumName"));
        tableView.getColumns().addAll(teamIdCol,nameCol,idCol,dateCol,refereeCol,scoreCol,stadiumCol);
        tableView.minWidthProperty().bind(pane.widthProperty());
        tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
        showInfo(teamId);
    }
    public void showInfo(int teamId){
        try {
            matches = controller.getTeamMatches(teamId);
            dataList = FXCollections.observableArrayList(matches);
            tableView.setItems(dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
