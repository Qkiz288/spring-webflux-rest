package com.kkukielka.springwebfluxrest.bootstrap;

import com.kkukielka.springwebfluxrest.domain.Category;
import com.kkukielka.springwebfluxrest.domain.Vendor;
import com.kkukielka.springwebfluxrest.repositories.CategoryRepository;
import com.kkukielka.springwebfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private VendorRepository vendorRepository;
    private CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository,
                     CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeVendors();
        initializeCategories();
    }

    private void initializeVendors() {
        vendorRepository.deleteAll().block();

        Vendor vendor1 = new Vendor();
        vendor1.setFirstName("John");
        vendor1.setLastName("Johnson");

        Vendor vendor2 = new Vendor();
        vendor2.setFirstName("Mike");
        vendor2.setLastName("Mikeson");

        Vendor vendor3 = new Vendor();
        vendor3.setFirstName("Paul");
        vendor3.setLastName("Paulson");

        vendorRepository.save(vendor1).block();
        vendorRepository.save(vendor2).block();
        vendorRepository.save(vendor3).block();

        log.debug("Saved Vendors count: " + vendorRepository.findAll().count().block());

    }

    private void initializeCategories() {
        categoryRepository.deleteAll().block();

        categoryRepository.save(Category.builder().description("Fruits").build()).block();
        categoryRepository.save(Category.builder().description("Vegetables").build()).block();
        categoryRepository.save(Category.builder().description("Meats").build()).block();

        log.debug("Saved Categories count: " + categoryRepository.findAll().count().block());
    }

}
