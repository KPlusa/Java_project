package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActionClientHandler {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private int id,ra;
    private String text,answer,aa,ab,ac,ad,subject,type;
    private String status;
    private String rcv; //Edycja
    private String tmp="";
    //private String id;
    private int support=0;
    private int tmpp=0;
    private String name_of_subject;
    private int counter=0;
    private String przedmiot,uzytkownik;
    private List<String> StringList=new ArrayList<String>();
    private List<Integer> IntegerList = new ArrayList<Integer>();
    private List<String> Uzytkownik = new ArrayList<String>();
    private List<Integer> Punkty = new ArrayList<Integer>();
    private String login,username;
    private String password;
    private String pass;
    private String help;
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
    public ActionClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void connectToDB() {
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    url, db_user, db_pass);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void Login()
    {
        try {
        stmt = con.createStatement();
        rs = stmt.executeQuery("select NAZWA,HASLO from Uzytkownik");
        System.out.println("Logowanie");
        status = "Niepoprawna nazwa uzytkownika lub haslo";
        login = dis.readUTF();
        pass = dis.readUTF();
        System.out.println("Wprowadzany login: " + login + "\n" + "Wprowadzane haslo: " + pass);
        while (rs.next()) {
            username =rs.getString(1);
            password =rs.getString(2);
            if (login.equals(username) && pass.equals(password)) {
                status = "Poprawne dane";
                break;
            }
        }
        con.close();
        dos.writeUTF(status);
    }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        }
    public void Register() {
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from Uzytkownik");
            System.out.println("Rejestracja");
            status = "Zarejestrowano";
            login = dis.readUTF();
            pass = dis.readUTF();
            String pass2 = dis.readUTF();
            String mail = dis.readUTF();
            System.out.println("Wprowadzana nazwa: " + login + "\n" + "Wprowadzane haslo: " + pass + "\n" + "Wprowadzane haslo2: " + pass2 + "\n" + "Wprowadzany mail: " + mail);
            if (!pass.equals(pass2)) {
                status = "Hasla sie roznia";
            } else
                {
                while (rs.next()) {
                    id++;
                    username = rs.getString(2);
                    password = rs.getString(3);
                    ma = rs.getString(4);
                    if (login.equals(username))
                        status = "Taka nazwa uzytkownika juz istnieje";
                    else if (mail.equals(ma))
                        status = "Taki adres e-mail juz istnieje";
                }
            if (status == "Zarejestrowano") {
                System.out.println("Po wejsciu: " + id);
                rs = stmt.executeQuery("insert into Uzytkownik values ((select max(id)+1 from uzytkownik),'" + login + "','" + pass + "','" + mail + "')");
            }
                }
            con.close();
            dos.writeUTF(status);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadMaterialCB()
    {
        StringList.clear();
        try{
            stmt=con.createStatement();
            rs=stmt.executeQuery("select NAZWA from Przedmiot");
            while(rs.next()){
                help=rs.getString(1);
                StringList.add(help);
                counter++;
            }
            dos.writeInt(counter);
            for(String help:StringList){
                dos.writeUTF(help);
            }
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadSubjectTableView()
    {
        StringList.clear();
        try {
        stmt=con.createStatement();
        rs = stmt.executeQuery("select NAZWA,RODZAJ from PRZEDMIOT");
        while(rs.next()){
            String nazwaPrzedmiotu = rs.getString(1);
            String rodzajPrzedmiotu = rs.getString(2);
            StringList.add(nazwaPrzedmiotu);
            StringList.add(rodzajPrzedmiotu);
            System.out.println("Dodano: "+nazwaPrzedmiotu+"\t"+ rodzajPrzedmiotu);
            counter++;
        }
        for(int i=0; i<counter;i++)
        {
            System.out.println("W liscie jest: "+StringList.get(i));
        }
        System.out.println(counter);
        dos.writeInt(counter);

        for(String nazwaPrzedmiotu:StringList){
            dos.writeUTF(nazwaPrzedmiotu);
            System.out.println("Do wysylki:"+nazwaPrzedmiotu);
        }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    public void LoadMaterialTableView ()
    {
        StringList.clear();
        try {
            tmp=dis.readUTF();
            stmt=con.createStatement();
            rs=stmt.executeQuery("select Material from Material where przedmiot_id=(select id from przedmiot where nazwa='"+tmp+"')");
            while(rs.next()){
                help=rs.getString(1);
                StringList.add(help);
                counter++;
            }
            dos.writeInt(counter);
            for(String help:StringList){
                dos.writeUTF(help);
            }
            dos.writeUTF(tmp);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadEdit ()
    {
        StringList.clear();
        try {
            stmt=con.createStatement();
            rs=stmt.executeQuery("select NAZWA from Przedmiot");
            System.out.println("Edycja");
            while(rs.next()){
                tmp=rs.getString(1);
                StringList.add(tmp);
                System.out.println("Dodano: "+tmp);
                counter++;
            }
            for(int i=0; i<counter;i++)
            {
                System.out.println("W liscie jest: "+StringList.get(i));
            }
            System.out.println(counter);
            dos.writeInt(counter);
            System.out.println("Licznik: "+counter);
            for(String nameOfSubject:StringList){
                dos.writeUTF(nameOfSubject);
                System.out.println("Do wysylki: "+nameOfSubject);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        
    }
    public void LoadEditClosed ()
    {
        StringList.clear();
        try {
            System.out.println("Edycja pytan zamknietych");
            receiver=dis.readUTF();
            tmp=dis.readUTF();
            System.out.println("Otrzymano: "+receiver+"\t"+tmp);
            stmt = con.createStatement();
            rs = stmt.executeQuery("Select * from pytania where PRZEDMIOT_ID in (select id from PRZEDMIOT where nazwa='"+receiver+"' and rodzaj='"+tmp+"')  ORDER BY id asc ");
            while(rs.next()){
                String id=rs.getString(1);
                System.out.println(id);
                StringList.add(id);
                String tresc=rs.getString(2);
                System.out.println(tresc);
                StringList.add(tresc);
                String a=rs.getString(4);
                System.out.println(a);
                if(a==null)
                    a="";
                StringList.add(a);
                String b=rs.getString(5);
                System.out.println(b);
                if(b==null)
                    b="";
                StringList.add(b);
                String c=rs.getString(6);
                System.out.println(c);
                if(c==null)
                    c="";
                StringList.add(c);
                String d=rs.getString(7);
                System.out.println(d);
                if(d==null)
                    d="";
                StringList.add(d);
                String correct=rs.getString(10);
                System.out.println(correct);
                if(correct==null)
                    correct="0";
                StringList.add(correct);
                System.out.println("Dodano: "+id+"\t"+tresc+"\t"+a+"\t"+b+"\t"+c+"\t"+d+"\t"+correct);
                counter++;
            }
            System.out.println("Licznik: "+counter);
            dos.writeInt(counter);
            for(String send:StringList){
                dos.writeUTF(send);
            }
            System.out.println(StringList);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadQuestions()
    {
        StringList.clear();
        IntegerList.clear();
        try {
            stmt=con.createStatement();
            rs = stmt.executeQuery("select przedmiot.ID,PRZEDMIOT.NAZWA,PRZEDMIOT.RODZAJ,TRESC from PYTANIA JOIN PRZEDMIOT ON PRZEDMIOT.ID=PYTANIA.PRZEDMIOT_ID");
            while(rs.next()){
                String support = rs.getString(2);
                System.out.println(support);
                StringList.add(support);
                support = rs.getString(3);
                System.out.println(support);
                StringList.add(support);
                support = rs.getString(4);
                System.out.println(support);
                StringList.add(support);
                counter++;
            }
            System.out.println(counter);
            dos.writeInt(counter);
            for(String send:StringList){
                dos.writeUTF(send);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadHistory()
    {
        try {
            String test = "", percent= "";
            String nick=dis.readUTF();
            String date=dis.readUTF();
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
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void LoadRank()
    {
        StringList.clear();
        IntegerList.clear();
        try {
            stmt=con.createStatement();
            sub = dis.readUTF();
            rs = stmt.executeQuery("select Ranking.ID,PRZEDMIOT.NAZWA,UZYTKOWNIK.NAZWA,RANKING.PUNKTY from RANKING JOIN PRZEDMIOT ON RANKING.PRZEDMIOT_ID=PRZEDMIOT.ID JOIN UZYTKOWNIK ON RANKING.UZYTKOWNIK_ID=UZYTKOWNIK.ID ORDER BY RANKING.PUNKTY DESC");
            List<String> Uzytkownik = new ArrayList<String>();
            List<Integer> Punkty = new ArrayList<Integer>();
            while(rs.next()){
                support = rs.getInt(1);
                IntegerList.add(support);
                przedmiot = rs.getString(2);
                StringList.add(przedmiot);
                uzytkownik = rs.getString(3);
                Uzytkownik.add(uzytkownik);
                support = rs.getInt(4);
                Punkty.add(support);
                counter++;
            }
            System.out.println(counter);
            dos.writeInt(counter);
            for(int i=0;i<counter;i++){
                dos.writeInt(IntegerList.get(i));
                dos.writeUTF(StringList.get(i));
                dos.writeUTF(Uzytkownik.get(i));
                dos.writeInt(Punkty.get(i));
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadSubjectToRank()
    {
        StringList.clear();
        IntegerList.clear();
        try {
            stmt=con.createStatement();
            sub = dis.readUTF();
            rs = stmt.executeQuery("select Ranking.ID,PRZEDMIOT.NAZWA,UZYTKOWNIK.NAZWA,RANKING.PUNKTY from RANKING JOIN PRZEDMIOT ON RANKING.PRZEDMIOT_ID=PRZEDMIOT.ID JOIN UZYTKOWNIK ON RANKING.UZYTKOWNIK_ID=UZYTKOWNIK.ID WHERE PRZEDMIOT.NAZWA='"+sub+"'ORDER BY RANKING.PUNKTY DESC");
            while(rs.next()){
                support = rs.getInt(1);
                IntegerList.add(support);
                String przedmiot = rs.getString(2);
                StringList.add(przedmiot);
                String uzytkownik = rs.getString(3);
                Uzytkownik.add(uzytkownik);
                support = rs.getInt(4);
                Punkty.add(support);
                counter++;
            }
            System.out.println(counter);
            dos.writeInt(counter);
            for(int i=0;i<counter;i++){
                dos.writeInt(IntegerList.get(i));
                dos.writeUTF(StringList.get(i));
                dos.writeUTF(Uzytkownik.get(i));
                dos.writeInt(Punkty.get(i));
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadLoginToRank()
    {
        StringList.clear();
        IntegerList.clear();
        try {
            stmt=con.createStatement();
            sub = dis.readUTF();
            rs = stmt.executeQuery("select Ranking.ID,PRZEDMIOT.NAZWA,UZYTKOWNIK.NAZWA,RANKING.PUNKTY from RANKING JOIN PRZEDMIOT ON RANKING.PRZEDMIOT_ID=PRZEDMIOT.ID JOIN UZYTKOWNIK ON RANKING.UZYTKOWNIK_ID=UZYTKOWNIK.ID WHERE UZYTKOWNIK.NAZWA='"+sub+"'ORDER BY RANKING.PUNKTY DESC");
            while(rs.next()){
                int support = rs.getInt(1);
                IntegerList.add(support);
                String przedmiot = rs.getString(2);
                StringList.add(przedmiot);
                String uzytkownik = rs.getString(3);
                Uzytkownik.add(uzytkownik);
                support = rs.getInt(4);
                Punkty.add(support);
                counter++;
            }
            System.out.println(counter);
            dos.writeInt(counter);

            for(int i=0;i<counter;i++){
                dos.writeInt(IntegerList.get(i));
                dos.writeUTF(StringList.get(i));
                dos.writeUTF(Uzytkownik.get(i));
                dos.writeInt(Punkty.get(i));
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadGenerateTest()
    {
        StringList.clear();
        try {
            stmt=con.createStatement();
            tmp=dis.readUTF();
            rs=stmt.executeQuery("select RODZAJ from Przedmiot WHERE NAZWA='"+tmp+"'");
            while(rs.next()){
                help=rs.getString(1);
                StringList.add(help);
                counter++;
            }
            dos.writeInt(counter);
            for(String help:StringList){
                dos.writeUTF(help);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void AddQuestionClosed()
    {
        try {
            id=dis.readInt();
            text= dis.readUTF();
            aa= dis.readUTF();
            ab= dis.readUTF();
            ac= dis.readUTF();
            ad= dis.readUTF();
            String subject=dis.readUTF();
            String type=dis.readUTF();
            int ra=dis.readInt();
            System.out.println("Otrzymano: "+id+"\t"+text+"\t"+aa+"\t"+ab+"\t"+ac+"\t"+ad+"\t"+ra+"\t"+subject+"\t"+type);
            stmt=con.createStatement();
            if(!aa.equals("") && (ra>0 && ra<5)) {
                rs = stmt.executeQuery("Select id from pytania where przedmiot_id in (select id from przedmiot where id =" + id + " or tresc='" + text + "')");
                while (rs.next())
                    counter++;
                if (counter == 0) {
                    rs = stmt.executeQuery("insert into PYTANIA (ID, TRESC, PRZEDMIOT_ID, ODP_A, ODP_B, ODP_C, ODP_D, POPR_Z) " +
                            "values (" + id + ",'" + text + "',(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and przedmiot.rodzaj='" + type + "'),'" + aa + "','" + ab + "','" + ac + "','" + ad + "'," + ra + " )");

                    dos.writeUTF("Pomyslnie dodano");
                    o_z=0;
                    rs = stmt.executeQuery("update pytania set o_z="+o_z+" where tresc='"+text+"' and odp_a = '"+aa+"' ");
                    System.out.println("O_Z: "+o_z);

                }
                dos.writeUTF("Takie dane juz istnieja w tym przedmiocie");
            }
            else {
                status="Dodaj przynajmniej jedna odpowiedz";
                if ((ra < 1 || ra > 4)) {
                    status = "Zaznacz poprawna odpowiedz";
                    if (aa.equals(""))
                        status="Dodaj przynajmniej jedna odpowiedz";
                }
                dos.writeUTF(status);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void UpdateQuestionClosed()
    {
        try {
            id=dis.readInt();
            text= dis.readUTF();
            aa= dis.readUTF();
            ab= dis.readUTF();
            ac= dis.readUTF();
            ad= dis.readUTF();
            subject=dis.readUTF();
            ra=dis.readInt();
            type=dis.readUTF();
            System.out.println("Otrzymano: "+id+"\t"+text+"\t"+aa+"\t"+ab+"\t"+ac+"\t"+ad+"\t"+ra+"\t"+subject+"\t"+type);
            stmt=con.createStatement();
            if (!aa.equals("")) {
                rs = stmt.executeQuery("update PYTANIA set  TRESC='" + text + "', PRZEDMIOT_ID=(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and Przedmiot.rodzaj ='" + type + "')" +
                        ", ODP_A='" + aa + "', ODP_B='" + ab + "', ODP_C='" + ac + "', ODP_D='" + ad + "', POPR_Z=" + ra + "where ID = " + id + "");
                status = "Pomyslnie zaktualizowano";
                rs = stmt.executeQuery("Select o_z from pytania where tresc='"+text+"' and odp_a = '"+aa+"' and odp_o is not null");
                while (rs.next()) {
                    tmpp=1;
                }
                if(tmpp==1) {
                    o_z = 2;
                    rs = stmt.executeQuery("update pytania set o_z=" + o_z + "  where tresc='" + text + "' and odp_a = '" + aa + "' ");
                }
            }
            else status = "odpowiedz  a nie  moze byc pusta";
            dos.writeUTF(status);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void DeleteQuestion()
    {
        try {
            id=dis.readInt();
            System.out.println("Otrzymano: "+id+"\t");
            stmt=con.createStatement();
            rs=stmt.executeQuery("delete from PYTANIA where ID = "+id+"");
            status="Pomyslnie usunieto";
            dos.writeUTF(status);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadEditOpened()
    {
        StringList.clear();
        try {
            System.out.println("Edycja pytan otwartych");
            receiver=dis.readUTF();
            tmp=dis.readUTF();
            System.out.println("Otrzymano: "+receiver);
            stmt = con.createStatement();
            rs = stmt.executeQuery("Select * from pytania where PRZEDMIOT_ID in (select id from PRZEDMIOT where nazwa='"+receiver+"' and rodzaj = '"+tmp+"') ORDER BY id asc ");
            while(rs.next()){
                String ID=rs.getString(1);
                System.out.println(ID);
                StringList.add(ID);
                text=rs.getString(2);
                System.out.println(text);
                StringList.add(text);
                answer=rs.getString(8);
                if(answer==null)
                    answer="";
                System.out.println(answer);
                StringList.add(answer);
                System.out.println(StringList);
                counter++;
            }
            dos.writeInt(counter);
            for(String send:StringList){
                dos.writeUTF(send);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void AddScore()
    {
        try {
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
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadTestClosed ()
    {
        try {
            stmt=con.createStatement();
            sub=dis.readUTF();
            type = dis.readUTF();
            List<String> listText = new ArrayList<String>();
            List<String> listA = new ArrayList<String>();
            List<String> listB = new ArrayList<String>();
            List<String> listC = new ArrayList<String>();
            List<String> listD = new ArrayList<String>();
            List<Integer> listRA = new ArrayList<Integer>();
            rs=stmt.executeQuery("select TRESC,ODP_A,ODP_B,ODP_C,ODP_D,POPR_Z from PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type+"'AND PYTANIA.O_Z!=1");
            while(rs.next()){
                text =rs.getString(1);
                listText.add(text);
                aa =rs.getString(2);
                listA.add(aa);
                String Odp_b =rs.getString(3);
                listB.add(ab);
                ac =rs.getString(4);
                listC.add(ac);
                String Odp_d =rs.getString(5);
                listD.add(ad);
                ra =rs.getInt(6);
                listRA.add(ra);
                counter++;
            }
            dos.writeInt(counter);
            for(int i=0;i<counter;i++){
                dos.writeInt(i);
                dos.writeUTF(listText.get(i));
                dos.writeUTF(listA.get(i));
                dos.writeUTF(listB.get(i));
                dos.writeUTF(listC.get(i));
                dos.writeUTF(listD.get(i));
                dos.writeInt(listRA.get(i));
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadSubjectToCB()
    {
        StringList.clear();
        try {
            rcv=dis.readUTF();
            stmt=con.createStatement();
            rs=stmt.executeQuery("select Rodzaj from Przedmiot  WHERE NAZWA='"+rcv+"'");
            while(rs.next()){
                tmp=rs.getString(1);
                StringList.add(tmp);
                System.out.println("Dodano: "+tmp);
                counter++;
            }
            dos.writeInt(counter);
            System.out.println("Licznik: "+counter);
            for(String t:StringList){
                dos.writeUTF(t);
                System.out.println("Do wysylki: "+t);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void CountQuestionsClosed()
    {
        try {
            stmt=con.createStatement();
            sub=dis.readUTF();
            type=dis.readUTF();
            System.out.println(sub);
            System.out.println(type);
            rs=stmt.executeQuery("SELECT TRESC,ODP_A,ODP_B,ODP_C,ODP_D,POPR_Z FROM PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type+"' AND PYTANIA.O_Z!=1");
            while(rs.next()){
                text =rs.getString(1);
                aa =rs.getString(2);
                ab =rs.getString(3);
                ac =rs.getString(4);
                ad =rs.getString(5);
                ra =rs.getInt(6);
                counter++;
            }
            dos.writeInt(counter);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteSubject()
    {
        try {
            subject=dis.readUTF();
            type=dis.readUTF();
            System.out.println("Otrzymano: "+subject+"\n"+type);
            stmt=con.createStatement();
            rs=stmt.executeQuery("delete from ranking WHERE przedmiot_id in (select id from przedmiot where NAZWA='"+subject+"' and rodzaj='"+type+"')");
            rs=stmt.executeQuery("delete from pytania WHERE przedmiot_id in (select id from przedmiot where NAZWA='"+subject+"' and rodzaj='"+type+"')");
            rs=stmt.executeQuery("delete from material WHERE przedmiot_id in (select id from przedmiot where NAZWA='"+subject+"' and rodzaj='"+type+"')");
            rs=stmt.executeQuery("delete from przedmiot WHERE NAZWA='"+subject+"' and rodzaj='"+type+"'");
            dos.writeUTF("Pomyslnie usunieto");
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void AddSubject()
    {
        try {
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
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void AddQuestionOpened()
    {
        try {
            id=dis.readInt();
            text= dis.readUTF();
            aa= dis.readUTF();
            subject=dis.readUTF();
            type=dis.readUTF();
            System.out.println("Otrzymano: "+id+"\t"+text+"\t"+aa+"\t"+subject+"\t"+type);
            stmt=con.createStatement();
            if(!aa.equals("")) {
                rs = stmt.executeQuery("Select id from pytania where przedmiot_id in (select id from przedmiot where id =" + id + " or tresc='" + text + "')");
                while (rs.next())
                    counter++;
                if (counter == 0) {
                    rs = stmt.executeQuery("insert into PYTANIA (ID, TRESC, PRZEDMIOT_ID, ODP_O) " +
                            "values (" + id + ",'" + text + "',(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and przedmiot.rodzaj='" + type + "'),'" + aa + "')");
                    dos.writeUTF("Pomyslnie dodano");
                    o_z=1;
                    rs = stmt.executeQuery("update pytania set o_z=" + o_z + "  where tresc='"+text+"' and odp_o = '"+aa+"' ");
                }
                dos.writeUTF("Takie dane juz istnieja w tym przedmiocie");
            }
            else {
                status="Dodaj odpowiedz";
                dos.writeUTF(status);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void UpdateQuestionOpened()
    {
        try {
            id=dis.readInt();
            text= dis.readUTF();
            aa= dis.readUTF();
            subject=dis.readUTF();
            type=dis.readUTF();
            System.out.println("Otrzymano: "+id+"\t"+text+"\t"+aa+"\t"+subject+"\t"+type);
            stmt=con.createStatement();
            if(!aa.equals("")) {
                rs = stmt.executeQuery("update PYTANIA set  TRESC='" + text + "', PRZEDMIOT_ID=(select PRZEDMIOT.ID from PRZEDMIOT where PRZEDMIOT.NAZWA= '" + subject + "' and Przedmiot.rodzaj ='" + type + "' )" +
                        ", ODP_O='" + aa + "' where ID = " + id + "");
                status = "Pomyslnie zaktualizowano";
                rs = stmt.executeQuery("Select o_z from pytania where tresc='"+text+"' and odp_o = '"+aa+"' and odp_a is not null");
                while (rs.next()) {
                    tmpp=1;
                }
                if(tmpp==1) {
                    o_z = 2;
                    rs = stmt.executeQuery("update pytania set o_z=" + o_z + "  where tresc='" + text + "' and odp_o = '" + aa + "' ");
                }
            }
            else status = "Odpowiedz nie moze byc pusta";
            dos.writeUTF(status);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadEditMat()
    {
        StringList.clear();
        try {
            System.out.println("Edycja materialow");
            receiver=dis.readUTF();
            tmp=dis.readUTF();
            System.out.println("Otrzymano: "+receiver);
            stmt = con.createStatement();
            rs = stmt.executeQuery("Select distinct id, (select NAZWA from PRZEDMIOT where nazwa='"+receiver+"' and rodzaj ='"+tmp+"')as nazwa,(select RODZAJ from PRZEDMIOT where nazwa='"+receiver+"' and rodzaj='"+tmp+"') as typ,MATERIAL " +
                    "from material where PRZEDMIOT_ID in (select id from PRZEDMIOT where nazwa='"+receiver+"'" +
                    "and rodzaj = '"+tmp+"') ORDER BY id asc");
            while(rs.next()){
                String ID=rs.getString(1);
                System.out.println(ID);
                StringList.add(ID);
                String Subject=rs.getString(2);
                System.out.println(Subject);
                StringList.add(Subject);
                String Type=rs.getString(3);
                System.out.println(Type);
                StringList.add(Type);
                String Text=rs.getString(4);
                StringList.add(Text);
                counter++;
            }
            dos.writeInt(counter);
            for(String send:StringList){
                dos.writeUTF(send);
            }
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void CountQuestionsOpened()
    {
        StringList.clear();
        try {
            stmt=con.createStatement();
            sub=dis.readUTF();
            type = dis.readUTF();
            rs=stmt.executeQuery("select TRESC,odp_o from PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type+"'AND PYTANIA.O_Z!=0");
            while(rs.next()){
                text =rs.getString(1);
                StringList.add(text);
                answer =rs.getString(2);
                StringList.add(answer);
                counter++;
            }
            dos.writeInt(counter);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void AddMaterial()
    {
        try {
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
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void UpdateMaterial()
    {
        try {
            tmp=dis.readUTF();
            text= dis.readUTF();
            subject=dis.readUTF();
            type=dis.readUTF();
            System.out.println("Otrzymano: "+text+"\t"+subject+"\t"+type);
            stmt=con.createStatement();
            rs=stmt.executeQuery("update Material set material='"+text+"' " +
                    "where material.material = '"+tmp+"'");
            status="Pomyslnie zaktualizowano";
            dos.writeUTF(status);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void DeleteMaterial()
    {
        try {
            text=dis.readUTF();
            System.out.println("Otrzymano: "+text+"\t");
            stmt=con.createStatement();
            rs=stmt.executeQuery("delete from material where material.material = '"+text+"'");
            status="Pomyslnie usunieto";
            dos.writeUTF(status);
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void LoadMaterialAndQuestionCB()
    {
        try {
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
        }catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void DeleteMaterialsQuestions()
    {
        try {
            rcv=dis.readUTF();
            receiver=dis.readUTF();
            tmp=dis.readUTF();
            System.out.println("Otrzymano: "+rcv+"\t"+receiver+"\t"+tmp);
            stmt=con.createStatement();
            if(tmp.equals("PYTANIA")) {
                rs = stmt.executeQuery("delete from PYTANIA where przedmiot_id in (select id from przedmiot where nazwa='" + rcv + "' and rodzaj ='" + receiver + "')");
                status = "Pomyslnie usunieto pytania";
            }
            else if(tmp.equals("MATERIALY")) {
                rs = stmt.executeQuery("delete from MATERIAL where przedmiot_id in (select id from przedmiot where nazwa='" + rcv + "' and rodzaj ='" + receiver + "')");
                status = "Pomyslnie usunieto materialy";
            }
            else status="error";
            dos.writeUTF(status);
            }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void LoadTestOpened()
    {
        try {
            stmt=con.createStatement();
            sub=dis.readUTF();
            type = dis.readUTF();
            List<String> listText = new ArrayList<String>();
            List<String> listAnswer = new ArrayList<String>();

            rs=stmt.executeQuery("select TRESC,odp_o from PYTANIA JOIN PRZEDMIOT ON PYTANIA.PRZEDMIOT_ID=PRZEDMIOT.ID WHERE PRZEDMIOT.NAZWA='"+sub+"' AND PRZEDMIOT.RODZAJ='"+type+"'AND PYTANIA.O_Z!=0");
            while(rs.next()){
               text =rs.getString(1);
                listText.add(text);
                String Popr_o =rs.getString(2);
                listAnswer.add(Popr_o);
                counter++;
            }
            dos.writeInt(counter);
            for(int i=0;i<counter;i++){
                dos.writeInt(i);
                dos.writeUTF(listText.get(i));
                dos.writeUTF(listAnswer.get(i));
            }
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}

