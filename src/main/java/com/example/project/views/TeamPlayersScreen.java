package com.example.project.views;

import com.example.project.controllers.PlayersDatabaseController;
import com.example.project.models.Player;
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
import java.util.ArrayList;

public class TeamPlayersScreen extends Application {
    private ArrayList<Player> players;
    private ObservableList<Player> dataList;
    private Pane pane;
    private Scene scene;
    private PlayersDatabaseController controller;
    private TableView<Player> tableView;
    public static int teamId;
    @Override
    public void start(Stage stage) {
        try {
            pane = new Pane();
            scene = new Scene(pane,750,600);
            controller = new PlayersDatabaseController();
            controller.setCon();
            mainPage(teamId);
            pane.getChildren().addAll(tableView);
            stage.setScene(scene);
            stage.setTitle("Team Players Info");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void mainPage(int teamId) {
        tableView = new TableView<>(dataList);
        TableColumn<Player,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Player,Integer> numCol  = new TableColumn<>("Number");
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        TableColumn<Player,Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Player,Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<Player,Integer> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Player,Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        TableColumn<Player,Integer> teamIdCol = new TableColumn<>("Team Id");
        teamIdCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTeam().getId()).asObject());
        TableColumn<Player,String> teamNameCol = new TableColumn<>("Team Name");
        teamNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTeam().getName()));
        tableView.getColumns().addAll(nameCol,numCol,ageCol,scoreCol,typeCol,rankCol,teamIdCol,teamNameCol);
        tableView.minWidthProperty().bind(pane.widthProperty());
        tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
        showInfo(teamId);
    }
    public void showInfo(int teamId){
        try {
            players = controller.getTeamPlayers(teamId);
            dataList = FXCollections.observableArrayList(players);
            tableView.setItems(dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
