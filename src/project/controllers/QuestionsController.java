package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Questionsclass;
import project.Storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "pytania" dostepnej z poziomu menu*/
public class QuestionsController extends Storage implements Initializable {
    private int counter;
    private Socket s;
    private Integer id;
    private String przedmiot, typ, tresc;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
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
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    /**Metoda wypelniajaca TableView odpowiednimi wartosciami*/
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
        dis.close();
        dos.close();
        s.close();
        return questions;
    }
}

