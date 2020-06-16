package project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import project.Storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "historia" dostepnej z poziomu menu*/
public class HistoryController extends Storage implements Initializable {

    private String test,percent;
    private String temp;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private Text Test;
    @FXML
    private Text score;
    @FXML
    DatePicker datePicker;
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datePicker.setConverter(converter);
    }

    /**Metoda wypelniajaca pola typu TextField odpowiednimi wartosciami
     *
     * @param event parametr zapewnia wywolanie metody po wybraniu daty
     */
    @FXML
    public void show_information(ActionEvent event) throws IOException {
        temp= datePicker.getValue().toString();
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
                dos.writeInt(9);
                dos.writeUTF(login);
                dos.writeUTF(temp);
                test=dis.readUTF();
                percent=dis.readUTF();
                Test.setText(test);
                score.setText(percent+"%");
                dis.close();
                dos.close();
                s.close();
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
}

