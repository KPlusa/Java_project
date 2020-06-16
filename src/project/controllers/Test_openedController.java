package project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.Storage;
import project.Testopenclass;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "test zamkniety" dostepnej po poprawnym wygenerowaniu testu*/
public class Test_openedController extends Storage implements Initializable {
    private int counter,id,obecne;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String subject,type,tresc,odp_o,ans;
    final ObservableList<Testopenclass> Testopen = FXCollections.observableArrayList();
    Random generator = new Random();
    private int[] ilosc=new int[20];
    private int[] pkt={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private String obecna_poprawna;
    private int suma_punktow=0;
    private String[] was_selected={"","","","","","","","","","","","","","","","","","","",""};
    @FXML
    private Label nr_pyt;
    @FXML
    private Label question;
    @FXML
    private Label error_msg;
    @FXML
    private TextArea odpowiedz;
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
    }
    /**Metoda pobiera dane dla testu
     *
     * @param sub nazwa przedmiotu
     * @param typ typ przedmiotu
     */
    public ObservableList<Testopenclass> fill_test(String sub, String typ) throws IOException {
        try {
            ip = InetAddress.getByName("localhost");
            s = new Socket(ip, 5057);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Brak polaczenia z serwerem");
        }
        subject=sub;
        type=typ;
        dos.writeInt(33);
        dos.writeUTF(sub);
        dos.writeUTF(typ);
        counter=dis.readInt();
        for(int i=0;i<counter;i++)
        {
            id=dis.readInt();
            id++;
            tresc = dis.readUTF();
            odp_o = dis.readUTF();
            Testopen.add(new Testopenclass(id,tresc,odp_o));
        }
        dis.close();
        dos.close();
        s.close();

        return Testopen;
    }
    /**Metoda generuje numery pytan uzytych do testu*/
    public void gen_number()throws IOException {
        for (int i = 0; i < 20; i++) {
            int num = generator.nextInt(counter) + 1;
            ilosc[i]=num;
            for (int j = 0; j <= i; j++) {
                if(i==0)
                {
                    break;
                }
                else if (ilosc[j] == num&&j!=i ) {
                    i--;
                    break;
                }
            }
        }
    }
    /**Metoda umieszcza dane dotyczace pierwszego pytania w formatce pytania otwartego*/
    @FXML
    public void fill_first_quest() throws IOException
    {
        nr_pyt.setText("1");
        for(Testopenclass cl:Testopen)
        {
            if(cl.getId()==ilosc[0])
            {
                question.setText(cl.getTresc());
                obecna_poprawna=cl.getOdp_o();
            }
        }
    }
    /**Metoda zapewnia przejscie do kolejnego pytania
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    private void go_right(MouseEvent event) throws IOException
    {
        ans=odpowiedz.getText();
        System.out.println(ans);
        was_selected[obecne]=ans;
        if(obecna_poprawna.equals(ans)) {
            pkt[obecne]=1;
        }
        else {
            pkt[obecne]=0;
        }

        if(obecne==19)
        {
            error_msg.setText("To ostatnie pytanie");
        }
        else {
            if(obecne!=19){obecne++;}
            for (Testopenclass cl : Testopen) {
                if (cl.getId() == ilosc[obecne]) {
                    error_msg.setText("");
                    question.setText(cl.getTresc());
                    obecna_poprawna = cl.getOdp_o();
                }
            }
        }
        odpowiedz.setText(was_selected[obecne]);
        nr_pyt.setText(String.valueOf(obecne+1));
    }
    /**Metoda zapewnia przejscie do poprzedniego pytania
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    private void go_left(MouseEvent event) throws IOException
    {
        ans=odpowiedz.getText();
        System.out.println(ans);
        was_selected[obecne]=ans;
        if(obecna_poprawna.equals(ans)) {
            pkt[obecne]=1;
        }
        else {
            pkt[obecne]=0;
        }

        if(obecne==0)
        {
            error_msg.setText("To pierwsze pytanie");
        }
        else {
            if(obecne!=0) {
                obecne--;
            }
            for (Testopenclass cl : Testopen) {
                if (cl.getId() == ilosc[obecne]) {
                    error_msg.setText("");
                    question.setText(cl.getTresc());
                    obecna_poprawna = cl.getOdp_o();
                }
            }
        }
        odpowiedz.setText(was_selected[obecne]);
        nr_pyt.setText(String.valueOf(obecne+1));
    }
    /**Metoda zapewnia zakonczenie testu oraz przejscie do formatki "Wynik"
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    private void end_test(MouseEvent event) throws IOException
    {
        ans=odpowiedz.getText();
        System.out.println(ans);
        if(obecna_poprawna.equals(ans)) {
            pkt[obecne]=1;
        }
        else {
            pkt[obecne]=0;
        }

        for(int i=0;i<20;i++)
        {
            suma_punktow=suma_punktow+pkt[i];
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Show_score.fxml"));
        Parent root = loader.load();
        Show_scoreController showController = loader.getController();
        showController.store_username(login);
        showController.pkt(suma_punktow,subject,type);
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}

