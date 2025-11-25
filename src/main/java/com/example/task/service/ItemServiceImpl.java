package com.example.task.service;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.exception.ItemNotFoundException;
import com.example.task.models.Item;
import com.example.task.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;


    @Override
    public ItemResponse addItem(ItemRequest request) {
        Item item = new Item();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        itemRepository.save(item);

        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    @Override
    public List<ItemResponse> findAll() {
        List<ItemResponse> itemResponses = new  ArrayList<>();
        itemRepository.findAll().forEach(item ->
                itemResponses.add(new ItemResponse(
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                )));

        return  itemResponses;
    }

    @Override
    public ItemResponse getItemById(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }


    @Override
    public boolean deleteItem(Integer id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ItemResponse updateItemById(ItemRequest request, Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new);
        if(request.getTitle()!=null && !request.getTitle().isBlank()){
            item.setTitle(request.getTitle());
        }

        if(request.getDescription()!=null && !request.getDescription().isBlank()){
            item.setDescription(request.getDescription());
        }

        item.setUpdatedAt(LocalDateTime.now());
        itemRepository.save(item);

        return  new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );

    }
}


