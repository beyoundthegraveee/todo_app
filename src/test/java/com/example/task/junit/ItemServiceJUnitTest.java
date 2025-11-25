package com.example.task.junit;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.models.Item;
import com.example.task.repositories.ItemRepository;
import com.example.task.service.ItemService;
import com.example.task.service.ItemServiceImpl;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceJUnitTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    @DisplayName("addItem should save new Item and return ItemResponse")
    void addItemShouldCreateNewItem(){
        ItemRequest itemRequest = new ItemRequest(
                "Test title",
                "Test description"
        );

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            item.setId(1);
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            return item;
        });

        ItemResponse itemResponse = itemService.addItem(itemRequest);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);

        verify(itemRepository).save(captor.capture());

        Item savedItem = captor.getValue();

        assertThat(savedItem.getId()).isEqualTo(1);
        assertThat(savedItem.getTitle()).isEqualTo("Test title");
        assertThat(savedItem.getDescription()).isEqualTo("Test description");

        assertThat(itemResponse.getId()).isEqualTo(1);
        assertThat(itemResponse.getTitle()).isEqualTo("Test title");
        assertThat(itemResponse.getDescription()).isEqualTo("Test description");
        assertThat(itemResponse.getCreatedAt()).isNotNull();
        assertThat(itemResponse.getUpdatedAt()).isNotNull();
    }




}
