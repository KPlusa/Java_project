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
import project.EditQC;
import project.Storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import java.util.ResourceBundle;

public class EditQuestionsController extends Storage implements Initializable {
    private String tmp;
    private int temp=0;
    private int counter = 0;
    private Integer ID, ra;
    private String id,pytanie, odp_a, odp_b, odp_c, odp_d,right_answer;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private TableView<EditQC> QClosed;
    @FXML
    private javafx.scene.control.TableColumn<EditQC, Integer> Id;
    @FXML
    private javafx.scene.control.TableColumn<EditQC, String> Pytanie;
    @FXML
    private javafx.scene.control.TableColumn<EditQC, String> OdpA;
    @FXML
    private javafx.scene.control.TableColumn<EditQC, String> OdpB;
    @FXML
    private javafx.scene.control.TableColumn<EditQC, String> OdpC;
    @FXML
    private TableColumn<EditQC, String> OdpD;
    @FXML
    private TextField IDC;
    @FXML
    private TextArea TEXT;
    @FXML
    private TextField AA;
    @FXML
    private TextField AB;
    @FXML
    private TextField AC;
    @FXML
    private TextField AD;
    @FXML
    private RadioButton RA;
    @FXML
    private RadioButton RB;
    @FXML
    private RadioButton RC;
    @FXML
    private RadioButton RD;
    @FXML
    private Label status;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        Pytanie.setCellValueFactory(new PropertyValueFactory<>("pytanie"));
        OdpA.setCellValueFactory(new PropertyValueFactory<>("odp_a"));
        OdpB.setCellValueFactory(new PropertyValueFactory<>("odp_b"));
        OdpC.setCellValueFactory(new PropertyValueFactory<>("odp_c"));
        OdpD.setCellValueFactory(new PropertyValueFactory<>("odp_d"));
        QClosed.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                onEdit();
            }
        });
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

    private void onEdit() {
        if (QClosed.getSelectionModel().getSelectedItem() != null) {
            EditQC editQC = QClosed.getSelectionModel().getSelectedItem();
            IDC.setText(String.valueOf(editQC.getId()));
            TEXT.setText(editQC.getPytanie());
            AA.setText(editQC.getOdp_a());
            AB.setText(editQC.getOdp_b());
            AC.setText(editQC.getOdp_c());
            AD.setText(editQC.getOdp_d());
            if(editQC.getRight_answer()==1) {
                RA.setSelected(true);
                RB.setSelected(false);
                RC.setSelected(false);
                RD.setSelected(false);
            }
            else if(editQC.getRight_answer()==2) {
                RA.setSelected(false);
                RB.setSelected(true);
                RC.setSelected(false);
                RD.setSelected(false);
            }
            else if(editQC.getRight_answer()==3) {
                RA.setSelected(false);
                RB.setSelected(false);
                RC.setSelected(true);
                RD.setSelected(false);
            }

            else if(editQC.getRight_answer()==4){
                RA.setSelected(false);
                RB.setSelected(false);
                RC.setSelected(false);
                RD.setSelected(true);
            }

            else {
                RA.setSelected(false);
                RB.setSelected(false);
                RC.setSelected(false);
                RD.setSelected(false);
            }




            System.out.println("Correct: "+editQC.getRight_answer());
        }
    }

    @FXML
    public ObservableList<EditQC> fill_table(String a,String b) throws IOException {
        System.out.println("W metodzie w editq mam: " + a);
        ObservableList<EditQC> EditQC_list = FXCollections.observableArrayList();
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
                dos.writeInt(7);
                dos.writeUTF(a);
                dos.writeUTF(b);
                counter = dis.readInt();
                for (int i = 1; i <= counter; i++) {
                    id=dis.readUTF();
                    ID=Integer.valueOf(id);
                    System.out.println("ID: " + ID);
                    pytanie = dis.readUTF();
                    System.out.println("Pytanie: " + pytanie);
                    odp_a = dis.readUTF();
                    System.out.println("odp_a: " + odp_a);
                    odp_b = dis.readUTF();
                    System.out.println("odp_b: " + odp_b);
                    odp_c = dis.readUTF();
                    System.out.println("odp_c: " + odp_c);
                    odp_d = dis.readUTF();
                    System.out.println("odp_d: " + odp_d);
                    right_answer=dis.readUTF();
                    ra=Integer.valueOf(right_answer);
                    System.out.println("right_answer: " + ra);
                    EditQC_list.add(new EditQC(ID, pytanie, odp_a, odp_b, odp_c, odp_d,ra));
                }
                QClosed.setItems(EditQC_list);
                break;
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EditQC_list;
    }

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
                dos.writeInt(14);
                dos.writeInt(Integer.valueOf(IDC.getText()));
                dos.writeUTF(TEXT.getText());
                dos.writeUTF(AA.getText());
                dos.writeUTF(AB.getText());
                dos.writeUTF(AC.getText());
                dos.writeUTF(AD.getText());
                dos.writeUTF(subject_storage);
                dos.writeUTF(type_storage);
                if(RA.isSelected())
                    temp=1;
                else if(RB.isSelected())
                    temp=2;
                else if(RC.isSelected())
                    temp=3;
                else if(RD.isSelected())
                    temp=4;
                dos.writeInt(temp);
                tmp=dis.readUTF();
                status.setText(tmp);
                if (tmp.equals("Pomyslnie dodano")) {
                    RA.setSelected(false);
                    RB.setSelected(false);
                    RC.setSelected(false);
                    RD.setSelected(false);
                    IDC.setText("");
                    TEXT.setText("");
                    AA.setText("");
                    AB.setText("");
                    AC.setText("");
                    AD.setText("");
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
                dos.writeInt(15);
                dos.writeInt(Integer.valueOf(IDC.getText()));
                dos.writeUTF(TEXT.getText());
                dos.writeUTF(AA.getText());
                dos.writeUTF(AB.getText());
                dos.writeUTF(AC.getText());
                dos.writeUTF(AD.getText());
                dos.writeUTF(subject_storage);
                if(RA.isSelected())
                    temp=1;
                else if(RB.isSelected())
                    temp=2;
                else if(RC.isSelected())
                    temp=3;
                else if(RD.isSelected())
                    temp=4;
                dos.writeInt(temp);
                dos.writeUTF(type_storage);
                tmp=dis.readUTF();
                if(tmp.equals("Pomyslnie zaktualizowano")) {
                    status.setText(tmp);
                    RA.setSelected(false);
                    RB.setSelected(false);
                    RC.setSelected(false);
                    RD.setSelected(false);
                    IDC.setText("");
                    TEXT.setText("");
                    AA.setText("");
                    AB.setText("");
                    AC.setText("");
                    AD.setText("");
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
                if(tmp.equals("Pomyslnie usunieto")) {
                    status.setText(tmp);
                    RA.setSelected(false);
                    RB.setSelected(false);
                    RC.setSelected(false);
                    RD.setSelected(false);
                    IDC.setText("");
                    TEXT.setText("");
                    AA.setText("");
                    AB.setText("");
                    AC.setText("");
                    AD.setText("");
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



