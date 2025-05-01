package com.example.sayapker.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimalResponse {
    private Long id;
    private String name;
    private String type;
    private String description;
    private double price;
    private String imageUrl;
}
