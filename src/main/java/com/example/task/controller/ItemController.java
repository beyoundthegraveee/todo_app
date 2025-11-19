package com.example.task.controller;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.exception.ItemNotFoundException;
import com.example.task.models.Item;
import com.example.task.service.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ResponseEntity<ItemResponse> createItem ( @RequestBody @Valid ItemRequest request) {
        ItemResponse itemResponse = itemService.addItem(request);
        return new ResponseEntity<>(itemResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Integer id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(ItemNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Integer id) {
        boolean isDeleted = itemService.deleteItem(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else  {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItemById(@PathVariable Integer id, @RequestBody @Valid ItemRequest request) {
        ItemResponse itemResponse = itemService.updateItemById(request, id);
        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

}
