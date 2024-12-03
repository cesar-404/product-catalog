package br.com.forzerofor.productcatalog.controller;

import br.com.forzerofor.productcatalog.dto.ProductDto;
import br.com.forzerofor.productcatalog.model.Product;
import br.com.forzerofor.productcatalog.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @TestConfiguration
    static class ProductControllerTestConfig {

        @Bean
        public ProductService productService() {
            ProductService productService = Mockito.mock(ProductService.class);
            Mockito.when(productService.getProductById(1L))
                    .thenReturn(new Product(1L, "Tv", "Big Tv", new BigDecimal(1000), 3));
            Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any(ProductDto.class)))
                    .thenReturn(new Product(1L, "Tv2", "Big Tv2", new BigDecimal(2000), 1));
            Mockito.when(productService.createProduct(Mockito.any(ProductDto.class)))
                    .thenReturn(new Product(1L, "Tv", "Big Tv", new BigDecimal(1000), 3));
            return productService;
        }
    }

    @Autowired
    WebTestClient webClient;

    private ProductDto productDto;
    private ProductDto productDto2;
    private ProductDto updatedProductDto;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto("Tv", "Big Tv", new BigDecimal(1000), 3);
        productDto2 = new ProductDto("", "", new BigDecimal(0), 0);
        updatedProductDto = new ProductDto("Tv2", "Big Tv2", new BigDecimal(2000), 1);
    }

    @Test
    void saveProductSuccess() {
        webClient
                .post()
                .uri("/product/save")
                .bodyValue(productDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Tv")
                .jsonPath("$.description").isEqualTo("Big Tv")
                .jsonPath("$.price").isEqualTo(1000)
                .jsonPath("$.stock").isEqualTo(3);
    }

    @Test
    void saveProductFail() {
        webClient
                .post()
                .uri("/product/save")
                .bodyValue(productDto2)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void listProductsSuccess() {
        webClient
                .get()
                .uri("/product/list")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class);
    }

    @Test
    void findProductByIdSuccess() {
        webClient
                .get()
                .uri("/product/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Tv")
                .jsonPath("$.description").isEqualTo("Big Tv")
                .jsonPath("$.price").isEqualTo(1000)
                .jsonPath("$.stock").isEqualTo(3);
    }

    @Test
    void findProductByIdFail() {
        webClient
                .get()
                .uri("/product/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateProductSuccess() {
        webClient
                .put()
                .uri("/product/update/1")
                .bodyValue(updatedProductDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Tv2")
                .jsonPath("$.description").isEqualTo("Big Tv2")
                .jsonPath("$.price").isEqualTo(2000)
                .jsonPath("$.stock").isEqualTo(1);
    }

    @Test
    void deleteProductSuccess() {
        webClient
                .delete()
                .uri("/product/delete/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(productService, times(1)).deleteProduct(Mockito.anyLong());
    }

    @Test
    void deleteProductFail() {
        webClient
                .delete()
                .uri("/product/delete/2")
                .exchange()
                .expectStatus().isNotFound();

        verify(productService, times(0)).deleteProduct(Mockito.anyLong());
    }
}