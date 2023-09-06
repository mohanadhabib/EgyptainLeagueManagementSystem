package com.example.project.views;

import com.example.project.HelloApplication;
import com.example.project.controllers.PlayersDatabaseController;
import com.example.project.models.*;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerScreen extends Application {
    private Text back = new Text("Back");
    private ArrayList<Player> players;
    private ObservableList<Player> dataList;
    private Pane pane;
    private Scene scene;
    private PlayersDatabaseController controller;
    private TableView<Player> tableView;
    private Button[] btn;
    public static int teamId;
    @Override
    public void start(Stage stage) {
        try {
            pane = new Pane();
            scene = new Scene(pane,750,600);
            controller = new PlayersDatabaseController();
            controller.setCon();
            mainPage(teamId);
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
            stage.setTitle("Players Page");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void mainPage(int teamId) {
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
            Player player = tableView.getSelectionModel().getSelectedItem();
            deleteInfo(player);
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
        tableView.setOnMouseClicked(e->{
             if (e.getButton() == MouseButton.SECONDARY){
                Player player = tableView.getSelectionModel().getSelectedItem();
                updateDialog(player);
                tableView.refresh();
            }
        });
        tableView.getColumns().addAll(nameCol,numCol,ageCol,scoreCol,typeCol,rankCol,teamIdCol,teamNameCol);
        tableView.layoutYProperty().bind(pane.heightProperty().divide(20));
        tableView.minWidthProperty().bind(pane.widthProperty());
        tableView.minHeightProperty().bind(pane.heightProperty().subtract(115));
        showInfo();
    }
    public void showInfo(){
        try {
            players = controller.getPlayersInfo();
            dataList = FXCollections.observableArrayList(players);
            tableView.setItems(dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addInfo(String name , int num , int age , String type , int score , int teamId , int rank){
        try {
            Player newPlayer = switch (type){
                case "goal" -> new GoalKeeperPlayer();
                case "def" -> new DefenderPlayer();
                case "mid" -> new MidFielderPlayer();
                case "fwd" -> new ForwardPlayer();
                default -> null;
            };
            Team newTeam = new Team();
            newTeam.setId(teamId);
            newPlayer.setName(name);
            newPlayer.setNumber(num);
            newPlayer.setAge(age);
            newPlayer.setType(type);
            newPlayer.setScore(score);
            newPlayer.setTeam(newTeam);
            newPlayer.setRank(rank);
            controller.addData(newPlayer);
            players.add(newPlayer);
            tableView.getItems().clear();
            tableView.getItems().addAll(players);
            tableView.refresh();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateInfo(Player player , int age , int score , String type , int rank , int num ){
        try{
            controller.updateData(age,score,rank,num,type,player.getName(),player.getTeam().getId());
            controller.getData("select * from players;");
            Player updatePlayer = player;
            updatePlayer.setAge(age);
            updatePlayer.setScore(score);
            updatePlayer.setType(type);
            updatePlayer.setRank(rank);
            updatePlayer.setNumber(num);
            tableView.refresh();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void deleteInfo(Player player){
        try {
            controller.deleteData(player.getName(),player.getTeam().getId());
            players.remove(player);
            tableView.getSelectionModel().clearSelection();
            tableView.getItems().clear();
            tableView.getItems().addAll(players);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void searchByNameNumAndTeam(String name , int number ,String team){
        try{
            Player player = controller.getSearchedPlayer(number,name,team);
            tableView.getItems().clear();
            tableView.getItems().addAll(player);
            tableView.refresh();
        }catch (Exception e){
            tableView.getItems().clear();
        }
    }
    public void addDialog(){
        ToggleGroup group = new ToggleGroup();
        Label[] lbl = new Label[7];
        RadioButton[] radioButton = new RadioButton[4];
        TextField[] txt = new TextField[6];
        Button submit = new Button("Submit");
        Pane pane = new Pane();
        Scene scene = new Scene(pane,600,600);
        Stage stage = new Stage();
        lbl[0] = new Label("Enter Name : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(8));
        lbl[1] = new Label("Enter Number : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(3.55));
        lbl[2] = new Label("Enter Age : ");
        lbl[2].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[2].layoutYProperty().bind(pane.heightProperty().divide(2.15));
        lbl[3] = new Label("Enter Score :");
        lbl[3].layoutXProperty().bind(pane.widthProperty().divide(2));
        lbl[3].layoutYProperty().bind(pane.heightProperty().divide(8));
        lbl[4] = new Label("Enter Team Id :");
        lbl[4].layoutXProperty().bind(pane.widthProperty().divide(2));
        lbl[4].layoutYProperty().bind(pane.heightProperty().divide(3.55));
        lbl[5] = new Label("Enter Rank :");
        lbl[5].layoutXProperty().bind(pane.widthProperty().divide(2));
        lbl[5].layoutYProperty().bind(pane.heightProperty().divide(2.15));
        lbl[6] = new Label("Select Type :");
        lbl[6].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[6].layoutYProperty().bind(pane.heightProperty().divide(1.60));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(2.70));
        txt[2] = new TextField();
        txt[2].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[2].layoutYProperty().bind(pane.heightProperty().divide(1.85));
        txt[3] = new TextField();
        txt[3].layoutXProperty().bind(pane.widthProperty().divide(2));
        txt[3].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[4] = new TextField();
        txt[4].layoutXProperty().bind(pane.widthProperty().divide(2));
        txt[4].layoutYProperty().bind(pane.heightProperty().divide(2.70));
        txt[5] = new TextField();
        txt[5].layoutXProperty().bind(pane.widthProperty().divide(2));
        txt[5].layoutYProperty().bind(pane.heightProperty().divide(1.85));
        radioButton[0] = new RadioButton("goal");
        radioButton[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[0].layoutYProperty().bind(pane.heightProperty().divide(1.50));
        radioButton[0].setToggleGroup(group);
        radioButton[1] = new RadioButton("def");
        radioButton[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[1].layoutYProperty().bind(pane.heightProperty().divide(1.40));
        radioButton[1].setToggleGroup(group);
        radioButton[2] = new RadioButton("mid");
        radioButton[2].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[2].layoutYProperty().bind(pane.heightProperty().divide(1.315));
        radioButton[2].setToggleGroup(group);
        radioButton[3] = new RadioButton("fwd");
        radioButton[3].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[3].layoutYProperty().bind(pane.heightProperty().divide(1.235));
        radioButton[3].setToggleGroup(group);
        submit.layoutXProperty().bind(pane.widthProperty().divide(6));
        submit.layoutYProperty().bind(pane.heightProperty().divide(1.155));
        submit.minWidthProperty().bind(pane.widthProperty().divide(4));
        submit.setOnAction(e->{
            String name = txt[0].getText() ;
            int num = Integer.parseInt(txt[1].getText());
            int age = Integer.parseInt(txt[2].getText());
            int score = Integer.parseInt(txt[3].getText());
            int teamId = Integer.parseInt(txt[4].getText());
            int rank = Integer.parseInt(txt[5].getText());
            String type = "";
            if(radioButton[0].isSelected()){
                type = radioButton[0].getText();
            }
            else if(radioButton[1].isSelected()){
                type = radioButton[1].getText();
            }
            else if(radioButton[2].isSelected()){
                type = radioButton[2].getText();
            }
            else if(radioButton[3].isSelected()){
                type = radioButton[3].getText();
            }
            addInfo(name,num,age,type,score,teamId,rank);
            showInfo();
            stage.close();
        });
        pane.getChildren().addAll(lbl[0],lbl[1],lbl[2],lbl[3],lbl[4],lbl[5],lbl[6],txt[0],txt[1],txt[2],txt[3],txt[4],txt[5],radioButton[0],radioButton[1],radioButton[2],radioButton[3],submit);
        stage.setTitle("Add Player");
        stage.setScene(scene);
        stage.show();
    }
    public void updateDialog(Player player){
        ToggleGroup group = new ToggleGroup();
        Label[] lbl = new Label[5];
        RadioButton[] radioButton = new RadioButton[4];
        TextField[] txt = new TextField[4];
        Button submit = new Button("Submit");
        Pane pane = new Pane();
        Scene scene = new Scene(pane,600,600);
        Stage stage = new Stage();
        lbl[0] = new Label("Enter New Age : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(8));
        lbl[1] = new Label("Enter New Score : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(3.55));
        lbl[2] = new Label("Enter New Rank :");
        lbl[2].layoutXProperty().bind(pane.widthProperty().divide(2));
        lbl[2].layoutYProperty().bind(pane.heightProperty().divide(8));
        lbl[3] = new Label("Enter New Number :");
        lbl[3].layoutXProperty().bind(pane.widthProperty().divide(2));
        lbl[3].layoutYProperty().bind(pane.heightProperty().divide(3.55));
        lbl[4] = new Label("Select Type :");
        lbl[4].layoutXProperty().bind(pane.widthProperty().divide(6));
        lbl[4].layoutYProperty().bind(pane.heightProperty().divide(1.60));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(2.70));
        txt[2] = new TextField();
        txt[2].layoutXProperty().bind(pane.widthProperty().divide(2));
        txt[2].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[3] = new TextField();
        txt[3].layoutXProperty().bind(pane.widthProperty().divide(2));
        txt[3].layoutYProperty().bind(pane.heightProperty().divide(2.70));
        radioButton[0] = new RadioButton("goal");
        radioButton[0].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[0].layoutYProperty().bind(pane.heightProperty().divide(1.50));
        radioButton[0].setToggleGroup(group);
        radioButton[1] = new RadioButton("def");
        radioButton[1].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[1].layoutYProperty().bind(pane.heightProperty().divide(1.40));
        radioButton[1].setToggleGroup(group);
        radioButton[2] = new RadioButton("mid");
        radioButton[2].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[2].layoutYProperty().bind(pane.heightProperty().divide(1.315));
        radioButton[2].setToggleGroup(group);
        radioButton[3] = new RadioButton("fwd");
        radioButton[3].layoutXProperty().bind(pane.widthProperty().divide(6));
        radioButton[3].layoutYProperty().bind(pane.heightProperty().divide(1.235));
        radioButton[3].setToggleGroup(group);
        submit.layoutXProperty().bind(pane.widthProperty().divide(6));
        submit.layoutYProperty().bind(pane.heightProperty().divide(1.155));
        submit.minWidthProperty().bind(pane.widthProperty().divide(4));
        submit.setOnAction(e->{
            int age = Integer.parseInt(txt[0].getText());
            int score = Integer.parseInt(txt[1].getText());
            int rank = Integer.parseInt(txt[2].getText());
            int num = Integer.parseInt(txt[3].getText());
            String type = "";
            if(radioButton[0].isSelected()){
                type = radioButton[0].getText();
            }
            else if(radioButton[1].isSelected()){
                type = radioButton[1].getText();
            }
            else if(radioButton[2].isSelected()){
                type = radioButton[2].getText();
            }
            else if(radioButton[3].isSelected()){
                type = radioButton[3].getText();
            }
            updateInfo(player,age,score,type,rank,num);
            showInfo();
            stage.close();
        });
        pane.getChildren().addAll(lbl[0],lbl[1],lbl[2],lbl[3],lbl[4],txt[0],txt[1],txt[2],txt[3],radioButton[0],radioButton[1],radioButton[2],radioButton[3],submit);
        stage.setTitle("Update Player");
        stage.setScene(scene);
        stage.show();
    }
    public void searchDialog(){
        Pane pane = new Pane();
        Scene scene = new Scene(pane,300,300);
        Stage stage = new Stage();
        Button btn = new Button("Search");
        Label [] lbl = new Label[3];
        TextField [] txt = new TextField[3];
        lbl[0] = new Label("Enter Name : ");
        lbl[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[0].layoutYProperty().bind(pane.heightProperty().divide(10));
        lbl[1] = new Label("Enter Number : ");
        lbl[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[1].layoutYProperty().bind(pane.heightProperty().divide(3));
        lbl[2] = new Label("Enter Team Name : ");
        lbl[2].layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl[2].layoutYProperty().bind(pane.heightProperty().divide(1.7));
        txt[0] = new TextField();
        txt[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[0].layoutYProperty().bind(pane.heightProperty().divide(5));
        txt[1] = new TextField();
        txt[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[1].layoutYProperty().bind(pane.heightProperty().divide(2.25));
        txt[2] = new TextField();
        txt[2].layoutXProperty().bind(pane.widthProperty().divide(4));
        txt[2].layoutYProperty().bind(pane.heightProperty().divide(1.425));
        btn.layoutXProperty().bind(pane.widthProperty().divide(4));
        btn.layoutYProperty().bind(pane.heightProperty().divide(1.15));
        btn.minWidthProperty().bind(pane.widthProperty().divide(3.55));
        btn.setOnAction(e->{
            searchByNameNumAndTeam(txt[0].getText(),Integer.parseInt(txt[1].getText()),txt[2].getText());
            stage.close();
        });
        pane.getChildren().addAll(lbl[0],lbl[1],lbl[2],txt[0],txt[1],txt[2],btn);
        stage.setTitle("Search Player");
        stage.setScene(scene);
        stage.show();
    }
}