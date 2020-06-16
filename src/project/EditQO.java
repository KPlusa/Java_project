package project;
/** Klasa przechowujaca dane potrzebne do edycji pytan otwartych*/
public class EditQO {
    private int id;
    private String pytanie, odp;
    public EditQO(){
        this.id= 0;
        this.pytanie= "";
        this.odp="";

    }
    public EditQO(int id, String pytanie, String odp){
        this.id= id;
        this.pytanie= pytanie;
        this.odp=odp;

    }
    public int getId(){
        return  this.id;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public String getPytanie(){ return  this.pytanie;}
    public void setPytanie(String pytanie)
    {
        this.pytanie=pytanie;
    }
    public String getOdp(){
        return  this.odp;
    }
    public void setOdp(String odp_a)
    {
        this.odp=odp;
    }
}
