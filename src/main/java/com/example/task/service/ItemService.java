package com.example.task.service;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.models.Item;


public interface ItemService {

    ItemResponse addItem(ItemRequest request);

    Iterable<Item> findAll();

    Item getItemById(Integer id);

    boolean deleteItem(Integer id);

    ItemResponse updateItemById(ItemRequest request, Integer id);
}
