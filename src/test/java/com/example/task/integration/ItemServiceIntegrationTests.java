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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class ItemServiceIntegrationTests {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("todo")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }


    @Test
    void addItemShouldPersistAndReturnResponse() {
        ItemRequest itemRequest = new ItemRequest(
                "New task",
                "New description"
        );

        ItemResponse itemResponse = itemService.addItem(itemRequest);

        assertThat(itemResponse.getId()).isNotNull();
        assertThat(itemResponse.getTitle()).isEqualTo("New task");
        assertThat(itemResponse.getDescription()).isEqualTo("New description");

        Item saved = itemRepository.findById(itemResponse.getId());
        assertThat(saved.getTitle()).isEqualTo(itemResponse.getTitle());
        assertThat(saved.getDescription()).isEqualTo(itemResponse.getDescription());

    }

    @Test
    void findAllShouldReturnAllItems() {
        ItemRequest itemRequest1 = new ItemRequest(
                "Task 1",
                "Description 1"
        );

        ItemRequest itemRequest2 = new ItemRequest(
                "Task2",
                "Description 2"
        );

        itemService.addItem(itemRequest1);
        itemService.addItem(itemRequest2);

        List<ItemResponse> items = itemService.findAll();

        assertThat(items.size()).isEqualTo(2);
        assertThat(items).extracting(ItemResponse::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void findByIdShouldReturnResponse() {
        ItemRequest itemRequest = new ItemRequest(
                "Task",
                "Description"
        );

        ItemResponse saved = itemService.addItem(itemRequest);

        ItemResponse response = itemService.getItemById(saved.getId());
    }







}
