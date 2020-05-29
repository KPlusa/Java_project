package project.controllers;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.Materialsclass;
import project.Subjectclass;


public class SubjectController implements Initializable {
    private String st;
    private int counter;
    private Socket s;
    private String subject;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;

    private double x,y;
    private Stage stage;


    @FXML
    private TableColumn<String, Subjectclass> ColSubName;
    @FXML
    private TableColumn<String, Subjectclass> ColSubType;
    @FXML
    public TableView subjectTable = new TableView();
    @FXML
    private TableView<Subjectclass> table;

    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private AnchorPane AnchorPaneMain;
    final ObservableList mylist = FXCollections.observableArrayList();
    public ObservableList<Subjectclass> list = FXCollections.observableArrayList();

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

    @FXML
    private void fill_table() throws IOException{
        mylist.clear();
        try{
            while(true){
                try{
                    ip = InetAddress.getByName("localhost");
                    s = new Socket(ip,5057);
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());

                } catch (Exception e){
                    e.printStackTrace();
                }
                dos.writeInt(4);
                counter = dis.readInt();
                mylist.add(dis.readUTF());
                for(int i=0;i<counter;i++) {
                    subject=dis.readUTF();
                  //  mylist.add();
                }

                break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            dis.close();
            dos.close();
            s.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeDraggable();

        try {
            fill_table();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ColSubName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColSubType.setCellValueFactory(new PropertyValueFactory<>("rodzaj"));

        // ColSubName.setCellValueFactory(cellData-> cellData.getValue().getSubjectName());
        //ColSubType.setCellValueFactory(cellData-> cellData.getValue().getSubjectType());

        //makeDraggable();

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