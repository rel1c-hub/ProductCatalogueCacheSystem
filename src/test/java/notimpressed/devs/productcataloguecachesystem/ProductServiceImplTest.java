package notimpressed.devs.productcataloguecachesystem;

import notimpressed.devs.productcataloguecachesystem.exception.ProductNotFoundException;
import notimpressed.devs.productcataloguecachesystem.model.Product;
import notimpressed.devs.productcataloguecachesystem.repository.ProductRepository;
import notimpressed.devs.productcataloguecachesystem.service.ProductService;
import notimpressed.devs.productcataloguecachesystem.service.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.0"));
        product.setCategory("TestCategory");
        product.setStock(5);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getProductById_existingProduct_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.getProductById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Test Product", found.getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_notExistingProduct_throwsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(1L)
        );

        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductsByCategory_existingCategory_returnsProducts() {
        List<Product> mockProducts = List.of(product);
        when(productRepository.findByCategory("TestCategory")).thenReturn(mockProducts);

        List<Product> result = productService.getProductsByCategory("TestCategory");

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());

        verify(productRepository, times(1)).findByCategory("TestCategory");
    }

    @Test
    void updateProduct_existingProduct_updatesAndSaves() {
        Product updated = new Product();
        updated.setName("Updated Product");
        updated.setPrice(new BigDecimal("20.0"));
        updated.setCategory("UpdatedCategory");
        updated.setStock(10);

        when(applicationContext.getBean(ProductService.class)).thenReturn(productService);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(1L, updated);

        assertEquals("Updated Product", result.getName());
        assertEquals("UpdatedCategory", result.getCategory());
        assertEquals(new BigDecimal("20.0"), result.getPrice());
        assertEquals(10, result.getStock());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_existingProduct_deletesSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_notExistingProduct_throwsException() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(1L)
        );

        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void createProduct_savesAndReturnsProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(new BigDecimal("30.0"));
        newProduct.setCategory("NewCategory");
        newProduct.setStock(15);

        when(productRepository.save(newProduct)).thenReturn(newProduct);

        Product result = productService.createProduct(newProduct);

        assertNotNull(result);
        assertEquals("New Product", result.getName());

        verify(productRepository, times(1)).save(newProduct);
    }
}