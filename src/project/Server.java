package project;


import java.io.*;
import java.nio.file.FileSystemAlreadyExistsException;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6485);
        System.out.println("Server dziala");
        while (true) {
            Socket s = null;
            try {

                s = ss.accept();
                System.out.println("\nPolaczono z socketem: " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());


                Thread t = new ClientHandler(s, dis, dos);
                t.start();

            } catch (Exception e) {
                System.out.println("\n zamykanie polaczenia");
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private int id=1;
    private String cs5;
    private String name_of_subject;
    private int counter=0;
    private int i=0;
    List<String> list=new ArrayList<String>();
    private String username;
    private String password;
    private String pass;
    private String pass2;
    private String help;
    private String mail;
    private String ma;
    private String receiver;
    private String to_return;
    private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String db_user = "javapro";
    private static String db_pass = "haslo";
    private static Connection con;
    private Statement stmt;
    private ResultSet rs;

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
                        counter=0;
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
                            rodzaj_przedmiotu.add(rodzajPrzedmiotu);
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

                        for(String rodzajPrzedmiotu:rodzaj_przedmiotu){
                            dos.writeUTF(rodzajPrzedmiotu);
                            System.out.println("Do wysylki:"+rodzajPrzedmiotu);
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

