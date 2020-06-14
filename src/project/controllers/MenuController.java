package project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import project.StoreLogin;


public class MenuController extends StoreLogin implements Initializable {
    private double x, y;
    private Stage stage;
    private String str;
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
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        makeDraggable();


    }



    @FXML
    public void go_subject(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Subject.fxml"));
        Parent root = loader.load();
        SubjectController subjectController = loader.getController();
        subjectController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void go_materials(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Materials.fxml"));
        Parent root = loader.load();
        MaterialsController materialsController = loader.getController();
        materialsController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_questions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Questions.fxml"));
        Parent root = loader.load();
        QuestionsController questionsController = loader.getController();
        questionsController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_edit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/edit.fxml"));
        Parent root = loader.load();
        EditController editController = loader.getController();
        editController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_generate_test(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Generate_test.fxml"));
        Parent root = loader.load();
        Generate_testController generate_testController = loader.getController();
        generate_testController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_rank(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Rank.fxml"));
        Parent root = loader.load();
        RankController rankController = loader.getController();
        rankController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_history(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/History.fxml"));
        Parent root = loader.load();
        HistoryController historyController = loader.getController();
        historyController.store_username(login);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void logout(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        Scene scene = new Scene(parent);
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
/*
    public void store_username(String text)
    {
        System.out.println("Jestem w menuController, mam: "+text);
        list.add(text);
        System.out.println(list);
        //sss.setText(text);
    }
*/




}
