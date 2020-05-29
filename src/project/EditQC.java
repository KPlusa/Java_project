package project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EditQC {
    private int id;
    private String pytanie;
    private String  odp_a;
    private String  odp_b;
    private String  odp_c;
    private String  odp_d;

    public EditQC(){
    }


    public int getId(){
        return  id;
    }

    public void setId(int id)
    {
        this.id=id;
    }
    ///////////////////////////////
    public String getPytanie(){ return  pytanie;
    }

    public void setPytanie(String pytanie)
    {
        this.pytanie=pytanie;
    }
    //////////////////////////////////
    public String getOdp_a(){
        return  odp_a;
    }

    public void setOdp_a(String odp_a)
    {
        this.odp_a=odp_a;
    }
    //////////////////////////////////

    public String getOdp_b(){
        return  odp_b;
    }

    public void setOdp_b(String odp_b)
    {
        this.odp_b=odp_b;
    }
    //////////////////////////////////

    public String getOdp_c(){
        return  odp_c;
    }

    public void setOdp_c(String odp_c)
    {
        this.odp_c=odp_c;
    }
    //////////////////////////////////

    public String getOdp_d(){
        return  odp_d;
    }

    public void setOdp_d(String odp_d)
    {
        this.odp_d=odp_d;
    }
    //////////////////////////////////

}
