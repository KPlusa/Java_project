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
import javafx.stage.Stage;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "generuj test" dostepnej z poziomu menu*/
public class Generate_testController extends Storage implements Initializable {

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

    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        try {
            fillcombo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Metoda wypelniajaca ComboBox nazwami przedmiotow
     *
     * @throws IOException wyjatek
     */
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
            dis.close();
            dos.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**Metoda wypelniajace pozostale ComboBox dostepnymi typami przedmiotow oraz testow(otwarte/zamkniete)
     *
     * @throws IOException wyjatek
     */
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
            for (int i = 0; i < counter; i++) {
                typ = dis.readUTF();
                type_list.add(typ);
            }

        } catch (IOException e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
        dis.close();
        dos.close();
        s.close();
        combo_type.setItems(type_list);
        type_test_list.clear();
        type_test_list.add("Zamknięty");
        type_test_list.add("Otwarty");
        combo_test_type.setItems(type_test_list);
    }

    /**Metoda odpowiadajaca za przejscie do testu, w tej metodzie wprowadzone sa rowniez zabezpieczenia przed bledna generacja testu
     *
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku
    * @throws IOException wyjatek*/
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
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dos.writeInt(21);
                dos.writeUTF(sub);
                dos.writeUTF(typ);
                counter=dis.readInt();
                dis.close();
                dos.close();
                s.close();
            if(counter<20) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Show_score.fxml"));
                Parent root = loader.load();
                Show_scoreController showController = loader.getController();
                showController.store_username(login);
                showController.get_error(counter);
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
            else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Test_closed.fxml"));
                Parent root = loader.load();
                Test_closedController Test_closedController = loader.getController();
                Test_closedController.store_username(login);
                Test_closedController.fill_test(sub, typ);
                Test_closedController.gen_number();
                Test_closedController.fill_first_quest();
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
            }
            else
            {
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Brak polaczenia z serwerem");
                }
                dos.writeInt(27);
                dos.writeUTF(sub);
                dos.writeUTF(typ);
                counter=dis.readInt();
                dis.close();
                dos.close();
                s.close();
                if(counter<20) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Show_score.fxml"));
                    Parent root = loader.load();
                    Show_scoreController showController = loader.getController();
                    showController.store_username(login);
                    showController.get_error(counter);
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }
                else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Test_opened.fxml"));
                    Parent root = loader.load();
                    Test_openedController test_openedController = loader.getController();
                    test_openedController.store_username(login);
                    test_openedController.fill_test(sub, typ);
                    test_openedController.gen_number();
                    test_openedController.fill_first_quest();
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }
            }
        }
    }
}

