package br.com.forzerofor.productcatalog.controller;

import br.com.forzerofor.productcatalog.dto.ProductDto;
import br.com.forzerofor.productcatalog.model.Product;
import br.com.forzerofor.productcatalog.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper mapper = new ObjectMapper();

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto("Tv", "Big Tv", new BigDecimal(1000), 22);
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        String jsonProduct = mapper.writeValueAsString(productDto);

        mockMvc.perform(
                post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProduct)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tv"))
                .andExpect(jsonPath("$.description").value("Big Tv"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.stock").value(22));
    }


}