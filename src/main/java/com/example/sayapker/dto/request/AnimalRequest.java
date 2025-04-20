package com.example.sayapker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalRequest {
    private String name;
    private String type;
    private String description;
    private double price;
    private String imageUrl;
}
