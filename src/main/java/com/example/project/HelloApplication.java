package com.example.project;

import com.example.project.views.GeneralFunctionsScreen;
import com.example.project.views.MatchScreen;
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
    Text[] hints;
    Button[] btn;
    Pane pane;
    Scene scene ;

    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
        scene = new Scene(pane,750,600);
        lblDesign();
        buttonsDesign(stage);
        hintsDesign();
        pane.getChildren().addAll(lbl,btn[0],btn[1],btn[2],btn[3]);
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
        lbl.layoutYProperty().bind(pane.heightProperty().divide(10));
        lbl.setStyle("-fx-font-size: 22;");
    }
    public void hintsDesign(){
        hints = new Text[5];
        hints[0] = new Text("To Show Team Players Double Click The Record.");
        hints[0].layoutXProperty().bind(pane.widthProperty().divide(2));
        hints[0].layoutYProperty().bind(pane.widthProperty().divide(6));
        hints[1] = new Text("To Show Team Matches Press Enter To The Record.");
        hints[1].layoutXProperty().bind(pane.widthProperty().divide(2));
        hints[1].layoutYProperty().bind(pane.widthProperty().divide(5));
        hints[2] = new Text("To Update Team Info Right Click The Record.");
        hints[2].layoutXProperty().bind(pane.widthProperty().divide(2));
        hints[2].layoutYProperty().bind(pane.widthProperty().divide(4.30));
        hints[3] = new Text("To Update Player Info Right Click The Record.");
        hints[3].layoutXProperty().bind(pane.widthProperty().divide(2));
        hints[3].layoutYProperty().bind(pane.widthProperty().divide(2.65));
        hints[4] = new Text("To Update Match Info Right Click The Record.");
        hints[4].layoutXProperty().bind(pane.widthProperty().divide(2));
        hints[4].layoutYProperty().bind(pane.widthProperty().divide(1.825));
        pane.getChildren().addAll(hints[0],hints[1],hints[2],hints[3],hints[4]);
    }
    public void buttonsDesign(Stage stage){
        btn = new Button[4];
        btn[0] = new Button("Teams Page");
        btn[0].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[0].layoutYProperty().bind(pane.heightProperty().divide(6));
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
        btn[1].layoutYProperty().bind(pane.heightProperty().divide(2.65));
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
        btn[2].layoutYProperty().bind(pane.heightProperty().divide(1.7));
        btn[2].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[2].minHeightProperty().bind(pane.heightProperty().divide(6));
        btn[2].setOnAction(e->{
            try {
                MatchScreen matchScreen = new MatchScreen();
                matchScreen.start(new Stage());
                stage.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        btn[3] = new Button("General Page");
        btn[3].layoutXProperty().bind(pane.widthProperty().divide(4));
        btn[3].layoutYProperty().bind(pane.heightProperty().divide(1.25));
        btn[3].minWidthProperty().bind(pane.widthProperty().divide(6));
        btn[3].minHeightProperty().bind(pane.heightProperty().divide(6));
        btn[3].setOnAction(e->{
            try {
                GeneralFunctionsScreen generalFunctionsScreen = new GeneralFunctionsScreen();
                generalFunctionsScreen.start(new Stage());
                stage.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
