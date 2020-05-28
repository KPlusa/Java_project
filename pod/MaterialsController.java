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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MaterialsController implements Initializable {
    private String st;
    private String materials;
    private String names;
    private int counter;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private double x, y;
    private Stage stage;
    final ObservableList mylist = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox chb;
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
 /*   @FXML
    private void fillcombo() throws IOException {
        mylist.clear();
        try {
            while (true) {

                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 6485);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());

                dos.writeInt(3);
                counter = dis.readInt();
                for(int i=0;counter>=i;i++) {
                    materials=dis.readUTF();
                    mylist.add(materials);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }

    }*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeDraggable();
        chb.setMaxHeight(30);
        mylist.clear();
       /* try {
            while (true) {

                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 6485);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());

                dos.writeInt(3);
                counter = dis.readInt();
                for (int i = 0; counter >= i; i++) {
                    materials = dis.readUTF().toString();
                    mylist.add(materials);
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        chb.setItems(mylist);
    }

    @FXML
    public void go_menu(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/menu.fxml"));
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
   /* @FXML
    private void setdisplay(MouseEvent event) throws IOException{
        //names=chb.getValue().toString();
        names="SYSTEMY OPERACYJNE";
        try {
            while (true) {

                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 6485);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(5);
                dos.writeUTF(names);
                counter = dis.readInt();
                materials=dis.readUTF();
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        chb.setValue(materials);

    }*/
}

