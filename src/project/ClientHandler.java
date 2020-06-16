package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ClientHandler class
class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private int id=1;
    private String cs5;
    private String nick,date,test, percent; //Historia

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

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

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
                    case 1:
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select NAZWA,HASLO from Uzytkownik");
                        System.out.println("Logowanie");
                        String to_return = "Niepoprawna nazwa uzytkownika lub haslo";
                        String login = dis.readUTF();
                        String pass = dis.readUTF();
                        System.out.println("Wprowadzany login: " + login + "\n" + "Wprowadzane haslo: " + pass);
                        while (rs.next()) {
                            //user_id=rs.getInt(1);
                            username =rs.getString(1);
                            password =rs.getString(2);
                            if (login.equals(username) && pass.equals(password)) {
                                to_return = "Poprawne dane";
                                break;
                            }
                        }
                        con.close();
                        //if (login.equals(username) && pass.equals(password))
                        //to_return = "Poprawne dane";

                        dos.writeUTF(to_return);
                        // dos.writeInt(user_id);
                        //System.out.println("Zamykanie socketa");
                        //s.close();
                        break;
                    case 2: //Rejestracja
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
                        //System.out.println("Zamykanie socketa");
                        //s.close();
                        break;

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
                    case 4:

                        stmt=con.createStatement();
                        rs = stmt.executeQuery("select NAZWA,RODZAJ from PRZEDMIOT");
                        List<String> nazwa_przedmiotu = new ArrayList<String>();
                        List<String> rodzaj_przedmiotu = new ArrayList<String>();
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
                    case 6://Edycja
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

                    case 7://Pytania zamkniete
                        System.out.println("Edycja pytan zamknietych");
                        receiver=dis.readUTF();
                        System.out.println("Otrzymano: "+receiver);
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("Select * from pytania where id in (select id from PRZEDMIOT where nazwa='"+receiver+"')");


                        List<String> question_list=new ArrayList<String>();List<Integer> id_list=new ArrayList<Integer>();
                        List<String> answer_A_list=new ArrayList<String>();
                        List<String> answer_B_list=new ArrayList<String>();
                        List<String> answer_C_list=new ArrayList<String>();
                        List<String> answer_D_list=new ArrayList<String>();

                        while(rs.next()){
                            int ID=rs.getInt(1);
                            String question=rs.getString(2);
                            String answer_a=rs.getString(4);
                            String answer_b=rs.getString(5);
                            String answer_c=rs.getString(6);
                            String answer_d=rs.getString(7);
                            id_list.add(ID);
                            question_list.add(question);
                            answer_A_list.add(answer_a);
                            answer_B_list.add(answer_b);
                            answer_C_list.add(answer_c);
                            answer_D_list.add(answer_d);
                            System.out.println("Dodano: "+ID+"\t"+ question +"\t"+answer_a+"\t"+answer_b+"\t"+answer_c+"\t"+answer_d+"\n");
                            counter++;
                        }
                        for(int i=0; i<counter;i++)
                        {
                            System.out.println("W liscie jest: "+id_list.get(i));
                        }
                        System.out.println("Licznik: "+counter);
                        dos.writeInt(counter);


                        for(int ID: id_list){
                            dos.writeInt(ID);
                            System.out.println("Do wysylki: "+ID);
                        }

                        for(String question:question_list){
                            dos.writeUTF(question);
                            System.out.println("Do wysylki: "+question);
                        }

                        for(String answer_a:answer_A_list){
                            dos.writeUTF(answer_a);
                            System.out.println("Do wysylki: "+answer_a);
                        }

                        for(String answer_b:answer_B_list){
                            dos.writeUTF(answer_b);
                            System.out.println("Do wysylki: "+answer_b);
                        }
                        for(String answer_c:answer_C_list){
                            dos.writeUTF(answer_c);
                            System.out.println("Do wysylki: "+answer_c);
                        }
                        for(String answer_d:answer_D_list){
                            dos.writeUTF(answer_d);
                            System.out.println("Do wysylki: "+answer_d);
                        }
                        break;
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
                    case 9://Historia
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
