package com.example.task.service;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.models.Item;

import java.util.List;
import java.util.Optional;


public interface ItemService {

    ItemResponse addItem(ItemRequest request);

    List<ItemResponse> findAll();

    ItemResponse getItemById(Long id);

    boolean deleteItem(Long id);

    ItemResponse updateItemById(ItemRequest request, Long id);
}
