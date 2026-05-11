package com.example.car_rental_sys.controllers;


import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.io.File;
import java.net.URL;

// import BrowserType



public class dashboardController {
    @FXML
    WebView webview;

    @FXML
    Pane browserPane;

    public static dashboardController instance;


    @FXML
    public void initialize() {
        System.out.println("dashboardController");
        initWebview();
        // initJxBrowser();
        instance = this;
    }


    private void  initWebview(){
        WebEngine engine = webview.getEngine();
        URL url = this.getClass().getResource("/com/example/car_rental_sys/html/dashboard/index.html");
        assert url != null;
        engine.load(url.toString());
    }

    private void  initJxBrowser(){


        Engine engine = Engine.newInstance(RenderingMode.OFF_SCREEN);



        browser = engine.newBrowser();

        browser.navigation().loadUrl(new File("src/main/resources/com/example/car_rental_sys/html/dashboard/index.html").getAbsolutePath());
        BrowserView view = BrowserView.newInstance(browser);
        view.setPrefSize(1000,750);
        browserPane.getChildren().add(view);
    }

    public void changePage(String type){
        WebEngine engine = webview.getEngine();
        if (type.equals("dashboard")){
            engine.load(this.getClass().getResource("/com/example/car_rental_sys/html/dashboard/index.html").toString());
            AdminServiceController.instance.changeIcon("right");
        }else if (type.equals("income")){
            engine.load(this.getClass().getResource("/com/example/car_rental_sys/html/incomeAnalysis/index.html").toString());
            AdminServiceController.instance.changeIcon("left");
        }
    }

}
