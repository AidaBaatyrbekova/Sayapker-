package com.example.sayapker.mapper;

import com.example.sayapker.dto.request.AnimalRequest;
import com.example.sayapker.dto.response.AnimalResponse;
import com.example.sayapker.model.entity.Animal;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    public static Animal toEntity(AnimalRequest request) {
        return Animal.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .build();
    }

    public static AnimalResponse toResponse(Animal animal) {
        return AnimalResponse.builder()
                .id(animal.getId())
                .name(animal.getName())
                .type(animal.getType())
                .description(animal.getDescription())
                .price(animal.getPrice())
                .imageUrl(animal.getImageUrl())
                .build();
    }
}
