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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.EditMatClass;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla opcji "edytuj material" w zakladce "edytuj"*/
public class Edit_matController extends Storage implements Initializable {
    private String tmp;
    private int counter = 0;
    private Integer ID;
    private String id,przedmiot, typ_przedmiotu, tresc;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private TableView<EditMatClass> EditMat;
    @FXML
    private javafx.scene.control.TableColumn<EditMatClass, String> Tresc;
    @FXML
    private TextArea TEXT;
    @FXML
    private Label status;
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        Tresc.setCellValueFactory(new PropertyValueFactory<>("tresc"));

        EditMat.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                onEdit();
            }
        });

    }
    /**Metoda odpowiadajaca za powrót do poprzedniej formatki
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku
     * @throws IOException wyjatek*/
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

    /**Metoda odczytuje wartosc z tableview do pola TextField*/
    private void onEdit() {
        if (EditMat.getSelectionModel().getSelectedItem() != null) {
            EditMatClass editMatClass = EditMat.getSelectionModel().getSelectedItem();
            TEXT.setText(editMatClass.getTresc());
        }
    }

    /**Metoda wypelnia tablice ktora zawiera elementy wyswietlane w tabeli
     *
     * @param a nazwa przedmiotu
     * @param b rodzaj przedmiotu
     * @return EditMatClass
     * @throws IOException wyjatek
     */
    @FXML
    public ObservableList<EditMatClass> fill_table(String a, String b) throws IOException {
        ObservableList<EditMatClass> EditMat_list = FXCollections.observableArrayList();
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
                dos.writeInt(26);
                dos.writeUTF(a);
                dos.writeUTF(b);
                counter = dis.readInt();
                System.out.println("Licznik: " + counter);
                for (int i = 1; i <= counter; i++) {
                    id=dis.readUTF();
                    ID=Integer.valueOf(id);
                    System.out.println("ID: " + ID);
                    przedmiot = dis.readUTF();
                    System.out.println("Pytanie: " + przedmiot);
                    typ_przedmiotu = dis.readUTF();
                    System.out.println("odp: " + typ_przedmiotu);
                    System.out.println("tresc: " + tresc);
                    tresc = dis.readUTF();
                    EditMat_list.add(new EditMatClass(ID, przedmiot, typ_przedmiotu,tresc));
                }

                EditMat.setItems(EditMat_list);
                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EditMat_list;
    }
    /**Metoda odpowiadajaca za dodanie rekordu do bazy
     *
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku dodaj
     * @throws IOException wyjatek
     */
    @FXML
    private void insert(ActionEvent event) throws IOException {
        if (!TEXT.getText().isEmpty()) {
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
                    dos.writeInt(28);
                    dos.writeUTF(TEXT.getText());
                    dos.writeUTF(subject_storage);
                    dos.writeUTF(type_storage);
                    tmp = dis.readUTF();
                    status.setText(tmp);
                    if (tmp.equals("Pomyslnie dodano")) {
                        TEXT.setText("");
                        Thread.sleep(300);
                        fill_table(subject_storage, type_storage);
                    }
                    break;
                }
                dis.close();
                dos.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            status.setText("Wprowadz tresc materialu");
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();
    }
    /**Metoda aktualizuje rekord podany przez uzytkownika w bazie
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku aktualizuj
     * @throws IOException wyjatek*/
    @FXML
    private void update(ActionEvent event) throws IOException {
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

                dos.writeInt(29);
                if (EditMat.getSelectionModel().getSelectedItem() != null) {
                    EditMatClass editMatClass = EditMat.getSelectionModel().getSelectedItem();
                    tmp=editMatClass.getTresc();
                }
                else tmp="";
                dos.writeUTF(tmp);
                dos.writeUTF(TEXT.getText());
                dos.writeUTF(subject_storage);
                dos.writeUTF(type_storage);
                tmp=dis.readUTF();
                status.setText(tmp);
                if(tmp.equals("Pomyslnie zaktualizowano")) {
                    TEXT.setText("");
                    Thread.sleep(300);
                    fill_table(subject_storage,type_storage);
                }
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(even ->
                        status.setText("")
                );
                pause.play();
                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**Metoda usuwa rekord podany przez uzytkownika z bazy
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku usun
     *@throws IOException wyjatek*/
    @FXML
    private void delete(ActionEvent event) throws IOException {
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
                dos.writeInt(30);
                dos.writeUTF(TEXT.getText());
                tmp=dis.readUTF();
                status.setText(tmp);
                if(tmp.equals("Pomyslnie usunieto")) {
                    TEXT.setText("");
                    Thread.sleep(300);
                    fill_table(subject_storage,type_storage);
                }
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(even ->
                        status.setText("")
                );
                pause.play();
                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

