package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.StoreLogin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Add_delete_subjectController extends StoreLogin implements Initializable {
    private double x, y;
    private Stage stage;
    @FXML
    private AnchorPane AnchorPaneMain;
    @FXML
    private void closeAction(MouseEvent event){
        System.exit(0);
    }

    @FXML
    private void minAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void maxAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        if(stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setResizable(false);
        }
        else {
            stage.setMaximized(true);
            stage.setResizable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeDraggable();

    }


    @FXML
    public void go_back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/edit.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_menu_avatar(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/menu.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    private void makeDraggable()
    {
        AnchorPaneMain.setOnMousePressed(((event) -> {
            x=event.getSceneX();
            y=event.getSceneY();
        }));

        AnchorPaneMain.setOnMouseDragged(((event) -> {
            stage= (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX()-x);
            stage.setY(event.getScreenY()-y);
        }));
    }

}

