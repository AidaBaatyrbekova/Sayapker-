package com.example.sayapker.controller;

import com.example.sayapker.dto.request.AnimalRequest;
import com.example.sayapker.dto.response.AnimalResponse;
import com.example.sayapker.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<AnimalResponse> create(@RequestBody AnimalRequest request) {
        return ResponseEntity.ok(animalService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAll() {
        return ResponseEntity.ok(animalService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> update(@PathVariable Long id,
                                                 @RequestBody AnimalRequest request) {
        return ResponseEntity.ok(animalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        animalService.delete(id);
        return ResponseEntity.ok("Animal deleted!");
    }
}