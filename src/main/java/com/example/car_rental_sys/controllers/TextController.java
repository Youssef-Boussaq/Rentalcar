package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.orm.Order;
import com.example.car_rental_sys.ui_components.OrderCard;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class TextController {
    @FXML
    Pane mainPane;

    WebView webView;


    @FXML
    public void initialize() {
        OrderCard orderCard = new OrderCard(new Order(1));
        mainPane.getChildren().add(orderCard);
        addBrowser();
    }

    public  void addBrowser(){
        webView = new WebView();
        WebEngine engine = webView.getEngine();

        engine.load("https://html5test.com");
        webView.setPrefSize(300, 300);
        mainPane.getChildren().add(webView);
    }

    @FXML
    private void buttonClick() {
        webView.setPrefSize(600, 600);
    }
}
