package com.example.task.dto;

import com.example.task.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;

    private String categoryName;

    private List<Item> items;

}
