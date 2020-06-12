package project;

public class Rankclass {
    int id,subject_id,user_id,score,data;

    public Rankclass(){
        this.id=0;
        this.subject_id=0;
        this.user_id=0;
        this.score=0;
        this.data=0;
    }
    public Rankclass(int id, int subject_id, int user_id, int score, int data){
        this.id=id;
        this.subject_id=subject_id;
        this.user_id=user_id;
        this.score=score;
        this.data=data;
    }
    public int getid(){
        return id;
    }
    public void setid(int id){
        this.id=id;
    }
    public int getSubject_id(){
        return subject_id;
    }
    public void setSubject_id(int subject_id){
        this.subject_id=subject_id;
    }
    public int getuser_id(){
        return user_id;
    }
    public void setuser_id(int id){
        this.user_id=user_id;
    }
    public int getscore(){
        return score;
    }
    public void setscore(int score){
        this.score=score;
    }
    public int getdata(){
        return data;
    }
    public void setdata(int data){
        this.data=data;
    }

}

