package project.controllers;

import javafx.animation.PauseTransition;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Add_delete_subjectController extends Storage implements Initializable {
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int counter;
    private String tmp;
    private String receiver;
    private ObservableList mylist = FXCollections.observableArrayList();
    private ObservableList myTypelist = FXCollections.observableArrayList();
    @FXML
    private ComboBox choice_type;
    @FXML
    private ComboBox choice_subject;
    @FXML
    private Label status;
    @FXML
    private TextArea text;

    @FXML
    public void fill_combo_subject() throws IOException {
        mylist.clear();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setText("Brak polaczenia z serwerem");
                }
                dos.writeInt(6);
                counter = dis.readInt();
                System.out.println(counter);
                for (int i = 0; i < counter; i++) {
                    receiver = dis.readUTF();
                    System.out.println("Otrzymano: " + receiver);
                    mylist.add(receiver);
                }
                choice_subject.setItems(mylist);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
    }

    @FXML
    public void fill_combo_type() throws IOException {
        myTypelist.clear();
        try {
            while (true) {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setText("Brak polaczenia z serwerem");
                }
                dos.writeInt(20);
                dos.writeUTF(choice_subject.getValue().toString());
                counter = dis.readInt();
                System.out.println("Licznik typow: " + counter);
                for (int i = 0; i < counter; i++) {
                    tmp = dis.readUTF();
                    System.out.println("Otrzymano: " + tmp);
                    myTypelist.add(tmp);
                }
                choice_type.setItems(myTypelist);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        try {
            fill_combo_subject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myTypelist.clear();
        myTypelist.add("WYKLAD");
        myTypelist.add("LABORATORIUM");
        myTypelist.add("PROJEKT");
        choice_type.setItems(myTypelist);
    }


    @FXML
    public void go_back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/edit.fxml"));
        Parent root = loader.load();
        EditController editController = loader.getController();
        editController.store_username(login);
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
    private void delete(ActionEvent event) throws IOException {
        if (choice_subject.getSelectionModel().isEmpty() && !choice_type.getSelectionModel().isEmpty())
            status.setText("Wybierz nazwe przedmiotu");
        else if (!choice_subject.getSelectionModel().isEmpty() && choice_type.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else if (choice_subject.getSelectionModel().isEmpty())
            status.setText("Wybierz nazwe przedmiotu");
        else if (choice_type.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else {
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
                    dos.writeInt(22);
                    dos.writeUTF(choice_subject.getValue().toString());
                    dos.writeUTF(choice_type.getValue().toString());
                    tmp = dis.readUTF();
                    if (tmp.equals("Pomyslnie usunieto")) {
                        status.setText(tmp);
                        Thread.sleep(300);
                        clean_();
                    }
                    fill_combo_subject();
                    break;
                }
                dis.close();
                dos.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();
    }

    @FXML
    private void insert(ActionEvent event) throws IOException {
        if (text.getText().isEmpty() && !choice_type.getSelectionModel().isEmpty())
            status.setText("Podaj nazwe przedmiotu");
        else if (!text.getText().isEmpty() && choice_type.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else if (text.getText().isEmpty())
            status.setText("Podaj nazwe przedmiotu");
        else if (choice_type.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else {
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
                    dos.writeInt(23);
                    dos.writeUTF(text.getText());
                    dos.writeUTF(choice_type.getValue().toString());
                    tmp = dis.readUTF();
                    status.setText(tmp);
                    if (tmp.equals("Pomyslnie dodano")) {
                        clean_();
                        Thread.sleep(300);
                    }
                    fill_combo_subject();
                    break;
                }
                dis.close();
                dos.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();
    }
    @FXML
    private void clean_()
    {
        myTypelist.clear();
        text.setText("");
        choice_subject.setValue("");
        choice_type.setValue("");
        myTypelist.add("WYKLAD");
        myTypelist.add("LABORATORIUM");
        myTypelist.add("PROJEKT");
        choice_type.setItems(myTypelist);
    }
}

