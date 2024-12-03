    package br.com.forzerofor.productcatalog.service;

    import br.com.forzerofor.productcatalog.dto.ProductDto;
    import br.com.forzerofor.productcatalog.model.Product;
    import br.com.forzerofor.productcatalog.repository.ProductRepository;
    import org.springframework.beans.BeanUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class ProductService {

        private final ProductRepository productRepository;

        @Autowired
        public ProductService(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        public Product createProduct(ProductDto productDto) {
            var productSaved = new Product();
            BeanUtils.copyProperties(productDto, productSaved);
            return productRepository.save(productSaved);
        }

        public List<Product> listAllProducts() {
            return productRepository.findAll();
        }

        public Product getProductById(Long id) {
            return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        }

        public Product updateProduct(Long id, ProductDto productDto) {
            var productSaved = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            BeanUtils.copyProperties(productDto, productSaved);
            return productRepository.save(productSaved);
        }

        public void deleteProduct(Long id) {
            var product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            productRepository.deleteById(product.getId());
        }

    }
