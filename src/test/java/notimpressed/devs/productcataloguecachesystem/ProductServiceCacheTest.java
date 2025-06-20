package notimpressed.devs.productcataloguecachesystem;


import notimpressed.devs.productcataloguecachesystem.model.Product;
import notimpressed.devs.productcataloguecachesystem.repository.ProductRepository;
import notimpressed.devs.productcataloguecachesystem.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableCaching
@SpringBootTest
class ProductServiceCacheTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setName("Cached Product");
        product.setPrice(new BigDecimal("50.0"));
        product.setCategory("CachedCategory");
        product.setStock(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    }

    @Test
    void updateProduct_shouldEvictCache() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        productService.getProductById(1L);

        Product updated = new Product();
        updated.setName("Updated Name");
        updated.setPrice(BigDecimal.TEN);
        updated.setCategory("Updated Category");
        updated.setStock(5);
        productService.updateProduct(1L, updated);
        productService.getProductById(1L);
        verify(productRepository, times(2)).findById(1L);
    }
}
