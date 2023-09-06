package com.example.project.views;

import com.example.project.HelloApplication;
import com.example.project.controllers.TeamsDatabaseController;
import com.example.project.models.CaptainPlayer;
import com.example.project.models.Player;
import com.example.project.models.Team;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;


public class TeamScreen extends Application {
    private Text back = new Text("Back");
    private ArrayList<Team> teams;
    private ObservableList<Team> dataList;
    private Pane pane;
    private Scene scene;
    private TeamsDatabaseController controller;
    private TableView<Team> tableView;

    private Button [] btn;

    @Override
    public void start(Stage stage) {
        try{
            pane = new Pane();
            scene = new Scene(pane,750,600);
            controller = new TeamsDatabaseController();
            controller.setCon();
            mainPage();
            back.setFont(Font.font(20));
            back.layoutYProperty().bind(pane.heightProperty().divide(30));
            back.layoutXProperty().bind(pane.widthProperty().divide(84));
            back.setOnMouseClicked(e ->{
                try {
                    HelloApplication main = new HelloApplication();
                    main.start(new Stage());
                    stage.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            pane.getChildren().addAll(back,tableView,btn[0],btn[1],btn[2],btn[3]);
            stage.setScene(scene);
            stage.setTitle("Teams Page");
            stage.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void mainPage() {
        btn = new Button[4];
        btn[0] = new Button("Add");
        btn[0].layoutXProperty().bind(pane.widthProperty().divide(3.25));
        btn[0].layoutYProperty().bind(pane.heightProperty().divide(1.125));
        btn[0].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[0].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[0].setOnAction(e->{
           addDialog();
           tableView.refresh();
        });
        btn[1] = new Button("Delete");
        btn[1].layoutXProperty().bind(pane.widthProperty().divide(1.85));
        btn[1].layoutYProperty().bind(pane.heightProperty().divide(1.125));
        btn[1].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[1].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[1].setOnAction(e->{
            Team team = tableView.getSelectionModel().getSelectedItem();
            deleteInfo(team);
            tableView.refresh();
        });
        btn[2] = new Button("Search");
        btn[2].layoutXProperty().bind(pane.widthProperty().divide(1.3));
        btn[2].layoutYProperty().bind(pane.heightProperty().divide(1.125));
        btn[2].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[2].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[2].setOnAction(e->{
            searchDialog();
            tableView.refresh();
        });
        btn[3] = new Button("Show All Info");
        btn[3].layoutXProperty().bind(pane.widthProperty().divide(12));
        btn[3].layoutYProperty().bind(pane.heightProperty().divide(1.125));
        btn[3].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[3].minHeightProperty().bind(pane.heightProperty().divide(12));
        btn[3].setOnAction(e->{
            showInfo();
            tableView.refresh();
        });
        tableView = new TableView<>(dataList);
        TableColumn<Team,Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Team,String> nameCol  = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Team,String> captainCol = new TableColumn<>("Captain");
        captainCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCaptain().getName()));
        TableColumn<Team,Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
        tableView.setOnKeyPressed(e -> {
              if (e.getCode() == KeyCode.ENTER){
                      Team team = tableView.getSelectionModel().getSelectedItem();
                      TeamMatchesScreen.teamId = team.getId();
                      new TeamMatchesScreen().start(new Stage());
            }
        });
        tableView.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                Team team = tableView.getSelectionModel().getSelectedItem();
                TeamPlayersScreen.teamId = team.getId();
                new TeamPlayersScreen().start(new Stage());
            }
            else if (e.getButton() == MouseButton.SECONDARY){
                Team team = tableView.getSelectionModel().getSelectedItem();
                updateDialog(team);
                tableView.refresh();
            }
        });
        tableView.getColumns().addAll(idCol,nameCol,captainCol,scoreCol);
        tableView.layoutYProperty().bind(pane.heightProperty().divide(20));
        tableView.minWidthProperty().bind(pane.widthProperty());
        tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
        showInfo();
    }
    public void showInfo(){
        try {
            teams = controller.getTeamsInfo();
            dataList = FXCollections.observableArrayList(teams);
            tableView.setItems(dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addInfo(int id , String name , String captain , int score){
        try {
            Team newTeam = new Team();
            Player captainPlayer = new CaptainPlayer();
            captainPlayer.setName(captain);
            newTeam.setId(id);
            newTeam.setName(name);
            newTeam.setCaptain(captainPlayer);
            newTeam.setTotalScore(score);
            controller.addData(newTeam);
            teams.add(newTeam);
            tableView.getItems().clear();
            tableView.getItems().addAll(teams);
            tableView.refresh();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateInfo(Team team , int id , String captainPlayer , int score){
        try{
            controller.updateData(id,captainPlayer,score);
            controller.getData("select * from teams;");
            Player captain = team.getCaptain();
            captain.setName(captainPlayer);
            team.setCaptain(captain);
            team.setTotalScore(score);
            tableView.refresh();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void deleteInfo(Team team){
        try {
            controller.deleteData(team.getId());
            teams.remove(team);
            tableView.getSelectionModel().clearSelection();
            tableView.getItems().clear();
            tableView.getItems().addAll(teams);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addDialog(){
        Label[] lbl = new Label[4];
        TextField [] txt = new TextField[4];
        Button submit = new Button("Submit");
        Pane pane = new Pane();
        Scene scene = new Scene(pane,600,400);
        Stage stage = new Stage();
        lbl[0] = new Label("Enter Id : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(8));
        lbl[1] = new Label("Enter Team Name : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(3.55));
        lbl[2] = new Label("Enter Captain Name : ");
        lbl[2].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[2].layoutYProperty().bind(pane.heightProperty().divide(2.15));
        lbl[3] = new Label("Enter Score :");
        lbl[3].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[3].layoutYProperty().bind(pane.heightProperty().divide(1.5));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(2.70));
        txt[2] = new TextField();
        txt[2].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[2].layoutYProperty().bind(pane.heightProperty().divide(1.85));
        txt[3] = new TextField();
        txt[3].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[3].layoutYProperty().bind(pane.heightProperty().divide(1.35));
        submit.layoutXProperty().bind(pane.widthProperty().divide(4));
        submit.layoutYProperty().bind(pane.heightProperty().divide(1.175));
        submit.setOnAction(e->{
            int id = Integer.parseInt(txt[0].getText());
            String name = txt[1].getText();
            String captain = txt[2].getText();
            int score = Integer.parseInt(txt[3].getText());
            addInfo(id,name,captain,score);
            stage.close();
        });
        pane.getChildren().addAll(lbl[0],lbl[1],lbl[2],lbl[3],txt[0],txt[1],txt[2],txt[3],submit);
        stage.setTitle("Add Team");
        stage.setScene(scene);
        stage.show();
    }
    public void updateDialog(Team team){
        Pane pane = new Pane();
        Scene scene = new Scene(pane,250,250);
        Stage stage = new Stage();
        Button submit = new Button("Submit");
        Label [] lbl = new Label[2];
        TextField [] txt = new TextField[2];
        lbl[0] = new Label("Enter new captain : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(4));
        lbl[1] = new Label("Enter new score : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(2));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(2.75));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(1.65));
        submit.layoutXProperty().bind(pane.widthProperty().divide(4));
        submit.layoutYProperty().bind(pane.heightProperty().divide(1.25));
        submit.setOnAction(e->{
            int id = team.getId();
            String captain = txt[0].getText();
            int score = Integer.parseInt(txt[1].getText());
            updateInfo(team,id,captain,score);
            stage.close();
        });
        pane.getChildren().addAll(lbl[0],lbl[1],txt[0],txt[1],submit);
        stage.setTitle("Update Team");
        stage.setScene(scene);
        stage.show();
    }
    public void searchDialog(){
        Pane pane = new Pane();
        Scene scene = new Scene(pane,300,300);
        Stage stage = new Stage();
        Text text = new Text("Or");
        Button[] btn = new Button[2];
        Label [] lbl = new Label[2];
        TextField [] txt = new TextField[2];
        lbl[0] = new Label("Enter Id : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(10));
        lbl[1] = new Label("Enter Name : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(1.7));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(4.25));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(1.4));
        btn[0] = new Button("Search By Id");
        btn[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[0].layoutYProperty().bind(pane.heightProperty().divide(2.75));
        btn[0].setOnAction(e->{
            searchById(Integer.parseInt(txt[0].getText()));
            stage.close();
        });
        btn[1] = new Button("Search By Name");
        btn[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[1].layoutYProperty().bind(pane.heightProperty().divide(1.2));
        btn[1].setOnAction(e->{
            searchByName(txt[1].getText());
            stage.close();
        });
        text.layoutXProperty().bind(pane.widthProperty().divide(4));
        text.layoutYProperty().bind(pane.heightProperty().divide(1.825));
        pane.getChildren().addAll(lbl[0],lbl[1],txt[0],txt[1],btn[0],btn[1],text);
        stage.setTitle("Search Team");
        stage.setScene(scene);
        stage.show();
    }
    public void searchById(int id){
        try{
            Team team = controller.getSearchedTeamById(id);
            tableView.getItems().clear();
            tableView.getItems().addAll(team);
            tableView.refresh();
        }catch (Exception e){
            tableView.getItems().clear();
        }
    }
    public void searchByName(String name){
        try{
            Team team = controller.getSearchedTeamByName(name);
            tableView.getItems().clear();
            tableView.getItems().addAll(team);
            tableView.refresh();
        }catch (Exception e){
            tableView.getItems().clear();
        }
    }
}
