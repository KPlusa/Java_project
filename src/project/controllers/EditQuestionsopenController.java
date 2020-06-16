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
import project.EditQO;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla opcji "pytania otwarte" w zakladce "edytuj"*/
public class EditQuestionsopenController extends Storage implements Initializable {
    private String tmp;
    private int counter = 0;
    private Integer ID;
    private String id,pytanie, odp;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private TableView<EditQO> Qopened;
    @FXML
    private javafx.scene.control.TableColumn<EditQO, Integer> Id;
    @FXML
    private javafx.scene.control.TableColumn<EditQO, String> Pytanie;
    @FXML
    private javafx.scene.control.TableColumn<EditQO, String> Odp;
    @FXML
    private TextField IDC;
    @FXML
    private TextArea TEXT;
    @FXML
    private TextArea A;
    @FXML
    private Label status;

    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Pytanie.setCellValueFactory(new PropertyValueFactory<>("pytanie"));
        Odp.setCellValueFactory(new PropertyValueFactory<>("odp"));
        Qopened.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                onEdit();
            }
        });

    }
    /**Metoda odpowiadajaca za powrót do poprzedniej formatki
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku*/
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
    /**Metoda odczytuje wartosc z tableview do pol TextField*/
    private void onEdit() {
        if (Qopened.getSelectionModel().getSelectedItem() != null) {
            EditQO editQo = Qopened.getSelectionModel().getSelectedItem();
            IDC.setText(String.valueOf(editQo.getId()));
            TEXT.setText(editQo.getPytanie());
            A.setText(editQo.getOdp());
        }
    }
    /**Metoda wypelnia liste elementami o podanej nazwie przedmiotu oraz typie
     *
     * @param a nazwa przedmiotu
     * @param b typ przedmiotu
     * @return EditQC_list lista uzyta do wypelnienia tabeli
     */
    @FXML
    public ObservableList<EditQO> fill_table(String a, String b) throws IOException {
        System.out.println("W metodzie w editqo mam: " + a);
        ObservableList<EditQO> EditQO_list = FXCollections.observableArrayList();
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
                dos.writeInt(17);
                dos.writeUTF(a);
                dos.writeUTF(b);
                counter = dis.readInt();
                System.out.println("Licznik: " + counter);
                for (int i = 1; i <= counter; i++) {
                    id=dis.readUTF();
                    ID=Integer.valueOf(id);
                    System.out.println("ID: " + ID);
                    pytanie = dis.readUTF();
                    System.out.println("Pytanie: " + pytanie);
                    odp = dis.readUTF();
                    System.out.println("odp: " + odp);
                    EditQO_list.add(new EditQO(ID, pytanie, odp));
                }
                Qopened.setItems(EditQO_list);
                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EditQO_list;
    }
    /**Metoda odpowiadajaca za dodanie rekordu do bazy
     *
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku dodaj
     */
    @FXML
    private void insert(ActionEvent event) throws IOException {
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
                dos.writeInt(24);
                dos.writeInt(Integer.valueOf(IDC.getText()));
                dos.writeUTF(TEXT.getText());
                dos.writeUTF(A.getText());
                dos.writeUTF(subject_storage);
                dos.writeUTF(type_storage);
                tmp=dis.readUTF();
                status.setText(tmp);
                if (tmp.equals("Pomyslnie dodano")) {
                    IDC.setText("");
                    TEXT.setText("");
                    A.setText("");
                    Thread.sleep(300);
                }
                fill_table(subject_storage,type_storage);
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
    /**Metoda aktualizuje rekord podany przez uzytkownika w bazie
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku aktualizuj*/
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
                dos.writeInt(25);
                dos.writeInt(Integer.valueOf(IDC.getText()));
                dos.writeUTF(TEXT.getText());
                dos.writeUTF(A.getText());
                dos.writeUTF(subject_storage);
                dos.writeUTF(type_storage);
                tmp=dis.readUTF();
                status.setText(tmp);
                if(tmp.equals("Pomyslnie zaktualizowano")) {
                    IDC.setText("");
                    TEXT.setText("");
                    A.setText("");
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
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku usun*/
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
                dos.writeInt(16);
                dos.writeInt(Integer.valueOf(IDC.getText()));
                tmp=dis.readUTF();
                status.setText(tmp);
                if(tmp.equals("Pomyslnie usunieto")) {
                    IDC.setText("");
                    TEXT.setText("");
                    A.setText("");
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

