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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.StoreLogin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Generate_testController extends StoreLogin implements Initializable {
    private double x, y;
    private Stage stage;
    @FXML
    private AnchorPane AnchorPaneMain;

    @FXML
    private void closeAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private ComboBox combo_subject;
    @FXML
    private ComboBox combo_type;
    @FXML
    private ComboBox combo_test_type;
    @FXML
    private Label error_msg;
    final ObservableList subject_list = FXCollections.observableArrayList();
    final ObservableList type_list = FXCollections.observableArrayList();
    final ObservableList type_test_list = FXCollections.observableArrayList();
    private int counter;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String sub, typ;

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
        try {
            fillcombo();
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

    private void fillcombo() throws IOException {
        combo_subject.setMaxHeight(30);
        subject_list.clear();
        try {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Brak polaczenia z serwerem");
                }
                dos.writeInt(3);
                counter = dis.readInt();
                System.out.println(counter);
                for (int i = 0; i < counter; i++) {
                    sub = dis.readUTF();
                    subject_list.add(sub);
                }
                combo_subject.setItems(subject_list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void fillsecondcombo()throws IOException
    {
        combo_type.setMaxHeight(30);
        type_list.clear();
        try {
            try {
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Brak polaczenia z serwerem");
            }
            dos.writeInt(13);
            dos.writeUTF(combo_subject.getValue().toString());
            counter = dis.readInt();
            System.out.println(counter);
            for (int i = 0; i < counter; i++) {
                typ = dis.readUTF();
                type_list.add(typ);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        combo_type.setItems(type_list);
        type_test_list.clear();
        type_test_list.add("Zamknięty");
        type_test_list.add("Otwarty");
        combo_test_type.setItems(type_test_list);
    }
    @FXML
    private void go_to_test(ActionEvent event) throws IOException{

        if(combo_subject.getSelectionModel().isEmpty())
        {
            error_msg.setText("Wybierz nazwe przedmiotu");
        }
        else if(combo_type.getSelectionModel().isEmpty())
        {
            error_msg.setText("Wybierz typ przedmiotu");
        }
        else if(combo_test_type.getSelectionModel().isEmpty())
        {
            error_msg.setText("Wybierz rodzaj testu");
        }
        else
        {
            sub = combo_subject.getValue().toString();
            typ = combo_type.getValue().toString();
            if (combo_test_type.getSelectionModel().isSelected(0)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Test_closed.fxml"));
                Parent root = loader.load();
                Test_closedController Test_closedController = loader.getController();
                Test_closedController.store_username(login);
                Test_closedController.store_sub(sub);
                Test_closedController.store_typ(typ);
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Test_opened.fxml"));
                Parent root = loader.load();
                Test_openedController Test_openedController = loader.getController();
                Test_openedController.store_username(login);
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
    }
}

