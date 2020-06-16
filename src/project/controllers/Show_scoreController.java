package project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import project.Storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
/**Klasa controlera dla zakladki "wynik" wywolywanej po rozwiazaniu testu lub w przypadku braku pytan dla danego typu testu*/
public class Show_scoreController extends Storage implements Initializable {
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    private Label error_msg;
    @FXML
    private Text Questions;
    @FXML
    private Text points;
    @FXML
    private Text per;
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
    }

    /** Metoda ustawia wartosc pola TextField w wypadku braku dostepnych pytan dla testy
     *
     * @param tmp dostepne pytania
     */
    public void get_error(int tmp){
        error_msg.setText("Dostępne "+tmp+" pytan na 20, test nie aktywowany oraz nie zapisany.");
    }

    /** Metoda ustawia wartosc pol TextField po rozwiazanym tescie
     *
     * @param imp wynik testu
     * @param sub nazwa przedmiotu
     * @param typ typ przedmiotu
     */
    public void pkt (int imp,String sub,String typ) throws IOException {
        Questions.setText("20");
        points.setText(String.valueOf(imp));
        float procent=imp;
        procent=procent/20;
        procent=procent*100;
        procent=Math.round(procent);
        per.setText(String.valueOf(procent)+"%");
        try {
            ip = InetAddress.getByName("localhost");
            s = new Socket(ip, 5057);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Brak polaczenia z serwerem");
        }
        DateTimeFormatter data = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(data.format(now));
        dos.writeInt(18);
        dos.writeUTF(login);
        System.out.println(sub);
        dos.writeUTF(sub);
        dos.writeUTF(typ);
        dos.writeInt(imp);
        dos.writeUTF(data.format(now).toString());
        dis.close();
        dos.close();
        s.close();
    }

}

