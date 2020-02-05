package com.kkukielka.springwebfluxrest.repositories;

import com.kkukielka.springwebfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
