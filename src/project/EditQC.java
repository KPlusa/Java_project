package project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EditQC {
    private IntegerProperty id;
    private StringProperty pytanie;
    private StringProperty odp_a;
    private StringProperty odp_b;
    private StringProperty odp_c;
    private StringProperty odp_d;

    public EditQC(){
        this.id= new SimpleIntegerProperty();
        this.pytanie= new SimpleStringProperty();
        this.odp_a=new SimpleStringProperty();
        this.odp_b=new SimpleStringProperty();
        this.odp_c=new SimpleStringProperty();
        this.odp_d=new SimpleStringProperty();
    }
    public int get_id(){
        return  id.get();
    }
    public IntegerProperty get_Pid(){
        return  id;
    }
    public void set_id(int id)
    {
        this.id.set(id);
    }
    ///////////////////////////////
    public String get_pytanie(){ return  pytanie.get();
    }
    public StringProperty get_Ppytanie()
    {
        return pytanie;
    }
    public void set_pytanie(String pytanie)
    {
        this.pytanie.set(pytanie);
    }
    //////////////////////////////////
    public String get_odp_a(){
        return  odp_a.get();
    }
    public StringProperty get_Podp_a()
    {
        return odp_a;
    }
    public void set_Podp_a(String odp_a)
    {
        this.odp_a.set(odp_a);
    }
    //////////////////////////////////

    public String get_odp_b(){
        return  odp_b.get();
    }
    public StringProperty get_Podp_b()
    {
        return odp_b;
    }
    public void set_Podp_b(String odp_b)
    {
        this.odp_b.set(odp_b);
    }
    //////////////////////////////////

    public String get_odp_c(){
        return  odp_c.get();
    }
    public StringProperty get_Podp_c()
    {
        return odp_c;
    }
    public void set_Podp_c(String odp_c)
    {
        this.odp_c.set(odp_c);
    }
    //////////////////////////////////

    public String get_odp_d(){
        return  odp_d.get();
    }
    public StringProperty get_Podp_d()
    {
        return odp_d;
    }
    public void set_Podp_d(String odp_d)
    {
        this.odp_d.set(odp_d);
    }
    //////////////////////////////////

}
