package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.ToolsLib.DataTools;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;



public class BillingSideBarController {

    @FXML
    private Label billSideText;
    @FXML
    private Pane radioChart,lineChart;


    @FXML
    public void initialize() {
        billSideText.setText("RM "+DataTools.getTotalSpending(StatusContainer.currentUser.getUserID())+ "0");
        initWebView();
    }


    private void initWebView(){
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load(new File("src/main/resources/com/example/car_rental_sys/html/wallet/radio.html").toURI().toString());
        webView.setPrefSize(250, 200);
        radioChart.getChildren().add(webView);

        WebView webView1 = new WebView();
        WebEngine engine1 = webView1.getEngine();
        engine1.load(new File("src/main/resources/com/example/car_rental_sys/html/wallet/bar.html").toURI().toString());
        webView1.setPrefSize(310, 200);
        lineChart.getChildren().add(webView1);
    }
}
