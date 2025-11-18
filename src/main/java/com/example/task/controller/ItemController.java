package com.example.task.controller;

import com.example.task.models.Item;
import com.example.task.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Item> getAllItems() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Item> getItemById(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }







}
