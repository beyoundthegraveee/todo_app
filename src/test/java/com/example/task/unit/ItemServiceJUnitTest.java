package com.example.task.unit;
import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.exception.ItemNotFoundException;
import com.example.task.models.Item;
import com.example.task.repositories.ItemRepository;
import com.example.task.service.ItemServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
    }


    @Test
    @DisplayName("findAll should map all entities to ItemResponse")
    void findAllShouldReturnListOfItemResponses(){
        Item item1 = new Item();
        item1.setTitle("Title 1");
        item1.setDescription("Description 1");

        Item item2 = new Item();
        item2.setTitle("Title 2");
        item2.setDescription("Description 2");

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<ItemResponse> itemResponses = itemService.findAll();

        assertThat(itemResponses.size()).isEqualTo(2);

        ItemResponse itemResponse1 = itemResponses.getFirst();
        assertThat(itemResponse1.getId()).isEqualTo(1);
        assertThat(itemResponse1.getTitle()).isEqualTo("Title 1");
        assertThat(itemResponse1.getDescription()).isEqualTo("Description 1");

        ItemResponse itemResponse2 = itemResponses.get(1);
        assertThat(itemResponse2.getId()).isEqualTo(2);
        assertThat(itemResponse2.getTitle()).isEqualTo("Title 2");
        assertThat(itemResponse2.getDescription()).isEqualTo("Description 2");

        verify(itemRepository).findAll();
    }


    @Test
    @DisplayName("getItemById should return ItemResponse with the same id")
    void getItemByIdShouldReturnItemResponseWhenExists(){
        Item item = new Item();
        item.setId(10);
        item.setTitle("Some Title");
        item.setDescription("Some Description");

        when(itemRepository.findById(10)).thenReturn(Optional.of(item));

        ItemResponse itemResponse = itemService.getItemById(10);

        assertThat(itemResponse.getId()).isEqualTo(10);
        assertThat(itemResponse.getTitle()).isEqualTo("Some Title");
        assertThat(itemResponse.getDescription()).isEqualTo("Some Description");

        verify(itemRepository).findById(10);
    }

    @Test
    @DisplayName("getItemById should return ItemNotFoundException when item does not exist")
    void getItemByIdShouldReturnItemNotFoundExceptionWhenNotFound(){
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemById(999)).isInstanceOf(ItemNotFoundException.class);

        verify(itemRepository).findById(999);
    }


    @Test
    @DisplayName("deleteItemById should delete item and return true")
    void deleteItemByIdShouldReturnTrueWhenItemExist(){
        Item item = new Item();
        item.setId(5);

        when(itemRepository.findById(5)).thenReturn(Optional.of(item));

        boolean Result = itemService.deleteItem(5);
        assertThat(Result).isTrue();
        verify(itemRepository).findById(5);
        verify(itemRepository).deleteById(5);
    }

    @Test
    void deleteItemByIdShouldReturnFalseWhenItemDoesNotExist(){
        when(itemRepository.findById(8)).thenReturn(Optional.empty());

        boolean Result = itemService.deleteItem(8);
        assertThat(Result).isFalse();
        verify(itemRepository).findById(8);
        verify(itemRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("updateItem should update all features of an old item and return ItemResponse")
    void updateItemByIdShouldUpdateItemWhenItemExist(){
        Item oldItem = new Item();
        oldItem.setId(1);
        oldItem.setTitle("Old Title");
        oldItem.setDescription("Old Description");

        when(itemRepository.findById(1)).thenReturn(Optional.of(oldItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));

        ItemRequest updateRequest = new ItemRequest(
                "New Title",
                "New Description"
        );

        ItemResponse itemResponse = itemService.updateItemById(updateRequest, 1);

        assertThat(itemResponse.getTitle()).isEqualTo("New Title");
        assertThat(itemResponse.getDescription()).isEqualTo("New Description");
        assertThat(itemResponse.getId()).isEqualTo(1);
        assertThat(itemResponse.getCreatedAt()).isNotNull();
        assertThat(itemResponse.getUpdatedAt()).isNotNull();

        verify(itemRepository).findById(1);
        verify(itemRepository).save(oldItem);
    }

    @Test
    @DisplayName("updateItem should throw ItemNotFoundException when item does not exist")
    void  updateItemByIdShouldThrowExceptionWhenItemDoesNotExist(){
        ItemRequest updateRequest = new ItemRequest(
                "New Title",
                "New Description"
        );

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.updateItemById(updateRequest, 999)).isInstanceOf(ItemNotFoundException.class);
        verify(itemRepository).findById(999);
        verify(itemRepository, never()).save(any());
    }

}
