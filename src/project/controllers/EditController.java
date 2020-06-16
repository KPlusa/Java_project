package project.controllers;

import javafx.animation.PauseTransition;
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
import javafx.collections.FXCollections;
import javafx.util.Duration;
import project.Storage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "edycja" dostepnej z poziomu menu*/
public class EditController extends Storage implements Initializable {
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int counter;
    private String tmp;
    private String receiver ,value,type;
    private ObservableList mylist = FXCollections.observableArrayList();
    private ObservableList myTypelist= FXCollections.observableArrayList();
    private ObservableList myQMlist= FXCollections.observableArrayList();

    @FXML
    private Label status;
    @FXML
    public ComboBox subject_choice=new ComboBox(mylist);
    @FXML
    public ComboBox type_choice;
    @FXML
    public ComboBox QM_choice;
/**Metoda wypelniajaca ComboBox nazwami przedmiotow*/
    @FXML
    private void fill_combo_subject() throws IOException {
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
                for(int i=0;i<counter;i++) {
                    receiver =dis.readUTF();
                    System.out.println("Otrzymano: "+receiver);
                    mylist.add(receiver);
                }
                subject_choice.setItems(mylist);
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
    }
    /**Metoda wypelniajaca ComboBox typami przedmiotow o wybranej nazwie*/
    @FXML
    private void fill_combo_type() throws IOException {
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
                dos.writeUTF(subject_choice.getValue().toString());
                counter = dis.readInt();
                System.out.println("Licznik typow: "+counter);
                for(int i=0;i<counter;i++) {
                    tmp =dis.readUTF();
                    System.out.println("Otrzymano: "+tmp);
                    myTypelist.add(tmp);
                }
                type_choice.setItems(myTypelist);
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }

    }
    /**Metoda wypelniajaca ComboBox dostepnymi zasobami(pytania/materialy) dla wybranej nazwy i typu przedmiotu*/
    @FXML
    private void fill_combo_question_and_mat() throws IOException {
        myQMlist.clear();
        if (!subject_choice.getSelectionModel().isEmpty()&&!type_choice.getSelectionModel().isEmpty()) {
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
                    dos.writeInt(31);
                    dos.writeUTF(subject_choice.getValue().toString());
                    dos.writeUTF(type_choice.getValue().toString());
                    counter = dis.readInt();
                    System.out.println("Licznik: " + counter);
                    if (counter == 1)
                        myQMlist.add("PYTANIA");
                    else if (counter == 2)
                        myQMlist.add("MATERIALY");
                    else if (counter == 3) {
                        myQMlist.add("PYTANIA");
                        myQMlist.add("MATERIALY");

                    }
                    QM_choice.setItems(myQMlist);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                dis.close();
                dos.close();
                s.close();
            }
        }
    }

    /**Metoda odpowiada za usuniecie podanej wartosci(pytania/materialy) dla przedmiotu o wybranej nazwie i typie
     *
     * @param event odpowiada za uruchomienie metody po wcisnieciu przycisku usun
     */
    @FXML
    private void delete_qm(ActionEvent event) throws IOException {
        if (!QM_choice.getSelectionModel().isEmpty()) {
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
                    dos.writeInt(32);
                    dos.writeUTF(subject_choice.getValue().toString());
                    dos.writeUTF(type_choice.getValue().toString());
                    dos.writeUTF(QM_choice.getValue().toString());
                    tmp = dis.readUTF();
                    status.setText(tmp);
                    if (tmp.equals("Pomyslnie usunieto materialy")|| tmp.equals("Pomyslnie usunieto pytania")) {
                        subject_choice.setValue("");
                        type_choice.setValue("");
                        QM_choice.setValue("");
                        Thread.sleep(300);
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
            status.setText("Zaznacz co chcesz usunac");
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();
    }



    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        try {
            fill_combo_subject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**Metoda odpowiadajaca za przejscie do formatki "dodaj usun przedmiot"
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku*/
    @FXML
    public void go_add_delete_subject(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Add_delete_subject.fxml"));
        Parent root = loader.load();
        Add_delete_subjectController add_delete_subjectController = loader.getController();
        add_delete_subjectController.store_username(login);
        add_delete_subjectController.store_subject(value);
        add_delete_subjectController.store_type(type);
        add_delete_subjectController.fill_combo_subject();
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    /**Metoda odpowiadajaca za przejscie do formatki "edytuj materialy"
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku*/
    @FXML
    public void go_edit_mat(ActionEvent event) throws IOException {
        if(subject_choice.getSelectionModel().isEmpty())
            status.setText("Wybierz przedmiot");

        else if(type_choice.getSelectionModel().isEmpty()&&!subject_choice.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else {
            value = subject_choice.getSelectionModel().getSelectedItem().toString();
            type = type_choice.getSelectionModel().getSelectedItem().toString();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Edit_mat.fxml"));
            Parent root = loader.load();
            Edit_matController edit_matController = loader.getController();
            edit_matController.store_username(login);
            edit_matController.fill_table(value, type);
            edit_matController.store_subject(value);
            edit_matController.store_type(type);
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();
    }
    /**Metoda odpowiadajaca za przejscie do formatki "edytuj pytania zamkniete"
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku*/
    @FXML
    public void go_edit_questions(ActionEvent event) throws IOException {
        if(subject_choice.getSelectionModel().isEmpty())
            status.setText("Wybierz przedmiot");

        else if(type_choice.getSelectionModel().isEmpty()&&!subject_choice.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else {
            value = subject_choice.getSelectionModel().getSelectedItem().toString();
            type = type_choice.getSelectionModel().getSelectedItem().toString();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/EditQuestions.fxml"));
            Parent root = loader.load();
            EditQuestionsController editQuestionsController = loader.getController();
            editQuestionsController.store_username(login);
            editQuestionsController.fill_table(value, type);
            editQuestionsController.store_subject(value);
            editQuestionsController.store_type(type);
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        }
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();

    }
    /**Metoda odpowiadajaca za przejscie do formatki "edytuj pytania otwarte"
     * @param event pozwala na uruchomienie metody w momencie klikniecia przycisku*/
    @FXML
    public void go_edit_questions_opn(ActionEvent event) throws IOException {
        if(subject_choice.getSelectionModel().isEmpty())
            status.setText("Wybierz przedmiot");

        else if(type_choice.getSelectionModel().isEmpty()&&!subject_choice.getSelectionModel().isEmpty())
            status.setText("Wybierz rodzaj przedmiotu");
        else {
            value =subject_choice.getSelectionModel().getSelectedItem().toString();
            type = type_choice.getSelectionModel().getSelectedItem().toString();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/EditQuestionsopen.fxml"));
            Parent root = loader.load();
            EditQuestionsopenController editQuestionsopenController = loader.getController();
            editQuestionsopenController.store_username(login);
            editQuestionsopenController.fill_table(value,type);
            editQuestionsopenController.store_subject(value);
            editQuestionsopenController.store_type(type);
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(even ->
                status.setText("")
        );
        pause.play();
    }


}

