package com.example.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemResponse {

    @NotBlank
    long id;

    @NotBlank
    String title;

    @NotBlank
    String description;

    @NotBlank
    LocalDateTime createdAt;

    @NotBlank
    LocalDateTime updatedAt;


}
