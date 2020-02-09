package com.kkukielka.springwebfluxrest.controllers;

import com.kkukielka.springwebfluxrest.domain.Category;
import com.kkukielka.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

class CategoryControllerTest {

    private WebTestClient webTestClient;
    private CategoryRepository categoryRepository;
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void listCategories() {
        // given
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("desc1").build(),
                                      Category.builder().description("desc2").build()));
        // when - then
        webTestClient.get().uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById() {
        // given
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("desc1").build()));

        // when - then
        webTestClient.get().uri(CategoryController.BASE_URL + "/1")
                .exchange()
                .expectBody(Category.class);

    }

    @Test
    void createCategory() {
        // given
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryToSave = Mono.just(Category.builder().description("test").build());

        // when - then
        webTestClient.post().uri(CategoryController.BASE_URL)
                .body(categoryToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    void updateCategory() {
        // given
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().description("test").build());

        // when - then
        webTestClient.put().uri(CategoryController.BASE_URL + "/1")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchCategoryWithChanges() {
        // given
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("abc").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().description("new abc").build());

        // when - then
        webTestClient.patch().uri(CategoryController.BASE_URL + "/1")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }

    @Test
    void patchCategoryWithNullChanges() {
        // given
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("abc").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().build());

        // when - then
        webTestClient.patch().uri(CategoryController.BASE_URL + "/1")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }

    @Test
    void patchCategoryNoChanges() {
        // given
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("abc").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().description("abc").build());

        // when - then
        webTestClient.patch().uri(CategoryController.BASE_URL + "/1")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void patchCategoryNoChangesNulls() {
        // given
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().build());

        // when - then
        webTestClient.patch().uri(CategoryController.BASE_URL + "/1")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }

}