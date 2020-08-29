package project.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Materialsclass;
import project.Storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "materialy" dostepnej z poziomu menu*/
public class MaterialsController extends Storage implements Initializable {
    private String materials;
    private String names;
    private int counter;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    final ObservableList mylist = FXCollections.observableArrayList();
    @FXML
    public TableView tabela = new TableView();
    @FXML
    private TableColumn<String,Materialsclass> mattable;
    @FXML
    private ComboBox chb;

    /** Metoda wypelniajaca ComboBox nazwami przedmiotow
     *
     * @throws IOException wyjatek
     */
    @FXML
    private void fillcombo() throws IOException {
        chb.setMaxHeight(30);
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
        dis.close();
        dos.close();
        s.close();

    }
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        try {
            fillcombo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mattable.setCellValueFactory(new PropertyValueFactory<>("material"));
    }
    /**Metoda wypelniajca kontener TableView materialami dla wybranego przedmiotu
     *
     * @param event parametr zapewnia wywolanie metody po wybraniu daty
     * @throws IOException wyjatek*/
    @FXML
    private void setdisplay(ActionEvent event) throws IOException{
        tabela.getItems().clear();
        names=chb.getValue().toString();
        try {
            while (true) {
                counter=0;
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
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
        dis.close();
        dos.close();
        s.close();

    }
}

