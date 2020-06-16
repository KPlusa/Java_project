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
    private static String db_user = "javapro";
    private static String db_pass = "haslo";
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
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    url, db_user, db_pass);
        } catch (Exception e) {
            System.out.println(e);
        }

        while (true) {
            try {
                int choice = dis.readInt();
                counter=0;
                switch (choice) {
                    /**Case 1 odpowiada za sprawdzenie czy podane dane uzytkownika odpowiadaja istniejacemu uzytkownikowi w bazie*/
                    case 1:
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select NAZWA,HASLO from Uzytkownik");
                        System.out.println("Logowanie");
                        String to_return = "Niepoprawna nazwa uzytkownika lub haslo";
                        String login = dis.readUTF();
                        String pass = dis.readUTF();
                        System.out.println("Wprowadzany login: " + login + "\n" + "Wprowadzane haslo: " + pass);
                        while (rs.next()) {
                            username =rs.getString(1);
                            password =rs.getString(2);
                            if (login.equals(username) && pass.equals(password)) {
                                to_return = "Poprawne dane";
                                break;
                            }
                        }
                        con.close();
                        dos.writeUTF(to_return);
                        break;
                    /**Case 2 odpowiada za dodanie nowego uzytkownika do bazy*/
                    case 2:
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select * from Uzytkownik");
                        System.out.println("Rejestracja");
                        to_return = "Zarejestrowano";
                        login = dis.readUTF();
                        pass = dis.readUTF();
                        pass2= dis.readUTF();
                        mail= dis.readUTF();
                        System.out.println("Wprowadzana nazwa: " + login + "\n" + "Wprowadzane haslo: " + pass + "\n" + "Wprowadzane haslo2: " + pass2 + "\n" + "Wprowadzany mail: " + mail);
                        if(!pass.equals(pass2)) {
                            to_return = "Hasla sie roznia";
                            con.close();
                            dos.writeUTF(to_return);
                            break;
                        }
                        while (rs.next()) {
                            id++;
                            username =rs.getString(2);
                            password =rs.getString(3);
                            ma= rs.getString(4);
                            if (login.equals(username)) {
                                to_return = "Taka nazwa uzytkownika juz istnieje";
                                con.close();
                                break;
                            }
                            else if (mail.equals(ma)) {
                                to_return = "Taki adres e-mail juz istnieje";
                                con.close();
                                break;
                            }
                        }
                        if(to_return=="Zarejestrowano"){
                            System.out.println("Po wejsciu: " +id);
                            rs = stmt.executeQuery("insert into Uzytkownik values ("+id+",'"+login+"','"+pass+"','"+mail+"')");
                        }
                        con.close();
                        dos.writeUTF(to_return);
                        break;
                    /**Case 3 odpowiada za wypelnienie ComboBox w zakladce Materialy oraz w zakladce Ranking*/
                    case 3:
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("select NAZWA from Przedmiot");
                        while(rs.next()){
                            help=rs.getString(1);
                            list.add(help);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(String help:list){
                            dos.writeUTF(help);
                        }
                        break;
                    /**Case 4 odpowiada za wypelnienie TableView w zakladce Przedmiot*/
                    case 4:
                        stmt=con.createStatement();
                        rs = stmt.executeQuery("select NAZWA,RODZAJ from PRZEDMIOT");
                        List<String> nazwa_przedmiotu = new ArrayList<String>();
                        while(rs.next()){
                            String nazwaPrzedmiotu = rs.getString(1);
                            String rodzajPrzedmiotu = rs.getString(2);
                            nazwa_przedmiotu.add(nazwaPrzedmiotu);
                            nazwa_przedmiotu.add(rodzajPrzedmiotu);
                            System.out.println("Dodano: "+nazwaPrzedmiotu+"\t"+ rodzajPrzedmiotu);
                            counter++;
                        }
                        for(int i=0; i<counter;i++)
                        {
                            System.out.println("W liscie jest: "+nazwa_przedmiotu.get(i));
                        }
                        System.out.println(counter);
                        dos.writeInt(counter);

                        for(String nazwaPrzedmiotu:nazwa_przedmiotu){
                            dos.writeUTF(nazwaPrzedmiotu);
                            System.out.println("Do wysylki:"+nazwaPrzedmiotu);
                        }
                        break;
                    /**Case 5 odpowiada za wypelnienie TableView w zakladce Materialy dla wartosci wybranej w ComboBox*/
                    case 5:
                        cs5=dis.readUTF();
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("select Material from Material where przedmiot_id=(select id from przedmiot where nazwa='"+cs5+"')");
                        while(rs.next()){
                            help=rs.getString(1);
                            list.add(help);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(String help:list){
                            dos.writeUTF(help);
                        }
                        dos.writeUTF(cs5);
                        break;
                        /**Case 6 pobiera dane do zakladki Edycja*/
                    case 6:
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("select NAZWA from Przedmiot");
                        System.out.println("Edycja");
                        while(rs.next()){
                            name_of_subject=rs.getString(1);
                            list.add(name_of_subject);
                            System.out.println("Dodano: "+name_of_subject);
                            counter++;
                        }
                        for(int i=0; i<counter;i++)
                        {
                            System.out.println("W liscie jest: "+list.get(i));
                        }
                        System.out.println(counter);
                        dos.writeInt(counter);

                        System.out.println("Licznik: "+counter);
                        for(String name_of_subject:list){
                            dos.writeUTF(name_of_subject);
                            System.out.println("Do wysylki: "+name_of_subject);
                        }
                        break;
                    /**Case 7 pobiera dane do zakladki Pytania Zamkniete w zakladce Edycja*/
                    case 7:
                        System.out.println("Edycja pytan zamknietych");
                        receiver=dis.readUTF();
                        tmp=dis.readUTF();
                        System.out.println("Otrzymano: "+receiver+"\t"+tmp);
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("Select * from pytania where PRZEDMIOT_ID in (select id from PRZEDMIOT where nazwa='"+receiver+"' and rodzaj='"+tmp+"')  ORDER BY id asc ");
                        List<String> question_list=new ArrayList<String>();
                        while(rs.next()){
                            String id=rs.getString(1);
                            System.out.println(id);
                            question_list.add(id);
                            String tresc=rs.getString(2);
                            System.out.println(tresc);
                            question_list.add(tresc);
                            String odp_a=rs.getString(4);
                            System.out.println(odp_a);
                            if(odp_a==null)
                                odp_a="";
                            question_list.add(odp_a);
                            String odp_b=rs.getString(5);
                            System.out.println(odp_b);
                            if(odp_b==null)
                                odp_b="";
                            question_list.add(odp_b);
                            String odp_c=rs.getString(6);
                            System.out.println(odp_c);
                            if(odp_c==null)
                                odp_c="";
                            question_list.add(odp_c);
                            String odp_d=rs.getString(7);
                            System.out.println(odp_d);
                            if(odp_d==null)
                                odp_d="";
                            question_list.add(odp_d);
                            String correct=rs.getString(10);
                            System.out.println(correct);
                            if(correct==null)
                                correct="0";
                            question_list.add(correct);
                            System.out.println("Dodano: "+id+"\t"+tresc+"\t"+odp_a+"\t"+odp_b+"\t"+odp_c+"\t"+odp_d+"\t"+correct);
                            counter++;
                        }
                        System.out.println("Licznik: "+counter);
                        dos.writeInt(counter);
                        for(String Q:question_list){
                            dos.writeUTF(Q);
                        }
                        System.out.println(question_list);
                        break;
                    /**Case 8 pobiera dane do zakladki Pytania*/
                    case 8:
                        stmt=con.createStatement();
                        rs = stmt.executeQuery("select przedmiot.ID,PRZEDMIOT.NAZWA,PRZEDMIOT.RODZAJ,TRESC from PYTANIA JOIN PRZEDMIOT ON PRZEDMIOT.ID=PYTANIA.PRZEDMIOT_ID");
                        List<Integer> id = new ArrayList<Integer>();
                        List<String> Przedmiot = new ArrayList<String>();
                        while(rs.next()){
                            String support = rs.getString(2);
                            System.out.println(support);
                            Przedmiot.add(support);
                            support = rs.getString(3);
                            System.out.println(support);
                            Przedmiot.add(support);
                            support = rs.getString(4);
                            System.out.println(support);
                            Przedmiot.add(support);
                            counter++;
                        }
                        System.out.println(counter);
                        dos.writeInt(counter);
                        for(String P:Przedmiot){
                            dos.writeUTF(P);
                        }
                        break;
                    /**Case 9 pobiera dane do zakladki Historia dla wybranej daty*/
                    case 9:
                        nick=dis.readUTF();
                        date=dis.readUTF();
                        System.out.println("Otrzymano: "+nick+"\t"+date);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("Select count(PRZEDMIOT_ID), (sum(PUNKTY)/(count(PRZEDMIOT_ID)*20))*100 from RANKING" +
                                "                         where UZYTKOWNIK_ID in (Select UZYTKOWNIK.ID from UZYTKOWNIK" +
                                "                                                 where NAZWA= '"+nick+"')" +
                                "                           and DATA <=to_date('"+date+"','YYYY-MM-DD')" +
                                "                         group by PRZEDMIOT_ID");
                        while(rs.next()){
                            test=rs.getString(1);
                            percent=rs.getString(2);
                            System.out.println("Test: "+test+"\n"+"Procent: "+percent);
                        }
                        if(test == NullPointerException || percent == NullPointerException ) {
                        test="0";
                        percent="0";
                        }
                        dos.writeUTF(test);
                        dos.writeUTF(percent);
                        break;
                    /**Case 10 pobiera dane do zakladki Ranking*/
                    case 10:
                        stmt=con.createStatement();
                        sub = dis.readUTF();
                            rs = stmt.executeQuery("select Ranking.ID,PRZEDMIOT.NAZWA,UZYTKOWNIK.NAZWA,RANKING.PUNKTY from RANKING JOIN PRZEDMIOT ON RANKING.PRZEDMIOT_ID=PRZEDMIOT.ID JOIN UZYTKOWNIK ON RANKING.UZYTKOWNIK_ID=UZYTKOWNIK.ID ORDER BY RANKING.PUNKTY DESC");
                        id = new ArrayList<Integer>();
                        Przedmiot = new ArrayList<String>();
                        List<String> Uzytkownik = new ArrayList<String>();
                        List<Integer> Punkty = new ArrayList<Integer>();
                        while(rs.next()){
                            int support = rs.getInt(1);
                            id.add(support);
                            String przedmiot = rs.getString(2);
                            Przedmiot.add(przedmiot);
                            String uzytkownik = rs.getString(3);
                            Uzytkownik.add(uzytkownik);
                            support = rs.getInt(4);
                            Punkty.add(support);
                            counter++;
                        }
                        System.out.println(counter);
                        dos.writeInt(counter);
                        for(int i=0;i<counter;i++){
                            dos.writeInt(id.get(i));
                            dos.writeUTF(Przedmiot.get(i));
                            dos.writeUTF(Uzytkownik.get(i));
                            dos.writeInt(Punkty.get(i));
                        }
                        break;
                    /**Case 11 pobiera dane do zakladki Ranking dla podanego przedmiotu*/
                    case 11:
                        stmt=con.createStatement();
                        sub = dis.readUTF();
                        rs = stmt.executeQuery("select Ranking.ID,PRZEDMIOT.NAZWA,UZYTKOWNIK.NAZWA,RANKING.PUNKTY from RANKING JOIN PRZEDMIOT ON RANKING.PRZEDMIOT_ID=PRZEDMIOT.ID JOIN UZYTKOWNIK ON RANKING.UZYTKOWNIK_ID=UZYTKOWNIK.ID WHERE PRZEDMIOT.NAZWA='"+sub+"'ORDER BY RANKING.PUNKTY DESC");
                        id = new ArrayList<Integer>();
                        Przedmiot = new ArrayList<String>();
                        Uzytkownik = new ArrayList<String>();
                        Punkty = new ArrayList<Integer>();
                        while(rs.next()){
                            int support = rs.getInt(1);
                            id.add(support);
                            String przedmiot = rs.getString(2);
                            Przedmiot.add(przedmiot);
                            String uzytkownik = rs.getString(3);
                            Uzytkownik.add(uzytkownik);
                            support = rs.getInt(4);
                            Punkty.add(support);
                            counter++;
                        }
                        System.out.println(counter);
                        dos.writeInt(counter);
                        for(int i=0;i<counter;i++){
                            dos.writeInt(id.get(i));
                            dos.writeUTF(Przedmiot.get(i));
                            dos.writeUTF(Uzytkownik.get(i));
                            dos.writeInt(Punkty.get(i));
                        }
                        break;
                    /**Case 12 pobiera dane do zakladki Ranking dla podanego loginu*/
                    case 12:
                        stmt=con.createStatement();
                        sub = dis.readUTF();
                        rs = stmt.executeQuery("select Ranking.ID,PRZEDMIOT.NAZWA,UZYTKOWNIK.NAZWA,RANKING.PUNKTY from RANKING JOIN PRZEDMIOT ON RANKING.PRZEDMIOT_ID=PRZEDMIOT.ID JOIN UZYTKOWNIK ON RANKING.UZYTKOWNIK_ID=UZYTKOWNIK.ID WHERE UZYTKOWNIK.NAZWA='"+sub+"'ORDER BY RANKING.PUNKTY DESC");
                        id = new ArrayList<Integer>();
                        Przedmiot = new ArrayList<String>();
                        Uzytkownik = new ArrayList<String>();
                        Punkty = new ArrayList<Integer>();
                        while(rs.next()){
                            int support = rs.getInt(1);
                            id.add(support);
                            String przedmiot = rs.getString(2);
                            Przedmiot.add(przedmiot);
                            String uzytkownik = rs.getString(3);
                            Uzytkownik.add(uzytkownik);
                            support = rs.getInt(4);
                            Punkty.add(support);
                            counter++;
                        }
                        System.out.println(counter);
                        dos.writeInt(counter);

                        for(int i=0;i<counter;i++){
                            dos.writeInt(id.get(i));
                            dos.writeUTF(Przedmiot.get(i));
                            dos.writeUTF(Uzytkownik.get(i));
                            dos.writeInt(Punkty.get(i));
                        }
                        break;
                    /**Case 13 pobiera dane do zakladki Generuj Test dla podanego przedmiotu*/
                    case 13:
                        stmt=con.createStatement();
                        sub=dis.readUTF();
                        rs=stmt.executeQuery("select RODZAJ from Przedmiot WHERE NAZWA='"+sub+"'");
                        while(rs.next()){
                            help=rs.getString(1);
                            list.add(help);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(String help:list){
                            dos.writeUTF(help);
                        }
                        break;
                    /**Case 14 dodaje wiersz do bazy podany w zakladce Pytania Zamkniete dostepnej w zakladce Edytuj*/
                    case 14:
                        int idd=dis.readInt();
                        String text= dis.readUTF();
                        String aa= dis.readUTF();
                        String ab= dis.readUTF();
                        String ac= dis.readUTF();
                        String ad= dis.readUTF();
                        String subject=dis.readUTF();
                        String type=dis.readUTF();
                        int ra=dis.readInt();
                        System.out.println("Otrzymano: "+idd+"\t"+text+"\t"+aa+"\t"+ab+"\t"+ac+"\t"+ad+"\t"+ra+"\t"+subject+"\t"+type);
                        stmt=con.createStatement();
                        if(!aa.equals("") && (ra>0 && ra<5)) {
                            rs = stmt.executeQuery("Select id from pytania where przedmiot_id in (select id from przedmiot where id =" + idd + " or tresc='" + text + "')");
                            while (rs.next())
                                counter++;
                            if (counter == 0) {
                                rs = stmt.executeQuery("insert into PYTANIA (ID, TRESC, PRZEDMIOT_ID, ODP_A, ODP_B, ODP_C, ODP_D, POPR_Z) " +
                                        "values (" + idd + ",'" + text + "',(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and przedmiot.rodzaj='" + type + "'),'" + aa + "','" + ab + "','" + ac + "','" + ad + "'," + ra + " )");

                                dos.writeUTF("Pomyslnie dodano");
                                o_z=0;
                                rs = stmt.executeQuery("update pytania set o_z="+o_z+" where tresc='"+text+"' and odp_a = '"+aa+"' ");
                                System.out.println("O_Z: "+o_z);

                            }
                            dos.writeUTF("Takie dane juz istnieja w tym przedmiocie");
                        }
                        else {
                            st="Dodaj przynajmniej jedna odpowiedz";
                            if ((ra < 1 || ra > 4)) {
                                st = "Zaznacz poprawna odpowiedz";
                                if (aa.equals(""))
                                    st="Dodaj przynajmniej jedna odpowiedz";
                            }
                            dos.writeUTF(st);
                        }
                        break;
                    /**Case 15 aktualizuje wiersz w bazie podany w zakladce Pytania Zamkniete dostepnej w zakladce Edytuj*/
                    case 15:
                        idd=dis.readInt();
                        text= dis.readUTF();
                        aa= dis.readUTF();
                        ab= dis.readUTF();
                        ac= dis.readUTF();
                        ad= dis.readUTF();
                        subject=dis.readUTF();
                        ra=dis.readInt();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+idd+"\t"+text+"\t"+aa+"\t"+ab+"\t"+ac+"\t"+ad+"\t"+ra+"\t"+subject+"\t"+type);
                        stmt=con.createStatement();
                        if (!aa.equals("")) {
                            rs = stmt.executeQuery("update PYTANIA set  TRESC='" + text + "', PRZEDMIOT_ID=(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and Przedmiot.rodzaj ='" + type + "')" +
                                    ", ODP_A='" + aa + "', ODP_B='" + ab + "', ODP_C='" + ac + "', ODP_D='" + ad + "', POPR_Z=" + ra + "where ID = " + idd + "");
                            st = "Pomyslnie zaktualizowano";
                            rs = stmt.executeQuery("Select o_z from pytania where tresc='"+text+"' and odp_a = '"+aa+"' and odp_o is not null");
                            while (rs.next()) {
                                tmpp=1;
                            }
                            if(tmpp==1) {
                                o_z = 2;
                                rs = stmt.executeQuery("update pytania set o_z=" + o_z + "  where tresc='" + text + "' and odp_a = '" + aa + "' ");
                            }
                        }
                        else st = "odpowiedz  a nie  moze byc pusta";
                        dos.writeUTF(st);
                        break;
                    /**Case 16 usuwa wiersz w bazie podany w zakladce Pytania Zamkniete lub w zakladce Pytania Otwarte dostepnej w zakladce Edytuj*/
                    case 16://usuwanie pytan zamknietych,otwartych
                        idd=dis.readInt();
                        System.out.println("Otrzymano: "+idd+"\t");
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("delete from PYTANIA where ID = "+idd+"");
                        st="Pomyslnie usunieto";
                        dos.writeUTF(st);
                        break;
                    /**Case 17 wypelnia Tableview w zakladce Pytania Otwarte dostepnej w zakladce Edytuj danymi*/
                    case 17:
                        System.out.println("Edycja pytan otwartych");
                        receiver=dis.readUTF();
                        tmp=dis.readUTF();
                        System.out.println("Otrzymano: "+receiver);
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("Select * from pytania where PRZEDMIOT_ID in (select id from PRZEDMIOT where nazwa='"+receiver+"' and rodzaj = '"+tmp+"') ORDER BY id asc ");
                        List<String> question_opened_list=new ArrayList<String>();
                        while(rs.next()){
                            String ID=rs.getString(1);
                            System.out.println(ID);
                            question_opened_list.add(ID);
                            String tresc=rs.getString(2);
                            System.out.println(tresc);
                            question_opened_list.add(tresc);
                            String odp=rs.getString(8);
                            System.out.println(odp);
                            if(odp==null)
                                odp="";
                            question_opened_list.add(odp);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(String Q:question_opened_list){
                            dos.writeUTF(Q);
                        }
                        break;
                        /**Case 18 dodaje wiersz do bazy dla odpowiedniego uzytkownika i testu(zakladka Wynik)*/
                    case 18:
                        stmt=con.createStatement();
                        login=dis.readUTF();
                        sub=dis.readUTF();
                        String typ=dis.readUTF();
                        int imp=dis.readInt();
                        String data=dis.readUTF();
                        rs=stmt.executeQuery("(Select max(id) from ranking)");
                        rs.next();
                        int ranking_id_i=rs.getInt(1);
                        ranking_id_i++;
                        System.out.println(ranking_id_i);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("(select id from PRZEDMIOT WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+typ+"')");
                        rs.next();
                        int przedmiot_id_i=rs.getInt(1);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("Select ID FROM UZYTKOWNIK WHERE UZYTKOWNIK.NAZWA='"+login+"'");
                        rs.next();
                        int user_id_i=rs.getInt(1);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("INSERT INTO RANKING(id,przedmiot_id,uzytkownik_id,punkty,data) VALUES ("+ranking_id_i+","+przedmiot_id_i+","+user_id_i+","+imp+",TO_DATE('"+data+"', 'YYYY-MM-DD'))");
                        System.out.println(login);
                        System.out.println(sub);
                        System.out.println(typ);
                        System.out.println(imp);
                        System.out.println(data);
                        break;
                        /**Case 19 pobiera dane dla testu Pytan Zamknietych*/
                    case 19:
                        stmt=con.createStatement();
                        sub=dis.readUTF();
                        String type_of_subject = dis.readUTF();
                        List<String> list_tresc = new ArrayList<String>();
                        List<String> list_odp_a = new ArrayList<String>();
                        List<String> list_odp_b = new ArrayList<String>();
                        List<String> list_odp_c = new ArrayList<String>();
                        List<String> list_odp_d = new ArrayList<String>();
                        List<Integer> list_popr_o = new ArrayList<Integer>();
                        rs=stmt.executeQuery("select TRESC,ODP_A,ODP_B,ODP_C,ODP_D,POPR_Z from PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type_of_subject+"'AND PYTANIA.O_Z!=1");
                        while(rs.next()){
                            String Tresc =rs.getString(1);
                            list_tresc.add(Tresc);
                            String Odp_a =rs.getString(2);
                            list_odp_a.add(Odp_a);
                            String Odp_b =rs.getString(3);
                            list_odp_b.add(Odp_b);
                            String Odp_c =rs.getString(4);
                            list_odp_c.add(Odp_c);
                            String Odp_d =rs.getString(5);
                            list_odp_d.add(Odp_d);
                            int Popr_o =rs.getInt(6);
                            list_popr_o.add(Popr_o);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(int i=0;i<counter;i++){
                            dos.writeInt(i);
                            dos.writeUTF(list_tresc.get(i));
                            dos.writeUTF(list_odp_a.get(i));
                            dos.writeUTF(list_odp_b.get(i));
                            dos.writeUTF(list_odp_c.get(i));
                            dos.writeUTF(list_odp_d.get(i));
                            dos.writeInt(list_popr_o.get(i));
                        }
                        break;
                        /**Case 20 wypelnia ComboBox w zakladce Edytuj oraz Dodaj Usun Przedmiot dostepnej z jej poziomu typami przedmiotow dla odpowiedniej nazwy przedmiotu*/
                    case 20:
                        rcv=dis.readUTF();
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("select Rodzaj from Przedmiot  WHERE NAZWA='"+rcv+"'");
                        while(rs.next()){
                            tmp=rs.getString(1);
                            l.add(tmp);
                            System.out.println("Dodano: "+tmp);
                            counter++;
                        }
                        dos.writeInt(counter);
                        System.out.println("Licznik: "+counter);
                        for(String t:l){
                            dos.writeUTF(t);
                            System.out.println("Do wysylki: "+t);
                        }
                        break;
                        /**Case 21 odpowiada za odczytanie wartosci ilosci pytan dla testu Pytan Zamknietych*/
                    case 21:
                        stmt=con.createStatement();
                        sub=dis.readUTF();
                        type_of_subject=dis.readUTF();
                        System.out.println(sub);
                        System.out.println(type_of_subject);
                        rs=stmt.executeQuery("SELECT TRESC,ODP_A,ODP_B,ODP_C,ODP_D,POPR_Z FROM PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type_of_subject+"' AND PYTANIA.O_Z!=1");
                        while(rs.next()){
                            String Tresc =rs.getString(1);
                            String Odp_a =rs.getString(2);
                            String Odp_b =rs.getString(3);
                            String Odp_c =rs.getString(4);
                            String Odp_d =rs.getString(5);
                            int Popr_o =rs.getInt(6);
                            counter++;
                        }
                        dos.writeInt(counter);
                        break;
                        /**Case 22 odpowiada za usuniecie przedmiotu w zakladce Dodaj Usun Przedmiot dostepnej z zakladki Edycja*/
                    case 22:
                        subject=dis.readUTF();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+subject+"\n"+type);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("delete from ranking WHERE przedmiot_id in (select id from przedmiot where NAZWA='"+subject+"' and rodzaj='"+type+"')");
                        rs=stmt.executeQuery("delete from pytania WHERE przedmiot_id in (select id from przedmiot where NAZWA='"+subject+"' and rodzaj='"+type+"')");
                        rs=stmt.executeQuery("delete from material WHERE przedmiot_id in (select id from przedmiot where NAZWA='"+subject+"' and rodzaj='"+type+"')");
                        rs=stmt.executeQuery("delete from przedmiot WHERE NAZWA='"+subject+"' and rodzaj='"+type+"'");
                        dos.writeUTF("Pomyslnie usunieto");
                        break;
                    /**Case 23 odpowiada za dodanie przedmiotu w zakladce Dodaj Usun Przedmiot dostepnej z zakladki Edycja*/
                    case 23:
                        subject=dis.readUTF();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+subject+"\n"+type);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("Select id from przedmiot where nazwa='"+subject+"' and rodzaj='"+type+"' ");
                        while (rs.next())
                            counter++;

                        if(counter==0){
                            rs=stmt.executeQuery("insert into przedmiot (id, nazwa, rodzaj) values ((select max(id)+1 from przedmiot),'"+subject+"', '"+type+"')");
                            dos.writeUTF("Pomyslnie dodano");}
                        else
                            dos.writeUTF("Takie dane juz istnieja w tym przedmiocie");
                        break;
                        /**Case 24 odpowiada za dodawanie wiersza Pytan Otwartych do bazy*/
                    case 24:
                        idd=dis.readInt();
                        text= dis.readUTF();
                        aa= dis.readUTF();
                        subject=dis.readUTF();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+idd+"\t"+text+"\t"+aa+"\t"+subject+"\t"+type);
                        stmt=con.createStatement();
                        if(!aa.equals("")) {
                            rs = stmt.executeQuery("Select id from pytania where przedmiot_id in (select id from przedmiot where id =" + idd + " or tresc='" + text + "')");
                            while (rs.next())
                                counter++;
                            if (counter == 0) {
                                rs = stmt.executeQuery("insert into PYTANIA (ID, TRESC, PRZEDMIOT_ID, ODP_O) " +
                                        "values (" + idd + ",'" + text + "',(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and przedmiot.rodzaj='" + type + "'),'" + aa + "')");
                                dos.writeUTF("Pomyslnie dodano");
                                o_z=1;
                                rs = stmt.executeQuery("update pytania set o_z=" + o_z + "  where tresc='"+text+"' and odp_o = '"+aa+"' ");
                            }
                            dos.writeUTF("Takie dane juz istnieja w tym przedmiocie");
                        }
                        else {
                            st="Dodaj odpowiedz";
                            dos.writeUTF(st);
                        }
                        break;
                    /**Case 25 odpowiada za aktualizowanie wiersza Pytan Otwartych do bazy*/
                    case 25:
                        idd=dis.readInt();
                        text= dis.readUTF();
                        aa= dis.readUTF();
                        subject=dis.readUTF();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+idd+"\t"+text+"\t"+aa+"\t"+subject+"\t"+type);
                        stmt=con.createStatement();
                        if(!aa.equals("")) {
                            rs = stmt.executeQuery("update PYTANIA set  TRESC='" + text + "', PRZEDMIOT_ID=(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and Przedmiot.rodzaj ='" + type + "' )" +
                                    ", ODP_O='" + aa + "' where ID = " + idd + "");
                            st = "Pomyslnie zaktualizowano";
                            rs = stmt.executeQuery("Select o_z from pytania where tresc='"+text+"' and odp_o = '"+aa+"' and odp_a is not null");
                            while (rs.next()) {
                                tmpp=1;
                            }
                            if(tmpp==1) {
                                o_z = 2;
                                rs = stmt.executeQuery("update pytania set o_z=" + o_z + "  where tresc='" + text + "' and odp_o = '" + aa + "' ");
                            }
                        }
                        else st = "Odpowiedz nie moze byc pusta";
                        dos.writeUTF(st);
                        break;
                        /**Case 26 odpowiada za zaladowanie danych do zakladki Edytuj Material*/
                    case 26:
                        System.out.println("Edycja materialow");
                        receiver=dis.readUTF();
                        tmp=dis.readUTF();
                        System.out.println("Otrzymano: "+receiver);
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("Select id, (select NAZWA from PRZEDMIOT where nazwa='"+receiver+"')as nazwa,(select RODZAJ from PRZEDMIOT where rodzaj='"+tmp+"') as typ,MATERIAL " +
                                "from material where PRZEDMIOT_ID in (select id from PRZEDMIOT where nazwa='"+receiver+"'" +
                                "and rodzaj = '"+tmp+"') ORDER BY id asc");
                        List<String> edit_mat_list=new ArrayList<String>();
                        while(rs.next()){
                            String ID=rs.getString(1);
                            System.out.println(ID);
                            edit_mat_list.add(ID);
                            String Subject=rs.getString(2);
                            System.out.println(Subject);
                            edit_mat_list.add(Subject);
                            String Type=rs.getString(3);
                            System.out.println(Type);
                            edit_mat_list.add(Type);
                            String Text=rs.getString(4);
                            edit_mat_list.add(Text);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(String M:edit_mat_list){
                            dos.writeUTF(M);
                        }
                        break;
                        /**Case 27 odpowiada za pobranie danych do testu Pytan Otwartych*/
                    case 27:
                        stmt=con.createStatement();
                        sub=dis.readUTF();
                        type_of_subject = dis.readUTF();
                        list_tresc = new ArrayList<String>();
                        List<String> list_odp_o = new ArrayList<String>();
                        rs=stmt.executeQuery("select TRESC,odp_o from PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type_of_subject+"'AND PYTANIA.O_Z!=0");
                        while(rs.next()){
                            String Tresc =rs.getString(1);
                            list_tresc.add(Tresc);
                            String Popr_o =rs.getString(2);
                            list_odp_o.add(Popr_o);
                            counter++;
                        }
                        dos.writeInt(counter);
                        break;
                        /**Case 28 odpowiada za dodanie materialow w zakladce Edytuj Material*/
                    case 28:

                        text= dis.readUTF();
                        subject=dis.readUTF();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+text+"\t"+subject+"\t"+type);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("Select id from material where przedmiot_id in (select id from przedmiot where material.material='"+text+"')");
                        while (rs.next())
                            counter++;
                        if(counter==0) {
                            rs=stmt.executeQuery("insert into Material (ID, material, PRZEDMIOT_ID) " +
                                    "values ((select max(id)+1 from material),'"+text+"',(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '"+subject+"' and przedmiot.rodzaj='"+type+"'))");
                            dos.writeUTF("Pomyslnie dodano");
                        }
                        dos.writeUTF("Takie dane juz istnieja w tym materiale");
                        break;
                    /**Case 29 odpowiada za aktualizacje materialow w zakladce Edytuj Material*/
                    case 29:
                        tmp=dis.readUTF();
                        text= dis.readUTF();
                        subject=dis.readUTF();
                        type=dis.readUTF();
                        System.out.println("Otrzymano: "+text+"\t"+subject+"\t"+type);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("update Material set material='"+text+"' " +
                                "where material.material = '"+tmp+"'");
                        st="Pomyslnie zaktualizowano";
                        dos.writeUTF(st);
                        break;
                    /**Case 30 odpowiada za usuwanie materialow w zakladce Edytuj Material*/
                    case 30:
                        text=dis.readUTF();
                        System.out.println("Otrzymano: "+text+"\t");
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("delete from material where material.material = '"+text+"'");
                        st="Pomyslnie usunieto";
                        dos.writeUTF(st);
                        break;
                    /**Case 31 odpowiada za wypelnienie ComboBox pozwalajacego na wybranie dostepnych zasobow(materialy/pytania) w zakladce Edytuj*/
                    case 31:
                        int mat=0;
                        int pyt=0;
                        rcv=dis.readUTF();
                        receiver=dis.readUTF();
                        System.out.println("Otrzymano: "+rcv+"\t"+receiver);
                        stmt=con.createStatement();
                        rs=stmt.executeQuery("select id from material  WHERE przedmiot_id in (select id from przedmiot where nazwa='"+rcv+"' and rodzaj ='"+receiver+"')");
                        while(rs.next()){
                            mat=1;
                        }
                        System.out.println("Mam mat =: "+mat);
                        rs=stmt.executeQuery("select id from pytania  WHERE przedmiot_id in (select id from przedmiot where nazwa='"+rcv+"' and rodzaj ='"+receiver+"')");
                        while(rs.next()){
                            pyt=1;
                        }
                        System.out.println("Mam pyt =: "+pyt);
                        if(mat==1 && pyt==1)
                            counter=3;
                        else if(mat==1 && pyt==0)
                            counter=2;
                        else if(mat==0 && pyt==1)
                            counter=1;
                        else
                            counter=0;
                        dos.writeInt(counter);
                        System.out.println("Licznik: "+counter);
                        for(String t:l){
                            dos.writeUTF(t);
                            System.out.println("Do wysylki: "+t);
                        }
                        break;
                        /**Case 32 odpowiada za usuniecie materialow lub Pytan dla wybranego przedmiotu oraz jego typu dostepne w zakladce Edytuj*/
                    case 32:
                        rcv=dis.readUTF();
                        receiver=dis.readUTF();
                        tmp=dis.readUTF();
                        System.out.println("Otrzymano: "+rcv+"\t"+receiver+"\t"+tmp);
                        stmt=con.createStatement();
                        if(tmp.equals("PYTANIA")) {
                            rs = stmt.executeQuery("delete from PYTANIA where przedmiot_id in (select id from przedmiot where nazwa='" + rcv + "' and rodzaj ='" + receiver + "')");
                            st = "Pomyslnie usunieto pytania";
                        }
                        else if(tmp.equals("MATERIALY")) {
                            rs = stmt.executeQuery("delete from MATERIAL where przedmiot_id in (select id from przedmiot where nazwa='" + rcv + "' and rodzaj ='" + receiver + "')");
                            st = "Pomyslnie usunieto materialy";
                        }
                        else st="error";
                        dos.writeUTF(st);
                        break;
                    /**Case 19 pobiera dane dla testu Pytan Otwartych*/
                    case 33:
                        stmt=con.createStatement();
                        sub=dis.readUTF();
                        type_of_subject = dis.readUTF();
                        list_tresc = new ArrayList<String>();
                        list_odp_o = new ArrayList<String>();
                        rs=stmt.executeQuery("select TRESC,odp_o from PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type_of_subject+"'AND PYTANIA.O_Z!=0");
                        while(rs.next()){
                            String Tresc =rs.getString(1);
                            list_tresc.add(Tresc);
                            String Popr_o =rs.getString(2);
                            list_odp_o.add(Popr_o);
                            counter++;
                        }
                        dos.writeInt(counter);
                        for(int i=0;i<counter;i++){
                            dos.writeInt(i);
                            dos.writeUTF(list_tresc.get(i));
                            dos.writeUTF(list_odp_o.get(i));
                        }
                    default:
                        break;
                }

                break;
            } catch (IOException | SQLException e) {
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
