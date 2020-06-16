package project;

public class EditMatClass {
    private String tresc;
    public EditMatClass()
    {
        this.tresc="";

    }
    public EditMatClass(int id, String przedmiot, String typ_przedmiotu, String tresc){
        this.tresc=tresc;

    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

}
