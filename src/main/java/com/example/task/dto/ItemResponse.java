package com.example.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemResponse {

    long id;

    String title;

    String description;

    LocalDate createdAt;



}
