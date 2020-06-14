package project;

public class Rankclass {
    int id,points;
    String subject,name;

    public Rankclass(){
        this.id=0;
        this.subject="";
        this.name="";
        this.points=0;
    }
    public Rankclass(int id, String subject, String name, int points){
        this.id=id;
        this.subject=subject;
        this.name=name;
        this.points=points;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getSubject(){
        return subject;
    }
    public void setSubject(String subject){
        this.subject=subject;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public int getPoints(){
        return points;
    }
    public void setPoints(int points){
        this.points=points;
    }


}

