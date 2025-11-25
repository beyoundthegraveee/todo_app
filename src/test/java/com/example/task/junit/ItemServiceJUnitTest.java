package com.example.task.junit;

import com.example.task.dto.ItemRequest;
import com.example.task.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor
@ExtendWith(MockitoExtension.class)
public class ItemServiceJUnitTest {

    @Mock
    private final ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }




}
