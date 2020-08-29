package project;
/** Klasa przechowujaca dane do testu zawierającego pytania otwarte*/
public class Testopenclass {
    String tresc, odp_o;
    int id;
    public Testopenclass() {
        this.id = 0;
        this.tresc = "";
        this.odp_o="";
    }
    public Testopenclass(int id, String tresc, String odp_o){
        this.id=id;
        this.tresc=tresc;
        this.odp_o=odp_o;
    }
    public int  getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getTresc(){return tresc;}
    public void setTresc(String tresc){this.tresc=tresc;}
    public String getOdp_o(){return odp_o;}
    public void setOdp_o(String odp_o){this.odp_o=odp_o;}
}
