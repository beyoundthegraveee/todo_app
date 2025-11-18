package com.example.task.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(updatable = false, nullable = false)
    private LocalDate createdAt =  LocalDate.now();


}
