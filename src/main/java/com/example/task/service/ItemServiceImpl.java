package com.example.task.service;

import com.example.task.models.Item;
import com.example.task.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }
}
