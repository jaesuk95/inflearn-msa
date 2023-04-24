package com.example.catalogservice.impl;

import com.example.catalogservice.model.CatalogueEntity;
import com.example.catalogservice.model.CatalogueRepository;
import com.example.catalogservice.model.CatalogueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogueServiceImpl implements CatalogueService {

    private final CatalogueRepository catalogueRepository;
    private final Environment environment;

    @Override
    public Iterable<CatalogueEntity> getAllCatalogues() {
        return catalogueRepository.findAll();
    }
}
