package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.ToolsLib.DataTools;
import com.example.car_rental_sys.ToolsLib.FXTools;
import com.example.car_rental_sys.orm.Admin;
import com.example.car_rental_sys.orm.Customer;
import com.example.car_rental_sys.orm.Driver;
import com.example.car_rental_sys.sqlParser.SQL;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Objects;



public class MessagePageController {
    @FXML
    Pane mainPane,navBarPane;

    @FXML
    public void initialize() {
        initBrowser();
        //System.out.println("MessagePageController initialize");
        initData();
    }

    private void initBrowser() {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        engine.load(new File("src/main/resources/com/example/car_rental_sys/html/contactUs/index.html").toURI().toString());

        webView.setPrefSize(1280,832);
        mainPane.getChildren().add(webView);

        navBarPane.toFront();

        engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaBackend", new Object() {
                    public void log(String message) {
                        if(Objects.equals(message, "back to service")){
                            //backToService();
                        }else if (Objects.equals(message, "close")){
                            System.exit(0);
                        }else if (message.startsWith("[") && message.endsWith("]")){
                            generateMessage(message);
                        }else if(message.startsWith("chatWith:")){
                            removeUnread(message);
                        }
                    }
                });
                engine.executeScript("var oldLog = console.log; console.log = function(message) { oldLog(message); javaBackend.log(message); };");
            }
        });
    }

    private void initData(){
        DataTools.generateMessageJSON(StatusContainer.currentUser.getUserID());
    }

    private void generateMessage(String message){
        int id = DataTools.getID("messages") ;
        int status = 0;

        message = message.substring(1,message.length()-1);

        String[] messageArray = message.split(",");

        if(messageArray.length !=5) return;

        // insert to db with sql
        //  (id,status, type,senderID, receiverID, message)
        String sql = "INSERT INTO messages VALUES ("+id+", " +status+ ", "
                +messageArray[0]+", "+messageArray[1]+", "+messageArray[2]+", '"
                +messageArray[3] +"', '" + messageArray[4] +"')";

        SQL.execute(sql);
        System.out.println("add message: " + sql);
    }

    private void removeUnread(String message){
        String[] messageArray = message.split(":");
        String senderID = messageArray[1];
        String receiverID = messageArray[2];

        String sql = "UPDATE messages SET status = 1 WHERE senderID = "+senderID+" AND receiverID = "+receiverID;
        SQL.execute(sql);

        if(Objects.equals(receiverID, "undefined")) return;
        sql = "UPDATE messages SET status = 1 WHERE senderID = "+receiverID+" AND receiverID = "+senderID;
        SQL.execute(sql);
        System.out.println("remove unread: " + sql);
    }



    private void backToService(){
        Platform.runLater(() -> {
            try {
                if(StatusContainer.currentUser instanceof Admin){
                    FXTools.changeScene("adminServicePage.fxml");
                }else if (StatusContainer.currentUser instanceof Driver){
                    FXTools.changeScene("driverServicePage.fxml");
                }else if (StatusContainer.currentUser instanceof Customer){
                    FXTools.changeScene("customerServicePage.fxml");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

// TODO: drag bar