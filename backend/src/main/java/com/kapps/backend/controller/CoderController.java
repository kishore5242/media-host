package com.kapps.backend.controller;


import com.kapps.backend.jpa.Coder;
import com.kapps.backend.jpa.CoderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coders")
public class CoderController {

    private final CoderRepository coderRepository;

    public CoderController(CoderRepository coderRepository) {
        this.coderRepository = coderRepository;
    }

    @GetMapping
    public List<Coder> getCoders() {
        return coderRepository.findAll();
    }

    @PostMapping
    public Coder addCoder(@RequestBody Coder coder) {
        return coderRepository.save(coder);
    }
}
