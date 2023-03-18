package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private static final Logger LOG = LoggerFactory.getLogger(GreetingController.class);

  @GetMapping("/greetings")
  public ResponseEntity<Greeting> getGreetings() {
    long currentTime = System.nanoTime();
    LOG.info("Greeting Initialized at {}", currentTime);
    if (currentTime % 2 == 0) {
      return ResponseEntity.ok(new Greeting("Good Morning!"));
    } else {
      return ResponseEntity.ok(new Greeting("Good Afternoon!"));
    }
  }
}


