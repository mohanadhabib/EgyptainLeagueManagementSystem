package com.example.project;

import com.example.project.views.PlayerScreen;
import com.example.project.views.TeamScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class HelloApplication extends Application {
    Text lbl;
    Button[] btn;
    Pane pane;
    Scene scene ;

    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
        scene = new Scene(pane,750,600);
        lblDesign();
        buttonsDesign(stage);
        pane.getChildren().addAll(lbl,btn[0],btn[1],btn[2]);
        stage.setMinWidth(600);
        stage.setMaxWidth(900);
        stage.setMinHeight(400);
        stage.setMaxHeight(900);
        stage.setScene(scene);
        stage.setTitle("Egyptian League");
        stage.show();
    }
    public void lblDesign(){
        lbl = new Text("Egyptian League Management System");
        lbl.layoutXProperty().bind(pane.widthProperty().divide(4));
        lbl.layoutYProperty().bind(pane.heightProperty().divide(5));
        lbl.setStyle("-fx-font-size: 22;");
    }
    public void buttonsDesign(Stage stage){
        btn = new Button[3];
        btn[0] = new Button("Teams Page");
        btn[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[0].layoutYProperty().bind(pane.heightProperty().divide(3.75));
        btn[0].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[0].minHeightProperty().bind(pane.heightProperty().divide(6));
        btn[0].setOnAction(e->{
           try {
               TeamScreen teamScreen = new TeamScreen();
               teamScreen.start(new Stage());
               stage.close();
           }catch (Exception ex){
               ex.printStackTrace();
           }
        });
        btn[1] = new Button("Players Page");
        btn[1].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[1].layoutYProperty().bind(pane.heightProperty().divide(2.15));
        btn[1].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[1].minHeightProperty().bind(pane.heightProperty().divide(6));
        btn[1].setOnAction(e->{
            try {
                PlayerScreen playerScreen = new PlayerScreen();
                playerScreen.start(new Stage());
                stage.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        btn[2] = new Button("Matches Page");
        btn[2].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[2].layoutYProperty().bind(pane.heightProperty().divide(1.50));
        btn[2].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[2].minHeightProperty().bind(pane.heightProperty().divide(6));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
