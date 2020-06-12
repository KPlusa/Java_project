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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterController implements Initializable {
    private double x, y;
    private String l;
    private String p;
    private String mmail;
    private Socket s;
    private String st;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Stage stage;
    @FXML
    private Button button;
    @FXML
    private AnchorPane container;
    @FXML
    private TextField username;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField pass2;
    @FXML
    private TextField mail;
    @FXML
    private Label status;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeDraggable();
    }
    @FXML
    public void go_register(ActionEvent event) throws IOException {
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
                dos.writeInt(2);
                dos.writeUTF(username.getText());
                dos.writeUTF(pass.getText());
                dos.writeUTF(pass2.getText());
                dos.writeUTF(mail.getText());
                st = dis.readUTF();
                System.out.println(st);
                status.setText(st);
                if(st.equals("Zarejestrowano"))
                {
                    username.setText("");
                    pass.setText("");
                    pass2.setText("");
                    mail.setText("");
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

    @FXML
    private void loadThird(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        Scene scene = button.getScene();
        root.translateXProperty().set(scene.getWidth());

        StackPane parentContainer = (StackPane) button.getScene().getRoot();

        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            parentContainer.getChildren().remove(container);
        });
        timeline.play();
    }

    @FXML
    private void closeAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void minAction(MouseEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void makeDraggable()
    {
        container.setOnMousePressed(((event) -> {
            x=event.getSceneX();
            y=event.getSceneY();
        }));

        container.setOnMouseDragged(((event) -> {
            stage= (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX()-x);
            stage.setY(event.getScreenY()-y);
        }));
    }
}