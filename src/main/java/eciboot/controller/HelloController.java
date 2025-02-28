package eciboot.controller;

import eciboot.annotation.GetMapping;
import eciboot.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public static String index() {
        return "Hola desde EciBoot!";
    }
}
