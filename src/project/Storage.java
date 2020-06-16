package project;
/**Klasa przechowuje Login aktywnego uzytkownika, oraz metody maksymalizwacji, minimalizacji, zamkniecia, oraz metode pozwalajaca na poruszanie sie aplikacji po ekranie
 * Klasa zawiera rowniez metode go_main oraz go_main_avatar ktore byly dostepne z poziomu kazdej formatki*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.controllers.MenuController;

import java.io.IOException;

public class Storage {
    public String login;
    public String subject_storage;
    public String type_storage;
    public Stage stage;
    public boolean what;
    public double x,y;
    @FXML
    public AnchorPane AnchorPaneMain;
    /**Metoda przechowujaca login uzytkownika*/
    public void store_username(String lgn)
    {
        this.login=lgn;
        //System.out.println("Mam: "+lgn);
    }
    /**Metoda przechowujaca przedmiot*/
    public void store_subject(String sbj)
    {
        this.subject_storage=sbj;
        System.out.println("Mam: "+sbj);
    }
    /**Metoda przechowujaca typ przedmiotu*/
    public void store_type(String tp)
    {
        this.type_storage=tp;
        System.out.println("Mam: "+tp);
    }
    /**Metoda pozwalajaca na minimalizacje okna
     * @param event parametr typu MouseEvent zapewnia możliwość użycia metody po kliknieciu myszka*/
    public void minAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        stage.setIconified(true);
    }
    /**Metoda pozwalajaca na zamkniecie okna
     * @param event parametr typu MouseEvent zapewnia możliwość użycia metody po kliknieciu myszka*/
    public void closeAction(MouseEvent event) {
        System.exit(0);
    }
    /**Metoda pozwalajaca na poruszanie sie okna*/
    public void makeDraggable() {
        AnchorPaneMain.setOnMousePressed(((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));
        AnchorPaneMain.setOnMouseDragged(((event) -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if(!stage.isMaximized()) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        }));
    }
    /**Metoda pozwalajaca na maksymalizacje okna
     * @param event parametr typu MouseEvent zapewnia możliwość użycia metody po kliknieciu myszka*/
    public void  maxAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        if(stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setResizable(false);
            what=false;
        }
        else {
            stage.setMaximized(true);
            stage.setResizable(true);
            what=true;
        }
    }
    @FXML
    public void go_menu(ActionEvent event) throws IOException {
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


}
