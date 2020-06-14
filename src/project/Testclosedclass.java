package project;

public class Testclosedclass {
    String tresc, odp_a, odp_b, odp_c,odp_d;
    int odp_popr;
    public Testclosedclass()
    {
        this.tresc="";
        this.odp_a="";
        this.odp_b="";
        this.odp_c="";
        this.odp_d="";
        this.odp_popr=0;
    }
    public Testclosedclass(String tresc, String odp_a, String odp_b, String odp_c, String odp_d, int odp_popr)
    {
        this.tresc=tresc;
        this.odp_a=odp_a;
        this.odp_b=odp_b;
        this.odp_c=odp_c;
        this.odp_d=odp_d;
        this.odp_popr=odp_popr;
    }
    public String  getTresc(){
        return tresc;
    }
    public String  getOdp_a(){
        return odp_a;
    }
    public String  getOdp_b(){
        return odp_b;
    }
    public String  getOdp_c(){
        return odp_c;
    }
    public String  getOdp_d(){
        return odp_d;
    }
    public int  getOdp_popr(){
        return odp_popr;
    }
    public void setTresc(String tresc){
        this.tresc=tresc;
    }
    public void setOdp_a(String odp_a){
        this.odp_a=odp_a;
    }
    public void setOdp_b(String odp_b){
        this.odp_b=odp_b;
    }
    public void setOdp_c(String odp_c){
        this.odp_c=odp_c;
    }
    public void setOdp_d(String odp_d){
        this.odp_d=odp_d;
    }
    public void setOdp_popr(int odp_popr){
        this.odp_popr=odp_popr;
    }
}
