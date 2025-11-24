package com.example.task.controller;

import com.example.task.dto.CategoryRequest;
import com.example.task.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {


    @PostMapping()
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest){

    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@RequestParam Integer id){

    }




}
