package com.example.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequest {

    @NotBlank(message = "title can not be blank")
    String title;

    @NotBlank
    String description;

}
