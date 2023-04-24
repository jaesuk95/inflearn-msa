package com.example.catalogservice.controller;

import com.example.catalogservice.controller.response.ResponseCatalogue;
import com.example.catalogservice.model.CatalogueEntity;
import com.example.catalogservice.model.CatalogueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-service")
public class CatalogueController {

    private final Environment environment;
    private final CatalogueService catalogueService;

    @GetMapping("/home")
    public String home() {
        return String.format("welcome to catalogue service %s", environment.getProperty("local.server.port"));
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("Catalogue service on PORT : %s", environment.getProperty("local.server.port"));
    }

    @GetMapping("/catalogues")
    public ResponseEntity<List<ResponseCatalogue>> getCatalogues() {
        Iterable<CatalogueEntity> orderList = catalogueService.getAllCatalogues();

        List<ResponseCatalogue> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCatalogue.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
