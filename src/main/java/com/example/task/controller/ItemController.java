package com.example.task.controller;

import com.example.task.models.Item;
import com.example.task.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/item")
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem (@RequestBody Item item) {
        return itemService.addItem(item);
    }

    @GetMapping
    public Iterable<Item> getAllItems() {
        return itemService.findAll();
    }





}
