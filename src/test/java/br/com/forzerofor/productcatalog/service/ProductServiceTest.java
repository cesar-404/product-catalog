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
import java.util.List;
import java.util.Optional;

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
        productDto = new ProductDto("Keyboard", "Small Keyboard", new BigDecimal(10000),
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

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldReturnAllProductsSuccessfully() {
        var product = new Product();
        List<Product> products = productService.listAllProducts();
        products.add(product);

        assertNotNull(products);
        assertInstanceOf(List.class, products);
        assertEquals(1, products.size());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnProductByIdSuccessfully() {
        Long id = 1L;
        var product = new Product(id, "Keyboard", "Small Keyboard",
                new BigDecimal(10000), 30);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(id);
        assertNotNull(result);
        assertInstanceOf(Product.class, result);
        assertAll("The proprieties should match",
                () -> assertEquals(result.getId(), product.getId()),
                () -> assertEquals(result.getName(), product.getName()),
                () -> assertEquals(result.getDescription(), product.getDescription()),
                () -> assertEquals(result.getPrice(), product.getPrice()),
                () -> assertEquals(result.getStock(), product.getStock()));

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnNullWhenProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(productService.getProductById(1L));

    }

    @Test
    void shouldUpdateProductSuccessfully() {
        var product = new Product(1L, "Keyboard test", "Small Keyboard test",
                new BigDecimal(20000), 20);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(product.getId(), productDto);

        verify(productRepository).save(product);

        assertNotNull(updatedProduct);
        assertInstanceOf(Product.class, updatedProduct);
        assertAll("The proprieties should match",
                () -> assertEquals(updatedProduct.getName(), productDto.name()),
                () -> assertEquals(updatedProduct.getDescription(), productDto.description()),
                () -> assertEquals(updatedProduct.getPrice(), productDto.price()),
                () -> assertEquals(updatedProduct.getStock(), productDto.stock()));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldUpdateProductByIdFailure() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, productDto));
        assertEquals("Product not found", thrown.getMessage());

        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        var id = 1L;
        var product = new Product();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldDeleteProductByIdFailure() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
        assertEquals("Product not found", thrown.getMessage());

        verify(productRepository, times(0)).deleteById(anyLong());
    }
}