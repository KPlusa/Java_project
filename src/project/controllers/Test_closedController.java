package project.controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import project.Questionsclass;
import project.Rankclass;
import project.StoreLogin;
import project.Testclosedclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Test_closedController extends StoreLogin implements Initializable {
    private double x,y;
    private Stage stage;
    private String sub,typ;
    private int counter;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String tresc, odp_a, odp_b, odp_c,odp_d;
    private int popr_o;
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
        try {
            fill_test();
        } catch (IOException e) {
            e.printStackTrace();
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
    public void store_sub(String subject){
        sub=subject;
    }
    public void store_typ(String type){
        typ=type;
    }
    public ObservableList<Testclosedclass> fill_test() throws IOException {
        ObservableList<Testclosedclass> Testclosed = FXCollections.observableArrayList();
        try {
            ip = InetAddress.getByName("localhost");
            s = new Socket(ip, 5057);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Brak polaczenia z serwerem");
        }
        dos.writeInt(19);
        System.out.println(sub);
        System.out.println(typ);
        dos.writeUTF(sub);
        dos.writeUTF(typ);
        counter=dis.readInt();
        for(int i=0;i<counter;i++)
        {
            tresc = dis.readUTF();
            odp_a = dis.readUTF();
            odp_b = dis.readUTF();
            odp_c = dis.readUTF();
            odp_d = dis.readUTF();
            popr_o = dis.readInt();
            System.out.println(tresc);
            System.out.println(odp_a);
            System.out.println(odp_b);
            System.out.println(odp_c);
            System.out.println(odp_d);
            System.out.println(popr_o);
            Testclosed.add(new Testclosedclass(tresc, odp_a, odp_b, odp_c,odp_d,popr_o));
        }
        return Testclosed;
    }

}

