package project;

public class Questionsclass {
    private int id;
    private String przedmiot,typ,tresc;
        public Questionsclass(){
            this.id=0;
            this.przedmiot="";
            this.typ="";
            this.tresc="";
        }
    public Questionsclass(int id, String przedmiot, String typ, String tresc){
        this.id=id;
        this.przedmiot=przedmiot;
        this.typ=typ;
        this.tresc=tresc;
    }
        public int getId(){
            return id;
        }
        public void setId(int id){
            this.id=id;
        }
        public String getPrzedmiot(){
            return przedmiot;
        }
        public void setPrzedmiot(String przedmiot){
            this.przedmiot=przedmiot;
        }
        public String getTyp(){
            return typ;
        }
        public void setTyp(String typ){
            this.typ=typ;
        }
        public String getTresc(){
            return tresc;
        }
        public void setTresc(String tresc){
            this.tresc=tresc;
        }


}
