package com.example.sayapker.service;

import com.example.sayapker.dto.request.AnimalRequest;
import com.example.sayapker.dto.response.AnimalResponse;

import java.util.List;

public interface AnimalService {
    AnimalResponse create(AnimalRequest request);
    AnimalResponse getById(Long id);
    List<AnimalResponse> getAll();
    AnimalResponse update(Long id, AnimalRequest request);
    void delete(Long id);
}