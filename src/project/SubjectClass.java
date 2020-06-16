package project;
/** Klasa przechowujaca dane dotyczące przedmiotu*/
public class SubjectClass {
    private String nazwa;
    private String rodzaj;
    public SubjectClass(){
        this.nazwa="";
        this.rodzaj="";
    }
    public SubjectClass(String nazwa,String rodzaj){
        this.nazwa=nazwa;
        this.rodzaj=rodzaj;
    }
    public String getNazwa(){
        return nazwa;
    }
    public void setnazwa(String nazwa){
        this.nazwa=nazwa;
    }
    public String getRodzaj(){
        return rodzaj;
    }
    public void setRodzaj(String rodzaj){
        this.rodzaj=rodzaj;
    }

}