package project;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Storage {
    public String login;
    public String subject_storage;
    public String type_storage;
    public Stage stage;
    public boolean what;
    public double x,y;
    @FXML
    public AnchorPane AnchorPaneMain;
    public void store_username(String lgn)
    {
        this.login=lgn;
        //System.out.println("Mam: "+lgn);
    }

    public void store_subject(String sbj)
    {
        this.subject_storage=sbj;
        System.out.println("Mam: "+sbj);
    }
    public void store_type(String tp)
    {
        this.type_storage=tp;
        System.out.println("Mam: "+tp);
    }

    public void minAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeAction(MouseEvent event) {
        System.exit(0);
    }

    public void makeDraggable() {
        AnchorPaneMain.setOnMousePressed(((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));
        AnchorPaneMain.setOnMouseDragged(((event) -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if(!stage.isMaximized()) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        }));
    }

    public void  maxAction(MouseEvent event){
        Stage stage=(Stage) AnchorPaneMain.getScene().getWindow();
        if(stage.isMaximized()) {
            stage.setMaximized(false);
            stage.setResizable(false);
            what=false;
        }
        else {
            stage.setMaximized(true);
            stage.setResizable(true);
            what=true;
        }
    }


}
