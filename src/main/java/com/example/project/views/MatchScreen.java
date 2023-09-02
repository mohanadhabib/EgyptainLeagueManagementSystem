package com.example.project.views;

import com.example.project.controllers.MatchesDatabaseController;
import com.example.project.models.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class MatchScreen extends Application {
    private ArrayList<Match> matches;
    private ObservableList<Match> dataList;
    private Pane pane;
    private Scene scene;
    private MatchesDatabaseController controller;
    private TableView<Match> tableView;
    private Button[] btn;
    @Override
    public void start(Stage stage) {
        try {
            pane = new Pane();
            scene = new Scene(pane,750,600);
            controller = new MatchesDatabaseController();
            controller.setCon();
            mainPage();
            pane.getChildren().addAll(tableView,btn[0],btn[1],btn[2],btn[3]);
            stage.setScene(scene);
            stage.setTitle("Matches Page");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void mainPage() {
        btn = new Button[4];
        btn[0] = new Button("Add");
        btn[0].layoutXProperty().bind(pane.widthProperty().divide(3.25));
        btn[0].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[0].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[0].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[0].setOnAction(e->{
            tableView.refresh();
        });
        btn[1] = new Button("Delete");
        btn[1].layoutXProperty().bind(pane.widthProperty().divide(1.85));
        btn[1].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[1].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[1].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[1].setOnAction(e->{
            Match match = tableView.getSelectionModel().getSelectedItem();
            deleteInfo(match);
            tableView.refresh();
        });
        btn[2] = new Button("Search");
        btn[2].layoutXProperty().bind(pane.widthProperty().divide(1.3));
        btn[2].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[2].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[2].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[2].setOnAction(e->{
            tableView.refresh();
        });
        btn[3] = new Button("Show All Info");
        btn[3].layoutXProperty().bind(pane.widthProperty().divide(12));
        btn[3].layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn[3].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[3].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[3].setOnAction(e->{
            showInfo();
            tableView.refresh();
        });
        tableView = new TableView<>(dataList);
        TableColumn<Match,Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Match, Date> dateCol  = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Match,String> refereeCol = new TableColumn<>("Referee");
        refereeCol.setCellValueFactory(new PropertyValueFactory<>("footballReferee"));
        TableColumn<Match,String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<Match,String> stadiumCol = new TableColumn<>("Stadium");
        stadiumCol.setCellValueFactory(new PropertyValueFactory<>("stadiumName"));
        TableColumn<Match,String> firstTeamCol = new TableColumn<>("First Team");
        firstTeamCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFirstTeam().getName()));
        TableColumn<Match,String> secondTeamCol = new TableColumn<>("Second Team");
        secondTeamCol.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getSecondTeam().getName()));
        tableView.setOnMouseClicked(e->{
            if (e.getButton() == MouseButton.SECONDARY){
                Match match = tableView.getSelectionModel().getSelectedItem();
                updateDialog(match);
                tableView.refresh();
            }
        });
        tableView.getColumns().addAll(idCol,dateCol,refereeCol,scoreCol,stadiumCol,firstTeamCol,secondTeamCol);
        tableView.minWidthProperty().bind(pane.widthProperty());
        tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
        showInfo();
    }
    public void showInfo(){
        try {
            matches = controller.getMatchesInfo();
            dataList = FXCollections.observableArrayList(matches);
            tableView.setItems(dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteInfo(Match match){
        try {
            controller.deleteData(match.getId());
            matches.remove(match);
            tableView.getSelectionModel().clearSelection();
            tableView.getItems().clear();
            tableView.getItems().addAll(matches);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateInfo(Match match , java.sql.Date date , String referee , String stadium , int id){
        try{
            controller.updateData(id,date,referee,stadium);
            controller.getMatchesInfo();
            Match updateMatch = match;
            updateMatch.setDate(date);
            updateMatch.setFootballReferee(referee);
            updateMatch.setStadiumName(stadium);
            tableView.refresh();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void updateDialog(Match match){
        DatePicker datePicker = new DatePicker();
        Label[] lbl = new Label[3];
        TextField[] txt = new TextField[2];
        Button submit = new Button("Submit");
        Pane pane = new Pane();
        Scene scene = new Scene(pane,600,600);
        Stage stage = new Stage();
        lbl[0] = new Label("Enter New Referee : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(8));
        lbl[1] = new Label("Enter New Stadium : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(3.55));
        lbl[2] = new Label("Enter New Date :");
        lbl[2].layoutXProperty().bind(pane.widthProperty().divide(2));
        lbl[2].layoutYProperty().bind(pane.heightProperty().divide(8));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(2.70));
        datePicker.layoutXProperty().bind(pane.widthProperty().divide(2));
        datePicker.layoutYProperty().bind(pane.heightProperty().divide(5));
        submit.layoutXProperty().bind(pane.widthProperty().divide(6));
        submit.layoutYProperty().bind(pane.heightProperty().divide(1.155));
        submit.minWidthProperty().bind(pane.widthProperty().divide(4));
        submit.setOnAction(e->{
         java.sql.Date date = java.sql.Date.valueOf(datePicker.getValue());
           String st1 = txt[0].getText();
           String st2 = txt[1].getText();
            updateInfo(match,date,st1,st2,match.getId());
            showInfo();
            stage.close();
        });
        pane.getChildren().addAll(lbl[0],lbl[1],lbl[2],txt[0],txt[1],datePicker,submit);
        stage.setTitle("Update Match");
        stage.setScene(scene);
        stage.show();
    }
}
