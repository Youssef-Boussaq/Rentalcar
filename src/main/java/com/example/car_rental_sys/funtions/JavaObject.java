package com.example.car_rental_sys.funtions;

import com.example.car_rental_sys.StatusContainer;

public final class JavaObject {
    public String sayHelloTo(String firstName) {
        System.out.println( "Hello, " + firstName + "!");
        return "Hello " + firstName + "!";
    }

    public String sayByeTo(String firstName) {
        System.out.println( "ByeBye, " + firstName + "!");
        return "ByeBye " + firstName + "!";
    }

    public void setLocationText(String text) {
        //System.out.println( "Location: " + text + "!");
        StatusContainer.locationChose = text;
    }


}