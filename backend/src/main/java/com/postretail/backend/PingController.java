package com.postretail.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PingController {

    private final PingRepository repo;

    public PingController(PingRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/ping")
    public List<Ping> ping() {
        return repo.findAll();
    }
}
