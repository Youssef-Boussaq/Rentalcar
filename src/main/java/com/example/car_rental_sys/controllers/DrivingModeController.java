package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.ToolsLib.*;
import com.example.car_rental_sys.orm.Driver;
import com.example.car_rental_sys.sqlParser.SQL;
import com.example.car_rental_sys.ui_components.MessageFrame;
import com.example.car_rental_sys.ui_components.MessageFrameType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


import java.io.File;



public class DrivingModeController  extends Controller{
    @FXML
    Pane sideIconLogo, sideIconHome, sideIconVolume, sideIconPhone, sideIconSetting, browserContainer, distanceDataPane;

    @FXML
    Label distanceLeftLabel, timeLeftLabel;

    @FXML
    ImageView userAvatar;

    @FXML
    public Pane mainPane;

    private Driver driver = (Driver) StatusContainer.currentUser;

    @FXML
    private void initialize() {
        initAvatar();
        initSideIconsEvent();
        initBorwserEvent();
        StatusContainer.currentPageController = this;
    }

    private void initAvatar() {
        userAvatar.setImage(driver.getAvatar());
        ImageTools.setImageShapeToCircle(userAvatar);
    }

    private void initSideIconsEvent() {
        sideIconLogo.setOnMouseClicked(event -> {
            setIconStyle(sideIconLogo);
            backToMainPage();
        });

        sideIconHome.setOnMouseClicked(event -> {
            setIconStyle(sideIconHome);
            backToDriverMainPage();
        });
        sideIconVolume.setOnMouseClicked(event -> {
            setIconStyle(sideIconVolume);
            setVolume();
        });
        sideIconPhone.setOnMouseClicked(event -> {
            phoneIconClicked();
            setIconStyle(sideIconPhone);
        });
        sideIconSetting.setOnMouseClicked(event -> {
            setIconStyle(sideIconSetting);
            backToSettingPage() ;
        });
    }

    private void setIconStyle(Pane pane) {
        clearAllSideIconsStyle();
        pane.getStyleClass().add("sidebarIconActive");
    }

    private void clearAllSideIconsStyle() {
        Pane[] sideIcons = new Pane[]{sideIconLogo, sideIconHome, sideIconVolume, sideIconPhone, sideIconSetting};
        for (Pane sideIcon : sideIcons) {
            sideIcon.getStyleClass().remove("sidebarIconActive");
        }
    }

    private void initBorwserEvent() {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        
        engine.load(new File("src/main/resources/com/example/car_rental_sys/html/directionsDark.html").toURI().toString());

        webView.setPrefSize(300, 490);
        browserContainer.getChildren().add(webView);

        engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                String origin = "'Asia Pacific University of Technology & Innovation'";
                origin = StringTools.replaceSpacialChar(origin);
                String destination = "'endah regal condo'";
                String jsArgs = "getDirections(" + origin + "," + destination + ")";
                engine.executeScript(jsArgs);

                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaBackend", new Object() {
                    public void log(String message) {
                        System.out.println(message);
                        Platform.runLater(() -> updateDistanceData(message));
                    }
                });
                engine.executeScript("var oldLog = console.log; console.log = function(message) { oldLog(message); javaBackend.log(message); };");
            }
        });

        distanceDataPane.toFront();
    }

    private void updateDistanceData(String distanceAndTime) {
        String[] distanceAndTimeArray = distanceAndTime.split(",");
        String distance = distanceAndTimeArray[0];
        String time = distanceAndTimeArray[1];

        distanceLeftLabel.setText(distance);
        timeLeftLabel.setText(time);

    }

    @FXML
    private void userAvatarClicked() {
        System.out.println("show user profile");
    }

    @FXML
    private void backToDriverMainPage() {
        System.out.println("backToDriverMainPage");
        FXTools.changeScene("driverServicePage.fxml");
    }

    @FXML
    private void backToMainPage() {
        System.out.println("backToMainPage");
        FXTools.changeScene("mainPage.fxml");
    }

    @FXML
    private void backToSettingPage() {
        System.out.println("sbackToSettingPage");
        System.out.println("back to setting page");
    }

    @FXML
    private void phoneIconClicked() {
        int userID = StatusContainer.currentOrderCard.userID;
        String phone = DataTools.getPhoneFromUserID(userID);
        PlatformTools.callWhatsApp(phone);
    }

    private void setVolume(){
        //System.out.println("setVolume");
        //deliveredOrder();
        ifFinish();
    }

    private void ifFinish(){
        MessageFrame messageFrame = new MessageFrame(MessageFrameType.CONFIRM, "Are you sure delivered this car?");
        messageFrame.setSuccessCallbackFunc((i) -> {
            deliveredOrder();
            FXTools.changeScene("driverServicePage.fxml");
            return null;
        });

        messageFrame.setFailedCallbackFunc((i) -> {
            messageFrame.close();
            return null;
        });
        messageFrame.show();
    }

    private void  deliveredOrder(){
        // schedule
        String scheduleID = DataTools.getNewID("schedule") + "";
        String orderID= StatusContainer.currentContinueOrderID + "";
        String status = "3";
        String relate = "null";
        String time = DateTools.getNow();

        String sql = "INSERT INTO schedule VALUES ('" + scheduleID + "','" + orderID + "','" + status + "','" + relate + "','" + time + "')";
        System.out.println(sql);

        // order
        String sql2 = "UPDATE orders SET status = '3' WHERE orderID = '" + orderID + "'";
        System.out.println(sql2);

        SQL.execute(sql);
        SQL.execute(sql2);
        sendSystemMsg();

        StatusContainer.currentContinueOrderID = 0;
    }

    private void sendSystemMsg(){
        String orderID = StatusContainer.currentContinueOrderID + "";
        String msg = "Your order("+ orderID +") has been delivered address: " + StatusContainer.currentOrderCard.pickUpLocation;
        DataTools.sendSystemMessage(StatusContainer.currentUser.getUserID(),msg);
    }
}

// TODO: close the browser engine when leave the page
