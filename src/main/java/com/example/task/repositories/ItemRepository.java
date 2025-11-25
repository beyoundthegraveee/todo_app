package com.example.task.repositories;

import com.example.task.models.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

    boolean existsById(Long id);

    Item findById(Long id);
}
