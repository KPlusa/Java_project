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
import javafx.stage.Stage;
import javafx.scene.Node;
import project.Storage;

/**Klasa controlera dla zakladki "historia" dostepnej po zalogowaniu*/
public class MenuController extends Storage implements Initializable {
    /**Metoda inicjalizacji okna*/
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        makeDraggable();
    }
    /**Metoda przejscia do zakladki "przedmiot"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "materialy"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "pytania"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "edycja"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "generuj test"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "ranking"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "historia"
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
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
    /**Metoda przejscia do zakladki "login" oraz wylogowujaca z aplikacji
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     * @throws IOException wyjatek*/
    @FXML
    public void logout(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }






}
