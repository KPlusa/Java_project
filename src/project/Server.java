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
    private String cs5;
    private int counter;
    private int i=0;
    List<String> list=new ArrayList<String>();
    private String username;
    private String password;
    private String help;
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

