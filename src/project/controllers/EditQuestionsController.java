package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.table.TableColumn;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class EditQuestionsController implements Initializable {
    private double x, y;
    private Stage stage;
    private int counter;
    private String receiver;
    private String value;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private TableView QClosed;
    @FXML
    private javafx.scene.control.TableColumn Id;
    @FXML
    private javafx.scene.control.TableColumn Pytanie;
    @FXML
    private javafx.scene.control.TableColumn OdpA;
    @FXML
    private javafx.scene.control.TableColumn OdpB;
    @FXML
    private javafx.scene.control.TableColumn OdpC;
    @FXML
    private javafx.scene.control.TableColumn OdpD;

    @FXML
    private AnchorPane AnchorPaneMain;

    @FXML
    private void closeAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void minAction(MouseEvent event) {
        Stage stage = (Stage) AnchorPaneMain.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maxAction(MouseEvent event) {
        Stage stage = (Stage) AnchorPaneMain.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setResizable(false);
        } else {
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
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/edit.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void go_menu_avatar(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/menu.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void makeDraggable() {
        AnchorPaneMain.setOnMousePressed(((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));

        AnchorPaneMain.setOnMouseDragged(((event) -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }));
    }

}

