package org.marakobz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping(produces = "text/plain")
    public ResponseEntity<String> getHello() {
        String message = "GET";
        return ResponseEntity.ok(message);
    }

    @PostMapping(produces = "text/plain")
    public ResponseEntity<String> postHello() {
        String message = "POST";
        return ResponseEntity.ok(message);
    }
}
