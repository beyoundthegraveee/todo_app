package com.example.task.integration;

import com.example.task.dto.ItemRequest;
import com.example.task.dto.ItemResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.id").exists());
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
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getItemByIdShouldReturnItem() throws Exception {
        ItemRequest item = new ItemRequest("Task1", "Description 1");
        ItemResponse saved = itemService.addItem(item);

        mockMvc.perform(get("/api/items/{id}", saved.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }


    @Test
    void getItemByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/items/{id}", 11111))
                .andExpect(status().isNotFound());
    }





}
