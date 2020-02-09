package com.kkukielka.springwebfluxrest.controllers;

import com.kkukielka.springwebfluxrest.domain.Vendor;
import com.kkukielka.springwebfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> listVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundVendor = vendorRepository.findById(id).block();
        boolean vendorChanged = false;

        if (foundVendor == null) {
            throw new RuntimeException(String.format("Vendor with ID = %s not found", id));
        }

        if (vendor == null) {
            throw new RuntimeException("Request vendor is null");
        }

        if (foundVendor.getFirstName() == null) {
            foundVendor.setFirstName("");
        }

        if (foundVendor.getLastName() == null) {
            foundVendor.setLastName("");
        }

        if (vendor.getFirstName() != null &&
                !foundVendor.getFirstName().equals(vendor.getFirstName())) {
            vendorChanged = true;
            foundVendor.setFirstName(vendor.getFirstName());
        }

        if (vendor.getLastName() != null &&
                !foundVendor.getLastName().equals(vendor.getLastName())) {
            vendorChanged = true;
            foundVendor.setLastName(vendor.getLastName());
        }

        return vendorChanged ? vendorRepository.save(foundVendor) : Mono.just(foundVendor);

    }

}
