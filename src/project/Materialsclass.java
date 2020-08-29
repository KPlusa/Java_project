package project;
/** Klasa przechowujaca dane dotyczace materialow*/
public class Materialsclass {
    private String material;
    public Materialsclass(){
    }
    public Materialsclass(String material){
        this.material=material;
    }
    public void setMaterial(String material){
        this.material=material;
    }
    public String getMaterial(){
        return this.material;
    }
}
