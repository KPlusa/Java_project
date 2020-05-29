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
    //private String st;
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
try {
    fill_table_view();
}catch (IOException e) {
    e.printStackTrace();
}


    }

    @FXML
    private void fill_table_view() throws IOException  {
/*
        ObservableList id_OL = FXCollections.observableArrayList();
        ObservableList questions_OL = FXCollections.observableArrayList();
        ObservableList answer_a_OL = FXCollections.observableArrayList();
        ObservableList answer_b_OL = FXCollections.observableArrayList();
        ObservableList answer_c_OL = FXCollections.observableArrayList();
        ObservableList answer_d_OL = FXCollections.observableArrayList();
        id_OL.clear();
        questions_OL.clear();
        answer_a_OL.clear();
        answer_b_OL.clear();
        answer_c_OL.clear();
        answer_d_OL.clear();
*/
        try {
            while (true) {

                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 6485);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    //status.setText("Brak polaczenia z serwerem");
                }


               // dos.writeInt(8);
                //counter = dis.readInt();
                System.out.println(dis.readInt());
                break;}
                /*for (int i = 0; i < counter; i++) {
                    int ID = dis.readInt();
                    String question = dis.readUTF();
                    String answer_a = dis.readUTF();
                    String answer_b = dis.readUTF();
                    String answer_c = dis.readUTF();
                    String answer_d = dis.readUTF();
                    System.out.println("Otrzymano: " + ID + "\t" + question + "\t" + answer_a + "\t" + answer_b + "\t" + answer_c + "\t" + answer_d + "\n");
                    id_OL.add(ID);
                    questions_OL.add(question);
                    answer_a_OL.add(answer_a);
                    answer_b_OL.add(answer_b);
                    answer_c_OL.add(answer_c);
                    answer_d_OL.add(answer_d);
                }*/
                /*
                for (int i = 0; i < counter; i++) {
                    int ID = dis.readInt();
                    id_OL.add(ID);
                }
                for (int i = 0; i < counter; i++) {
                    String question = dis.readUTF();
                    questions_OL.add(question);

                }
                for (int i = 0; i < counter; i++) {

                    String answer_a = dis.readUTF();
                    answer_a_OL.add(answer_a);

                }
                for (int i = 0; i < counter; i++) {

                    String answer_b = dis.readUTF();

                    answer_b_OL.add(answer_b);

                }
                for (int i = 0; i < counter; i++) {

                    String answer_c = dis.readUTF();

                    answer_c_OL.add(answer_c);

                }
                for (int i = 0; i < counter; i++) {
                    String answer_d = dis.readUTF();
                    answer_d_OL.add(answer_d);
                }
                Id.setCellValueFactory(cellDataFeatures -> id_OL);
                Pytanie.setCellValueFactory(cellDataFeatures -> questions_OL);
                OdpA.setCellValueFactory(cellDataFeatures -> answer_a_OL);
                OdpB.setCellValueFactory(cellDataFeatures -> answer_b_OL);
                OdpC.setCellValueFactory(cellDataFeatures -> answer_c_OL);
                OdpD.setCellValueFactory(cellDataFeatures -> answer_d_OL);


                break;
            }*/
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

