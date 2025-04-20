package com.example.sayapker.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // Жаныбардын аты
    private String type;           // Мисалы: ит, мышык, куш
    private String description;    // Кыскача сүрөттөмө
    private double price;          // Баасы
    private String imageUrl;       // Сүрөттүн URL'и
}
