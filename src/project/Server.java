package project;


import java.io.*;
import java.nio.file.FileSystemAlreadyExistsException;
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

