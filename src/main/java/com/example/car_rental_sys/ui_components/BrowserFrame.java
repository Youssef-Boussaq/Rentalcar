package com.example.car_rental_sys.ui_components;

import com.example.car_rental_sys.ToolsLib.ImageTools;
import com.example.car_rental_sys.funtions.JavaObject;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import javafx.concurrent.Worker;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.function.Function;



public class BrowserFrame {
    private int width;
    private int height;
    private String url;
    private double locationX,locationY;
    protected  Stage stage =  new Stage();
    protected  WebView webView = null;
    protected  Function<String,Void> callBackFunc = null;

    // style
    private String captionBarBgc = "#323844";

    public BrowserFrame(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }


    private void initBrowser() {
        webView = new WebView();
        WebEngine engine = webView.getEngine();

        if(url.startsWith("http")){
            engine.load(this.url);
        }else{
            engine.load(new File(this.url).toURI().toString());
        }
        
        // Expose a callback for JavaFX WebView
        engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("java", new JavaObject());
            }
        });
    }

    private void initStage(){


        BorderPane browserContainer = new BorderPane(webView);
        browserContainer.setPrefSize(width,height);
        browserContainer.setLayoutX(0);
        browserContainer.setLayoutY(30);

        Pane root = new Pane();
        root.getChildren().addAll(browserContainer,getCaptionBar(),getCloseButton());

        Scene scene = new Scene(root,this.width,this.height +30);

        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
    }

    public Label  getCaptionBar(){
        Label captionBar = new Label();
        captionBar.setPrefSize(this.width,30);
        captionBar.setStyle("-fx-background-color: " + captionBarBgc + ";");
//        captionBar.setStyle("-fx-background-color: #818181");
        captionBar.setLayoutX(0);
        captionBar.setLayoutY(0);

        captionBar.setOnMousePressed((MouseEvent event) -> {
            locationX = stage.getX() - event.getScreenX();
            locationY = stage.getY() - event.getScreenY();
        });

        captionBar.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() + locationX);
            stage.setY(event.getScreenY() + locationY);
        });
        return  captionBar;
    }


    public ImageView getCloseButton(){
        ImageView closeIcon = new ImageView();
        closeIcon.setX(this.width - 30);
        closeIcon.setY(5);
        closeIcon.setFitWidth(20);
        closeIcon.setFitHeight(20);

        Image image = ImageTools.getImageObjFromPath("src/main/resources/com/example/car_rental_sys/image/UI/closeIconFrame.png");
        Image imageHover = ImageTools.getImageObjFromPath("src/main/resources/com/example/car_rental_sys/image/UI/closeIconHoverFrame.png");

        closeIcon.setImage(image);

        closeIcon.setOnMouseClicked(e -> stage.close());

        closeIcon.setOnMouseEntered(e -> {
            closeIcon.setImage(imageHover);
            stage.getScene().setCursor(Cursor.HAND);
        });

        closeIcon.setOnMouseExited(e -> {
            closeIcon.setImage(image);
            stage.getScene().setCursor(Cursor.DEFAULT);
        });
        return  closeIcon;
    }


    private  void callBack( String message){
        System.out.println("call back message: [" + message + "] from BrowserFrame");
        callBackFunc.apply(message);
    }

    public void show(){
        initBrowser();
        initStage();
        setJavaScriptFunc();
        stage.show();
    }

    private void closeCallBack(){
        callBackFunc.apply("close");
    }

    public void setCloseCallBack(Function<String,Void> callBackFunc){
        stage.setOnCloseRequest(e -> callBackFunc.apply("close"));
    }

    public void setJavaScriptFunc(){
        // Handled in initBrowser for WebView
    }


}
