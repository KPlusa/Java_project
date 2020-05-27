package project;


import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5057);
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
    private String name_of_subject;
    private int counter=0;
    private List<String> list=new ArrayList<String>();
    private String username; private String login;
    private String password; private String pass; private String pass2;
    private String mail; private String ma;
    private String receiver;
    private String to_return;
    private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String db_user = "DB_ADMIN";
    private static String db_pass = "qazwsx";
    private static Connection con;
    private Statement stmt;
    private ResultSet rs;
    private List<Integer> id_list=new ArrayList<Integer>();
    private List<String> question_list=new ArrayList<String>();
    private List<String> answer_A_list=new ArrayList<String>();
    private List<String> answer_B_list=new ArrayList<String>();
    private List<String> answer_C_list=new ArrayList<String>();
    private List<String> answer_D_list=new ArrayList<String>();

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {


        while (true) {
            try {

                Class.forName("oracle.jdbc.driver.OracleDriver");
                con = DriverManager.getConnection(
                        url, db_user, db_pass);
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                int choice = dis.readInt();
                //if(choice==0)
                   // break;
                switch (choice) {
                    case 1: //Logowanie
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("select nazwa,haslo from Uzytkownik");
                        System.out.println("Logowanie");
                        to_return = "Niepoprawna nazwa uzytkownika lub haslo";
                        login = dis.readUTF();
                        pass = dis.readUTF();
                        System.out.println("Wprowadzany login: " + login + "\n" + "Wprowadzane haslo: " + pass);
                        while (rs.next()) {
                            username =rs.getString(1);
                            password =rs.getString(2);
                            if (login.equals(username) && pass.equals(password)) {
                                to_return = "Poprawne dane";
                                break;
                            }
                        }
                        //con.close();

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
                        break;
                    case 8:
                        System.out.println(counter);
                        dos.writeInt(counter);
                        for(int i=1; i<3;i++)
                        {
                            System.out.println("W liscie jest: "+id_list.get(i));
                        }

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

