package com.example.task.integration;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
import com.example.task.models.Item;
import com.example.task.repositories.ItemRepository;
import com.example.task.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }


    @Test
    void createItemShouldReturnNewItem() throws Exception {
        ItemRequest itemRequest = new ItemRequest("Task1", "Example of description");
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ItemResponse itemResponse = objectMapper.readValue(json, ItemResponse.class);
                    assertThat(itemResponse.getId()).isNotNull();
                    assertThat(itemResponse.getTitle()).isEqualTo(itemRequest.getTitle());
                    assertThat(itemResponse.getDescription()).isEqualTo(itemRequest.getDescription());
                    assertThat(itemResponse.getCreatedAt()).isNotNull();
                    assertThat(itemResponse.getUpdatedAt()).isNotNull();
                });
    }


    @Test
    void getAllItemsShouldReturnAllItems() throws Exception {
        ItemRequest item1 = new ItemRequest("Task1", "Description 1");
        ItemRequest item2 = new ItemRequest("Task2", "Description 2");
        itemService.addItem(item1);
        itemService.addItem(item2);

        mockMvc.perform(get("/api/items")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ItemResponse[] itemResponses = objectMapper.readValue(json, ItemResponse[].class);
                    assertThat(itemResponses.length).isEqualTo(2);
                    assertThat(itemResponses).extracting(ItemResponse::getTitle).containsExactlyInAnyOrder(
                            "Task1", "Task2"
                    );
                });
    }

    @Test
    void getItemByIdShouldReturnItem() throws Exception {
        ItemRequest item = new ItemRequest("Task1", "Description 1");
        ItemResponse saved = itemService.addItem(item);

        mockMvc.perform(get("/api/items/{id}", saved.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ItemResponse itemResponse = objectMapper.readValue(json, ItemResponse.class);
                    assertThat(itemResponse.getId()).isEqualTo(saved.getId());
                    assertThat(itemResponse.getTitle()).isEqualTo(saved.getTitle());
                    assertThat(itemResponse.getDescription()).isEqualTo(saved.getDescription());
                    assertThat(itemResponse.getCreatedAt()).isNotNull();
                    assertThat(itemResponse.getUpdatedAt()).isNotNull();
                });
    }


    @Test
    void getItemByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/items/{id}", 9999))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    String message = objectMapper.readTree(json).get("message").asText();
                    assertThat(message).isEqualTo("Item not found");
                });
    }

    @Test
    void deleteItemByIdShouldReturnNoContentWhenItemExists() throws Exception {
        ItemRequest item = new ItemRequest("Task1", "Description 1");
        ItemResponse saved = itemService.addItem(item);
        assertThat(itemRepository.existsById(saved.getId())).isTrue();
        mockMvc.perform(delete("/api/items/{id}", saved.getId()))
                .andExpect(status().isNoContent());
        assertThat(itemRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    void deleteItemByIdShouldReturnNotFoundWhenItemDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/items/{id}", 11111))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateItemShouldReturnUpdatedItemWhenItemExists() throws Exception {
        ItemRequest item = new ItemRequest("Task1", "Description 1");
        ItemResponse saved = itemService.addItem(item);
        assertThat(itemRepository.existsById(saved.getId())).isTrue();

        ItemRequest updateItem = new ItemRequest("New Task", "New Description");
        mockMvc.perform(patch("/api/items/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateItem)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    ItemResponse itemResponse = objectMapper.readValue(json, ItemResponse.class);
                    assertThat(itemResponse.getId()).isEqualTo(saved.getId());
                    assertThat(itemResponse.getTitle()).isEqualTo(updateItem.getTitle());
                    assertThat(itemResponse.getDescription()).isEqualTo(updateItem.getDescription());
                    assertThat(itemResponse.getCreatedAt()).isNotNull();
                    assertThat(itemResponse.getUpdatedAt()).isAfter(saved.getUpdatedAt());
                });

        Item updatedItem = itemRepository.findById(saved.getId());
        assertThat(updatedItem.getTitle()).isEqualTo(updateItem.getTitle());
        assertThat(updatedItem.getDescription()).isEqualTo(updateItem.getDescription());
    }


    @Test
    void updateItemShouldReturnNotFoundWhenItemDoesNotExist() throws Exception {
        ItemRequest item = new ItemRequest("Task1", "Description 1");
        mockMvc.perform(patch("/api/items/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isNotFound());
    }


}
