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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.Storage;

/**Klasa controlera startowego- logowanie*/
public class LoginController extends Storage implements Initializable {
    private String st;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private Button button;
    @FXML
    private AnchorPane AnchorPaneMain;
    @FXML
    private StackPane parentContainer;
    @FXML
    private TextField loginn;
    @FXML
    private PasswordField password;
    @FXML
    private Label status;
    /**Metoda inicjalizacji okna*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
    }

    /**Metoda przejscia do okna rejestracji
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    private void loadSecond(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Register.fxml"));
        Scene scene = button.getScene();
        root.translateYProperty().set(scene.getHeight());
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(AnchorPaneMain);
        });
        timeline.play();
    }
    /**Metoda przejscia do menu
     *
     *@param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    public void go_menu(ActionEvent event) throws IOException {
        try {
            while (true) {
                ip = InetAddress.getByName("localhost");
                s = new Socket(ip, 5057);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(1);
                dos.writeUTF(loginn.getText());
                dos.writeUTF(password.getText());
                st = dis.readUTF();
                System.out.println(st);
                status.setText(st);
                if (st.equals("Poprawne dane")) {
                    Thread.sleep(300);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/menu.fxml"));
                    Parent root = loader.load();
                    MenuController menuController = loader.getController();
                    menuController.store_username(loginn.getText());
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                    dis.close();
                    dos.close();
                    s.close();
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









}