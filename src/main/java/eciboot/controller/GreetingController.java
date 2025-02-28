package eciboot.controller;

import eciboot.annotation.GetMapping;
import eciboot.annotation.RequestParam;
import eciboot.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value= "name", defaultValue = "Mundo") String name){
        return "Hola "+ name +"!!!!";
    }
}
