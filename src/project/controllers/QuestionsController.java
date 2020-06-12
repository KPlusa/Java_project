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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.Questionsclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionsController implements Initializable {
    private double x, y;
    private String st;
    private int counter;
    private Socket s;
    private Integer id;
    private String przedmiot, typ, tresc;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Stage stage;
    @FXML
    private AnchorPane AnchorPaneMain;

    @FXML
    private void closeAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    TableView<Questionsclass> table;
    @FXML
    private TableColumn<Questionsclass, Integer> col_id;
    @FXML
    private TableColumn<Questionsclass, String> col_przedmiot;
    @FXML
    private TableColumn<Questionsclass, String> col_typ;
    @FXML
    private TableColumn<Questionsclass, String> col_tresc;


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
        col_id.setMinWidth(2);
        col_przedmiot.setMinWidth(200);
        col_tresc.setMinWidth(200);
        col_typ.setMinWidth(200);
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_przedmiot.setCellValueFactory(new PropertyValueFactory<>("przedmiot"));
        col_typ.setCellValueFactory(new PropertyValueFactory<>("typ"));
        col_tresc.setCellValueFactory(new PropertyValueFactory<>("tresc"));
        try {
            table.setItems(fill_table());
        } catch (Exception e) {
        }

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


    @FXML
    private ObservableList<Questionsclass> fill_table() throws IOException {
        ObservableList<Questionsclass> questions = FXCollections.observableArrayList();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dos.writeInt(8);
                counter = dis.readInt();
                for (int i = 1; i <= counter; i++) {
                    id=i;
                    przedmiot = dis.readUTF();
                    System.out.println(przedmiot);
                    typ = dis.readUTF();
                    System.out.println(typ);
                    tresc = dis.readUTF();
                    System.out.println(tresc);
                    questions.add(new Questionsclass(id, przedmiot, typ, tresc));
                }

                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        return questions;
    }
}

