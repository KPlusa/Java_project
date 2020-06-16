package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.Storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Show_scoreController extends Storage implements Initializable {
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private Label error_msg;
    @FXML
    private Text Questions;
    @FXML
    private Text points;
    @FXML
    private Text per;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
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

    public void get_error(int tmp){
        error_msg.setText("Dostępne "+tmp+" pytan na 20, test nie aktywowany oraz nie zapisany.");
    }
    public void pkt (int imp,String sub,String typ) throws IOException {
        Questions.setText("20");
        points.setText(String.valueOf(imp));
        float procent=imp;
        procent=procent/20;
        procent=procent*100;
        procent=Math.round(procent);
        per.setText(String.valueOf(procent)+"%");
        try {
            ip = InetAddress.getByName("localhost");
            s = new Socket(ip, 5057);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Brak polaczenia z serwerem");
        }
        DateTimeFormatter data = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(data.format(now));
        dos.writeInt(18);
        dos.writeUTF(login);
        System.out.println(sub);
        dos.writeUTF(sub);
        dos.writeUTF(typ);
        dos.writeInt(imp);
        dos.writeUTF(data.format(now).toString());
    }

}

