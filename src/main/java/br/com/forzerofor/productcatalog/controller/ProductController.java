package br.com.forzerofor.productcatalog.controller;

import br.com.forzerofor.productcatalog.dto.ProductDto;
import br.com.forzerofor.productcatalog.model.Product;
import br.com.forzerofor.productcatalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<Product> save(@RequestBody ProductDto productDto) {
        if (productDto == null || productDto.name() == null || productDto.name().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }



}
