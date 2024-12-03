package br.com.forzerofor.productcatalog.service;

import br.com.forzerofor.productcatalog.dto.ProductDto;
import br.com.forzerofor.productcatalog.model.Product;
import br.com.forzerofor.productcatalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto("Keyboard",
                "Small Keyboard",
                new BigDecimal(10000),
                30);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        var product = new Product();
        BeanUtils.copyProperties(productDto, product);
        assertAll("The proprieties should match",
                () -> assertEquals(product.getName(), productDto.name()),
                () -> assertEquals(product.getDescription(), productDto.description()),
                () -> assertEquals(product.getPrice(), productDto.price()),
                () -> assertEquals(product.getStock(), productDto.stock()));

        doReturn(product).when(productRepository).save(any(Product.class));

        Product result = productService.createProduct(productDto);

        assertNotNull(result);
        assertInstanceOf(Product.class, result);

        assertAll("The proprieties should match",
                () -> assertEquals(result.getName(), productDto.name()),
                () -> assertEquals(result.getDescription(), productDto.description()),
                () -> assertEquals(result.getPrice(), productDto.price()),
                () -> assertEquals(result.getStock(), productDto.stock()));

        verify(productRepository, timeout(1)).save(any(Product.class));
    }

    @Test
    void shouldCreateProductFailed() {

    }

}