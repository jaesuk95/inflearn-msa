package com.example.catalogservice.model;

import org.springframework.data.repository.CrudRepository;

public interface CatalogueRepository extends CrudRepository<CatalogueEntity, Long> {
    CatalogueEntity findByProductId(String productId);
}
