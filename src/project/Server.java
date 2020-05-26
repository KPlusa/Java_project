package project;


import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Server {
    private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String user = "DB_ADMIN";
    private static String password = "qazwsx";
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
    public String username = "admin";
    public String password = "admin";

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {

        while (true) {

            try {
                int choice = dis.readInt();
                switch (choice) {
                    case 1:
                        System.out.println("Logowanie");
                        String to_return = "Niepoprawna nazwa uzytkownika lub haslo";
                        String login = dis.readUTF();
                        String pass = dis.readUTF();
                        System.out.println("Wprowadzany login: " + login + "\n" + "Wprowadzane haslo: " + pass);

                        if (login.equals(username) && pass.equals(password))
                            to_return = "Poprawne dane";

                        dos.writeUTF(to_return);
                        //System.out.println("Zamykanie socketa");
                        //s.close();
                        break;
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

