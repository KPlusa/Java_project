package project.controllers;

import javafx.beans.Observable;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import project.EditQC;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    private double x,y;
    private Stage stage;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int counter;
    private String receiver; private String value;
    private ObservableList mylist = FXCollections.observableArrayList();

    @FXML
    private Label status;
    @FXML
    private AnchorPane AnchorPaneMain;
    @FXML
    private ComboBox subject_choice=new ComboBox(mylist);
    @FXML
    private TableView<EditQC> QClosed;
    @FXML
    private TableColumn<EditQC,Integer> Id;
    @FXML
    private TableColumn<EditQC,String> Pytanie;
    @FXML
    private TableColumn<EditQC,String> OdpA;
    @FXML
    private TableColumn<EditQC,String> OdpB;
    @FXML
    private TableColumn<EditQC,String> OdpC;
    @FXML
    private TableColumn<EditQC,String> OdpD;

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

    @FXML
    private void closeAction(MouseEvent event){
        System.exit(0);
    }

    @FXML
    private void minAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void maxAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        if(stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setResizable(false);
        }
        else {
            stage.setMaximized(true);
            stage.setResizable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeDraggable();
        try {
            fill_combo_subject();
        } catch (IOException e) {
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
    public void go_add_delete_subject(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/Add_delete_subject.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_edit_mat(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/Edit_mat.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    @FXML
    public void go_edit_questions(ActionEvent event) throws IOException {

        value =subject_choice.getSelectionModel().getSelectedItem().toString();
        System.out.println("Wartosc: "+value);
        ObservableList<EditQC> EditQC_list =FXCollections.observableArrayList();
        ObservableList<Integer> id_OL = FXCollections.observableArrayList();
        ObservableList<String> questions_OL = FXCollections.observableArrayList();
        ObservableList<String> answer_a_OL = FXCollections.observableArrayList();
        ObservableList<String> answer_b_OL = FXCollections.observableArrayList();
        ObservableList<String> answer_c_OL = FXCollections.observableArrayList();
        ObservableList<String> answer_d_OL = FXCollections.observableArrayList();
        id_OL.clear();
        questions_OL.clear();
        answer_a_OL.clear();
        answer_b_OL.clear();
        answer_c_OL.clear();
        answer_d_OL.clear();
        try {
            while (true) {
                if(value.equals(""))
                    status.setText("Wybierz przedmiot");
                try {
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip, 5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setText("Brak polaczenia z serwerem");
                }

                dos.writeInt(7);
                dos.writeUTF(value);
                System.out.println("W comboboxie jest: "+value);
                int c=dis.readInt();
                counter=c;
                System.out.println("Licznik: "+c);
                for (int i = 0; i < counter; i++) {
                    int ID = dis.readInt();
                    id_OL.add(ID);
                    System.out.println("Otrzymano: "+ID+"\t\n");
                }
                for (int i = 0; i < counter; i++) {
                    String question = dis.readUTF();
                    questions_OL.add(question);
                    System.out.println("Otrzymano: "+question+"\t\n");

                }
                for (int i = 0; i < counter; i++) {

                    String answer_a = dis.readUTF();
                    answer_a_OL.add(answer_a);
                    System.out.println("Otrzymano: "+answer_a+"\t\n");

                }
                for (int i = 0; i < counter; i++) {

                    String answer_b = dis.readUTF();

                    answer_b_OL.add(answer_b);
                    System.out.println("Otrzymano: "+answer_b+"\t\n");

                }
                for (int i = 0; i < counter; i++) {

                    String answer_c = dis.readUTF();
                    answer_c_OL.add(answer_c);
                    System.out.println("Otrzymano: "+answer_c+"\t\n");

                }
                for (int i = 0; i < counter; i++) {
                    String answer_d = dis.readUTF();
                    answer_d_OL.add(answer_d);
                    System.out.println("Otrzymano: "+answer_d+"\t\n");
                }
                for(int i = 0; i < counter; i++ )
                {
                    EditQC eqc= new EditQC();
                    eqc.set_id(id_OL.get(i));
                    eqc.set_pytanie(questions_OL.get(i));
                    eqc.set_Podp_a(answer_a_OL.get(i));
                    eqc.set_Podp_b(answer_b_OL.get(i));
                    eqc.set_Podp_c(answer_c_OL.get(i));
                    eqc.set_Podp_d(answer_d_OL.get(i));
                    EditQC_list.add(eqc);
                }

                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

            Parent parent = FXMLLoader.load(getClass().getResource("../fxml/EditQuestions.fxml"));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);

            window.show();

    }
    @FXML
    public void go_edit_questions_opn(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../fxml/EditQuestionsopen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void makeDraggable()
    {
        AnchorPaneMain.setOnMousePressed(((event) -> {
            x=event.getSceneX();
            y=event.getSceneY();
        }));

        AnchorPaneMain.setOnMouseDragged(((event) -> {
            stage= (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX()-x);
            stage.setY(event.getScreenY()-y);
        }));
    }

}

