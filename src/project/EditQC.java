package project;



public class EditQC {
    private int id;
    private String pytanie;
    private String odp_a;
    private String odp_b;
    private String odp_c;
    private String odp_d;

    public EditQC(){
        this.id= 0;
        this.pytanie= "";
        this.odp_a="";
        this.odp_b="";
        this.odp_c="";
        this.odp_d="";
    }
    public int get_id(){
        return  id;
    }
    public void set_id(int id)
    {
        this.id=id;
    }
    ///////////////////////////////
    public String get_pytanie(){ return  pytanie;
    }
    public void set_pytanie(String pytanie)
    {
        this.pytanie=pytanie;
    }
    //////////////////////////////////
    public String get_odp_a(){
        return  odp_a;
    }
    public void set_Podp_a(String odp_a)
    {
        this.odp_a=odp_a;
    }
    //////////////////////////////////

    public String get_odp_b(){
        return  odp_b;
    }
    public void set_Podp_b(String odp_b)
    {
        this.odp_b=odp_b;
    }
    //////////////////////////////////

    public String get_odp_c(){
        return  odp_c;
    }
    public void set_Podp_c(String odp_c)
    {
        this.odp_c=odp_c;
    }
    //////////////////////////////////

    public String get_odp_d(){
        return  odp_d;
    }
    public void set_Podp_d(String odp_d)
    {
        this.odp_d=odp_d;
    }
    //////////////////////////////////

}
