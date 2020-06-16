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
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.Storage;
import project.Testclosedclass;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
/**Klasa controlera dla zakladki "test zamkniety" dostepnej po poprawnym wygenerowaniu testu*/
public class Test_closedController extends Storage implements Initializable {
    private String subject,type;
    private int counter;
    private Socket s;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String tresc, odp_a, odp_b, odp_c,odp_d;
    private int popr_o,id,obecne=0;
    private int[] ilosc=new int[20];
    private int[] pkt={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int obecna_poprawna;
    private int suma_punktow=0;
    private int[] was_selected={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    final ObservableList<Testclosedclass> Testclosed = FXCollections.observableArrayList();
    Random generator = new Random();
    @FXML
    private Label question;
    @FXML
    private Label ans_a;
    @FXML
    private Label ans_b;
    @FXML
    private Label ans_d;
    @FXML
    private Label ans_c;
    @FXML
    private Label error_msg;
    @FXML
    private Label nr_pyt;
    @FXML
    private RadioButton chb_a;
    @FXML
    private RadioButton chb_b;
    @FXML
    private RadioButton chb_c;
    @FXML
    private RadioButton chb_d;
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
    public ObservableList<Testclosedclass> fill_test(String sub, String typ) throws IOException {
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
        dos.writeInt(19);
        dos.writeUTF(sub);
        dos.writeUTF(typ);
        counter=dis.readInt();
        for(int i=0;i<counter;i++)
        {
            id=dis.readInt();
            id++;
            tresc = dis.readUTF();
            odp_a = dis.readUTF();
            odp_b = dis.readUTF();
            odp_c = dis.readUTF();
            odp_d = dis.readUTF();
            popr_o = dis.readInt();
            Testclosed.add(new Testclosedclass(id,tresc, odp_a, odp_b, odp_c,odp_d,popr_o));
        }
        dis.close();
        dos.close();
        s.close();
        return Testclosed;
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
    /**Metoda umieszcza dane dotyczace pierwszego pytania w formatce pytania zamknietego*/
    @FXML
    public void fill_first_quest() throws IOException
    {
        chb_a.setVisible(true);
        chb_b.setVisible(true);
        chb_c.setVisible(true);
        chb_d.setVisible(true);
        nr_pyt.setText("1");
        for(Testclosedclass cl:Testclosed)
        {
            if(cl.getId()==ilosc[0])
            {
                question.setText(cl.getTresc());
                ans_a.setText(cl.getOdp_a());
                if(ans_a.equals(""))
                {
                    chb_a.setVisible(false);
                }
                ans_b.setText(cl.getOdp_b());
                if(ans_b.equals(""))
                {
                    chb_b.setVisible(false);
                }
                ans_c.setText(cl.getOdp_c());
                if(ans_c.equals(""))
                {
                    chb_c.setVisible(false);
                }
                ans_d.setText(cl.getOdp_d());
                if(ans_d.equals(""))
                {
                    chb_d.setVisible(false);
                }
                obecna_poprawna=cl.getOdp_popr();
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
        chb_a.setVisible(true);
        chb_b.setVisible(true);
        chb_c.setVisible(true);
        chb_d.setVisible(true);
        boolean c1=chb_a.isSelected();
        int cs1=0;
        if(c1)
        {cs1=1;}
        boolean c2=chb_b.isSelected();
        int cs2=0;
        if(c2)
        {cs2=2;}
        boolean c3=chb_c.isSelected();
        int cs3=0;
        if(c3)
        {cs3=3;}
        boolean c4=chb_d.isSelected();
        int cs4 = 0;
        if(c4)
        {cs4=4;}
        cs4=cs4+cs3+cs2+cs1;
        was_selected[obecne]=cs4;
        if(obecna_poprawna==cs4) {
            pkt[obecne]=1;
        }
        else {
            pkt[obecne]=0;
        }
        chb_a.setSelected(false);
        chb_b.setSelected(false);
        chb_c.setSelected(false);
        chb_d.setSelected(false);
        if(obecne==19)
        {
            error_msg.setText("To ostatnie pytanie");

        }
       else {
            if(obecne!=19){obecne++;}
            for (Testclosedclass cl : Testclosed) {
            if (cl.getId() == ilosc[obecne]) {
                    error_msg.setText("");
                    question.setText(cl.getTresc());
                ans_a.setText(cl.getOdp_a());
                if(ans_a.equals(""))
                {
                    chb_a.setVisible(false);
                }
                ans_b.setText(cl.getOdp_b());
                if(ans_b.equals(""))
                {
                    chb_b.setVisible(false);
                }
                ans_c.setText(cl.getOdp_c());
                if(ans_c.equals(""))
                {
                    chb_c.setVisible(false);
                }
                ans_d.setText(cl.getOdp_d());
                if(ans_d.equals(""))
                {
                    chb_d.setVisible(false);
                }
                    obecna_poprawna = cl.getOdp_popr();
                }
            }
        }
       if(was_selected[obecne]==1)
       {
           chb_a.setSelected(true);
       }
        if(was_selected[obecne]==2)
        {
            chb_b.setSelected(true);
        }
        if(was_selected[obecne]==3)
        {
            chb_c.setSelected(true);
        }
        if(was_selected[obecne]==4)
        {
            chb_d.setSelected(true);
        }

        nr_pyt.setText(String.valueOf(obecne+1));
    }
    /**Metoda zapewnia przejscie do poprzedniego pytania
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    private void go_left(MouseEvent event) throws IOException
    {
        chb_a.setVisible(true);
        chb_b.setVisible(true);
        chb_c.setVisible(true);
        chb_d.setVisible(true);
        boolean c1=chb_a.isSelected();
        int cs1=0;
        if(c1==true)
        {cs1=1;}
        boolean c2=chb_b.isSelected();
        int cs2=0;
        if(c2==true)
        {cs2=2;}
        boolean c3=chb_c.isSelected();
        int cs3=0;
        if(c3==true)
        {cs3=3;}
        boolean c4=chb_d.isSelected();
        int cs4 = 0;
        if(c4==true)
        {cs4=4;}
        cs4=cs4+cs3+cs2+cs1;
        was_selected[obecne]=cs4;
        if(obecna_poprawna==cs4) {
            pkt[obecne]=1;
        }
        else {
            pkt[obecne]=0;
        }
        chb_a.setSelected(false);
        chb_b.setSelected(false);
        chb_c.setSelected(false);
        chb_d.setSelected(false);
        if(obecne==0)
        {
            error_msg.setText("To pierwsze pytanie");
        }
        else {
            if(obecne!=0) {
                obecne--;
            }
            for (Testclosedclass cl : Testclosed) {

                if (cl.getId() == ilosc[obecne]) {
                    error_msg.setText("");
                    question.setText(cl.getTresc());
                    ans_a.setText(cl.getOdp_a());
                    if(ans_a.equals(""))
                    {
                        chb_a.setVisible(false);
                    }
                    ans_b.setText(cl.getOdp_b());
                    if(ans_b.equals(""))
                    {
                        chb_b.setVisible(false);
                    }
                    ans_c.setText(cl.getOdp_c());
                    if(ans_c.equals(""))
                    {
                        chb_c.setVisible(false);
                    }
                    ans_d.setText(cl.getOdp_d());
                    if(ans_d.equals(""))
                    {
                        chb_d.setVisible(false);
                    }
                    obecna_poprawna = cl.getOdp_popr();
                }
            }
        }
        if(was_selected[obecne]==1)
        {
            chb_a.setSelected(true);
        }
        if(was_selected[obecne]==2)
        {
            chb_b.setSelected(true);
        }
        if(was_selected[obecne]==3)
        {
            chb_c.setSelected(true);
        }
        if(was_selected[obecne]==4)
        {
            chb_d.setSelected(true);
        }
        nr_pyt.setText(String.valueOf(obecne+1));
    }
    /**Metoda zapewnia zakonczenie testu oraz przejscie do formatki "Wynik"
     *
     * @param event parametr zapewnia wywolanie metody po nacisnieciu przycisku
     */
    @FXML
    private void end_test(MouseEvent event) throws IOException
    {
        boolean c1=chb_a.isSelected();
        int cs1=0;
        if(c1)
        {cs1=1;}
        boolean c2=chb_b.isSelected();
        int cs2=0;
        if(c2)
        {cs2=2;}
        boolean c3=chb_c.isSelected();
        int cs3=0;
        if(c3)
        {cs3=3;}
        boolean c4=chb_d.isSelected();
        int cs4 = 0;
        if(c4)
        {cs4=4;}
        cs4=cs4+cs3+cs2+cs1;
        if(obecna_poprawna==cs4) {
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

