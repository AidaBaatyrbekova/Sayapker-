package com.example.sayapker.service.impl;

import com.example.sayapker.dto.request.AnimalRequest;
import com.example.sayapker.dto.response.AnimalResponse;
import com.example.sayapker.exception.NotFoundException;
import com.example.sayapker.mapper.AnimalMapper;
import com.example.sayapker.model.entity.Animal;
import com.example.sayapker.repository.AnimalRepository;
import com.example.sayapker.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    @Override
    public AnimalResponse create(AnimalRequest request) {
        Animal animal = AnimalMapper.toEntity(request);
        return AnimalMapper.toResponse(animalRepository.save(animal));
    }

    @Override
    public AnimalResponse getById(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal not found with id: " + id));
        return AnimalMapper.toResponse(animal);
    }

    @Override
    public List<AnimalResponse> getAll() {
        return animalRepository.findAll()
                .stream()
                .map(AnimalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AnimalResponse update(Long id, AnimalRequest request) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal not found with id: " + id));

        animal.setName(request.getName());
        animal.setType(request.getType());
        animal.setDescription(request.getDescription());
        animal.setPrice(request.getPrice());
        animal.setImageUrl(request.getImageUrl());

        return AnimalMapper.toResponse(animalRepository.save(animal));
    }

    @Override
    public void delete(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new NotFoundException("Animal not found with id: " + id);
        }
        animalRepository.deleteById(id);
    }
}
