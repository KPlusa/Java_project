package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**Klasa ClientHandler zapewnia polaczenie pomiedzy klientem a serwerem*/
class ClientHandler extends Thread {
    int choice = 0;
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private int id=1;
    private String cs5;
    private String nick,date,test, percent; //Historia
    private String rcv; //Edycja
    private String tmp;
    private String st;
    private int tmpp=0;
    private String name_of_subject;
    private int counter=0;
    List<String> list=new ArrayList<String>();
    private String username;
    private String password;
    private String pass2;
    private String help;
    private String mail;
    private String ma,sub;
    private String receiver;
    private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String db_user = "DB_ADMIN";
    private static String db_pass = "qazwsx";
    private static Connection con;
    private Statement stmt;
    private ResultSet rs;
    private Object NullPointerException;
    private int o_z=0;
    private List<String> l=new ArrayList<String>();

    /**Konstruktor klasy ClientHandler
     *
     * @param s socket
     * @param dis input
     * @param dos output
     */
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }
/** Metoda odpowiadajaca za polaczenie z baza oraz odpowiedzi ze strony serwera*/
    @Override
    public void run() {
        ActionClientHandler actionClientHandler = new ActionClientHandler(s,dis, dos);
        actionClientHandler.connectToDB();

        while (true) {
            try {
                choice = dis.readInt();
                switch (choice) {
                    /**Case 1 odpowiada za sprawdzenie czy podane dane uzytkownika odpowiadaja istniejacemu uzytkownikowi w bazie*/
                    case 1:
                        actionClientHandler.Login();
                        break;
                    /**Case 2 odpowiada za dodanie nowego uzytkownika do bazy*/
                    case 2:
                        actionClientHandler.Register();
                        break;
                    /**Case 3 odpowiada za wypelnienie ComboBox w zakladce Materialy oraz w zakladce Ranking*/
                    case 3:
                        actionClientHandler.LoadMaterialCB();
                        break;
                    /**Case 4 odpowiada za wypelnienie TableView w zakladce Przedmiot*/
                    case 4:
                        actionClientHandler.LoadSubjectTableView();
                        break;
                    /**Case 5 odpowiada za wypelnienie TableView w zakladce Materialy dla wartosci wybranej w ComboBox*/
                    case 5:
                        actionClientHandler.LoadMaterialTableView();
                        break;
                        /**Case 6 pobiera dane do zakladki Edycja*/
                    case 6:
                        actionClientHandler.LoadEdit();
                        break;
                    /**Case 7 pobiera dane do zakladki Pytania Zamkniete w zakladce Edycja*/
                    case 7:
                        actionClientHandler.LoadEditClosed();
                        break;
                    /**Case 8 pobiera dane do zakladki Pytania*/
                    case 8:
                        actionClientHandler.LoadQuestions();
                        break;
                    /**Case 9 pobiera dane do zakladki Historia dla wybranej daty*/
                    case 9:
                        actionClientHandler.LoadHistory();
                        break;
                    /**Case 10 pobiera dane do zakladki Ranking*/
                    case 10:
                        actionClientHandler.LoadRank();
                        break;
                    /**Case 11 pobiera dane do zakladki Ranking dla podanego przedmiotu*/
                    case 11:
                        actionClientHandler.LoadSubjectToRank();
                        break;
                    /**Case 12 pobiera dane do zakladki Ranking dla podanego loginu*/
                    case 12:
                       actionClientHandler.LoadLoginToRank();
                        break;
                    /**Case 13 pobiera dane do zakladki Generuj Test dla podanego przedmiotu*/
                    case 13:
                        actionClientHandler.LoadGenerateTest();
                        break;
                    /**Case 14 dodaje wiersz do bazy podany w zakladce Pytania Zamkniete dostepnej w zakladce Edytuj*/
                    case 14:
                        actionClientHandler.AddQuestionClosed();
                        break;
                    /**Case 15 aktualizuje wiersz w bazie podany w zakladce Pytania Zamkniete dostepnej w zakladce Edytuj*/
                    case 15:
                        actionClientHandler.UpdateQuestionClosed();
                        break;
                    /**Case 16 usuwa wiersz w bazie podany w zakladce Pytania Zamkniete lub w zakladce Pytania Otwarte dostepnej w zakladce Edytuj*/
                    case 16:
                        actionClientHandler.DeleteQuestion();
                        break;
                    /**Case 17 wypelnia Tableview w zakladce Pytania Otwarte dostepnej w zakladce Edytuj danymi*/
                    case 17:
                        actionClientHandler.LoadEditOpened();
                        break;
                        /**Case 18 dodaje wiersz do bazy dla odpowiedniego uzytkownika i testu(zakladka Wynik)*/
                    case 18:
                        actionClientHandler.AddScore();
                        break;
                        /**Case 19 pobiera dane dla testu Pytan Zamknietych*/
                    case 19:
                        actionClientHandler.LoadTestClosed();
                        break;
                        /**Case 20 wypelnia ComboBox w zakladce Edytuj oraz Dodaj Usun Przedmiot dostepnej z jej poziomu typami przedmiotow dla odpowiedniej nazwy przedmiotu*/
                    case 20:
                        actionClientHandler.LoadSubjectToCB();
                        break;
                        /**Case 21 odpowiada za odczytanie wartosci ilosci pytan dla testu Pytan Zamknietych*/
                    case 21:
                        actionClientHandler.CountQuestionsClosed();
                        break;
                        /**Case 22 odpowiada za usuniecie przedmiotu w zakladce Dodaj Usun Przedmiot dostepnej z zakladki Edycja*/
                    case 22:
                        actionClientHandler.DeleteSubject();
                        break;
                    /**Case 23 odpowiada za dodanie przedmiotu w zakladce Dodaj Usun Przedmiot dostepnej z zakladki Edycja*/
                    case 23:
                        actionClientHandler.AddSubject();
                        break;
                        /**Case 24 odpowiada za dodawanie wiersza Pytan Otwartych do bazy*/
                    case 24:
                        actionClientHandler.AddQuestionOpened();
                        break;
                    /**Case 25 odpowiada za aktualizowanie wiersza Pytan Otwartych do bazy*/
                    case 25:
                        actionClientHandler.UpdateQuestionOpened();
                        break;
                        /**Case 26 odpowiada za zaladowanie danych do zakladki Edytuj Material*/
                    case 26:
                        actionClientHandler.LoadEditMat();
                        break;
                        /**Case 27 odpowiada za pobranie danych do testu Pytan Otwartych*/
                    case 27:
                        actionClientHandler.CountQuestionsOpened();
                        break;
                        /**Case 28 odpowiada za dodanie materialow w zakladce Edytuj Material*/
                    case 28:
                       actionClientHandler.AddMaterial();
                        break;
                    /**Case 29 odpowiada za aktualizacje materialow w zakladce Edytuj Material*/
                    case 29:
                        actionClientHandler.UpdateMaterial();
                        break;
                    /**Case 30 odpowiada za usuwanie materialow w zakladce Edytuj Material*/
                    case 30:
                        actionClientHandler.DeleteMaterial();
                        break;
                    /**Case 31 odpowiada za wypelnienie ComboBox pozwalajacego na wybranie dostepnych zasobow(materialy/pytania) w zakladce Edytuj*/
                    case 31:
                        actionClientHandler.LoadMaterialAndQuestionCB();
                        break;
                        /**Case 32 odpowiada za usuniecie materialow lub Pytan dla wybranego przedmiotu oraz jego typu dostepne w zakladce Edytuj*/
                    case 32:
                        actionClientHandler.DeleteMaterialsQuestions();
                        break;
                    /**Case 19 pobiera dane dla testu Pytan Otwartych*/
                    case 33:
                        actionClientHandler.LoadTestOpened();
                    default:
                        break;
                }

                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
