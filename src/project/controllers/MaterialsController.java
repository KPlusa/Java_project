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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.Materialsclass;

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
    public TableView tabela = new TableView();
    @FXML
    private TableColumn<String,Materialsclass> mattable;
    @FXML
    private ComboBox chb;
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
    @FXML
    private void fillcombo() throws IOException {
        chb.setMaxHeight(30);
        mylist.clear();
        try {
            while (true) {
                try {
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 6485);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Brak polaczenia z serwerem");
                }
                dos.writeInt(3);
                counter = dis.readInt();
                System.out.println(counter);
                for(int i=0;i<counter;i++) {
                    materials=dis.readUTF();
                    mylist.add(materials);
                }
                chb.setItems(mylist);
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeDraggable();
        try {
            fillcombo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mattable.setCellValueFactory(new PropertyValueFactory<>("material"));
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
    @FXML
    private void setdisplay(ActionEvent event) throws IOException{
        tabela.getItems().clear();
        names=chb.getValue().toString();
        try {
            while (true) {
                counter=0;
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
        tabela.getItems().add(new Materialsclass(materials));
        tabela.getVisibleLeafColumns();

    }
}

