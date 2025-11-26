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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("New task");
        assertThat(saved.getDescription()).isEqualTo("New description");

    }


}
