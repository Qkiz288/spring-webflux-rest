package com.kkukielka.springwebfluxrest.controllers;

import com.kkukielka.springwebfluxrest.domain.Category;
import com.kkukielka.springwebfluxrest.domain.Vendor;
import com.kkukielka.springwebfluxrest.repositories.VendorRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class VendorControllerTest {

    private VendorController vendorController;
    private VendorRepository vendorRepository;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void listVendors() {
        // given
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("fname1").lastName("lname1").build(),
                        Vendor.builder().firstName("fname2").lastName("lname2").build()));

        // when - then
        webTestClient.get().uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        // given
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("fname1").lastName("lname1").build()));

        // when - then
        webTestClient.get().uri(VendorController.BASE_URL + "/1")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void createVendor() {
        // given
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("test")
            .lastName("test2").build());

        // when - then
        webTestClient.post().uri(VendorController.BASE_URL)
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateVendor() {
        // given
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstName("test1")
            .lastName("test2").build());

        // when - then
        webTestClient.put().uri(VendorController.BASE_URL + "/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);
    }

    @Test
    void patchVendorWithChangesFirstName() {
        // given
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("fname").lastName("lname").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstName("new fname").build());

        // when - then
        webTestClient.patch().uri(VendorController.BASE_URL + "/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    void patchVendorWithChangesLastName() {
        // given
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("fname").lastName("lname").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().lastName("new lname").build());

        // when - then
        webTestClient.patch().uri(VendorController.BASE_URL + "/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    void patchVendorWithNullChanges() {
        // given
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("fname").lastName("lname").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().build());

        // when - then
        webTestClient.patch().uri(VendorController.BASE_URL + "/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }

    @Test
    void patchVendorNoChanges() {
        // given
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("fname").lastName("lname").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstName("fname").lastName("lname").build());

        // when - then
        webTestClient.patch().uri(VendorController.BASE_URL + "/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }

    @Test
    void patchVendorNoChangesNulls() {
        // given
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().build());

        // when - then
        webTestClient.patch().uri(VendorController.BASE_URL + "/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }

}