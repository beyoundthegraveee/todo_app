package com.example.task.service;

import com.example.task.models.Item;

import java.util.Optional;


public interface ItemService {

    Item addItem(Item item);

    Iterable<Item> findAll();

    Optional<Item> getItemById(Integer id);
}
