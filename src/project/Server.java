package project;


import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5057);
        while (true) {
            Socket s = null;

            try {
                System.out.println("Server is running");
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                Thread t = new ClientHandler(s, dis, dos);
                t.start();

            } catch (Exception e) {
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
        String to_return = "Niepoprawna nazwa uzytkownika lub haslo";
        while (true) {
            try {

                String login = dis.readUTF().toString();
                String pass = dis.readUTF().toString();
                System.out.println("Entered Login: " + login + "\n" + "Entered password: " + pass);

                if (login.equals(username) && pass.equals(password))
                    to_return = "Poprawne dane";

                dos.writeUTF(to_return);
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

