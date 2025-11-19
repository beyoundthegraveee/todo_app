package com.example.task.service;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.models.Item;
import java.util.Optional;


public interface ItemService {

    ItemResponse addItem(ItemRequest request);

    Iterable<Item> findAll();

    Optional<Item> getItemById(Integer id);

    boolean deleteItem(Integer id);

    ItemResponse updateItemById(ItemRequest request, Integer id);
}
