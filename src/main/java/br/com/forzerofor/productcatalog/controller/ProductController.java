package br.com.forzerofor.productcatalog.controller;

import br.com.forzerofor.productcatalog.dto.ProductDto;
import br.com.forzerofor.productcatalog.model.Product;
import br.com.forzerofor.productcatalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<Product> save(@RequestBody @Valid ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.listAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findById(@PathVariable("productId") Long productId) {
        if (productService.getProductById(productId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(productId));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> update(@PathVariable("productId") Long productId,
                                          @RequestBody @Valid ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, productDto));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Product> delete(@PathVariable("productId") Long productId) {
        if (productService.getProductById(productId) == null) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}