package project;

public class EditQC {
    private int id,right_answer;
    private String pytanie, odp_a, odp_b, odp_c, odp_d;
    public EditQC(){
        this.id= 0;
        this.right_answer=0;
        this.pytanie= "";
        this.odp_a="";
        this.odp_b="";
        this.odp_c="";
        this.odp_d="";
        this.right_answer=0;

    }
    public EditQC(int id, String pytanie, String odp_a, String odp_b, String odp_c, String odp_d,int right_answer){
        this.id= id;
        this.pytanie= pytanie;
        this.odp_a=odp_a;
        this.odp_b=odp_b;
        this.odp_c=odp_c;
        this.odp_d=odp_d;
        this.right_answer=right_answer;
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
    public String getOdp_a(){
        return  this.odp_a;
    }
    public void setOdp_a(String odp_a)
    {
        this.odp_a=odp_a;
    }
    public String getOdp_b(){
        return  this.odp_b;
    }
    public void setOdp_b(String odp_b)
    {
        this.odp_b=odp_b;
    }
    public String getOdp_c(){
        return  this.odp_c;
    }
    public void setOdp_c(String odp_c)
    {
        this.odp_c=odp_c;
    }
    public String getOdp_d(){
        return  this.odp_d;
    }
    public void setOdp_d(String odp_d)
    {
        this.odp_d=odp_d;
    }
    public int getRight_answer() {return this.right_answer; }
    public void setRight_answer(int right_answer) {
        this.right_answer = right_answer;
    }
}
