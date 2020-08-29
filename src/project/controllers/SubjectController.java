package project.controllers;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Storage;
import project.SubjectClass;

/**Klasa controlera dla zakladki "przedmiot" dostepnej z poziomu menu*/
public class SubjectController extends Storage implements Initializable {
    private int counter;
    private Socket s;
    private String name;
    private String type;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    TableView<SubjectClass>table;
    @FXML
    private TableColumn<SubjectClass,String> ColSubName;
    @FXML
    private TableColumn<SubjectClass,String> ColSubType;

    /**Metoda wypelniajaca TableView odpowiednimi wartosciami
     *
     * @return SubjectClass
     * @throws IOException wyjatek
     */
    @FXML
    private ObservableList<SubjectClass> fill_table() throws IOException{
        ObservableList<SubjectClass> subject = FXCollections.observableArrayList();
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
                for(int i=0;i<counter;i++) {
                    name=dis.readUTF();
                    type=dis.readUTF();
                    subject.add(new SubjectClass(name,type));
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
        dis.close();
        dos.close();
        s.close();
        return subject;
    }
    /**Metoda inicjalizacji okna oraz wywolujaca metody wypelniajace kontenery*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable();
        ColSubName.setMinWidth(200);
        ColSubType.setMinWidth(200);
        ColSubName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        ColSubType.setCellValueFactory(new PropertyValueFactory<>("rodzaj"));
        try {
            table.setItems(fill_table());
        }
        catch (Exception e) {
        }
    }

}