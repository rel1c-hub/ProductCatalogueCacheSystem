package notimpressed.devs.productcataloguecachesystem;

import notimpressed.devs.productcataloguecachesystem.dto.ProductRequestDto;
import notimpressed.devs.productcataloguecachesystem.dto.ProductResponseDto;
import notimpressed.devs.productcataloguecachesystem.exception.ProductNotFoundException;
import notimpressed.devs.productcataloguecachesystem.model.Product;
import notimpressed.devs.productcataloguecachesystem.repository.ProductRepository;
import notimpressed.devs.productcataloguecachesystem.service.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    void getProductById_existingProduct_returnsProductDto() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDto foundDto = productService.getProductById(1L);

        assertNotNull(foundDto);
        assertEquals(1L, foundDto.id());
        assertEquals("Test Product", foundDto.name());

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
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponseDto dto = new ProductResponseDto(
                1L,
                "Test Product",
                "Test Description",
                BigDecimal.TEN,
                "TestCategory",
                10
        );

        Page<ProductResponseDto> dtoPage = new PageImpl<>(List.of(dto), pageable, 1);

        when(productService.getProductsByCategory("TestCategory", pageable))
                .thenReturn(dtoPage);

        Page<ProductResponseDto> result = productService.getProductsByCategory("TestCategory", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).name());

        verify(productRepository, times(1)).findByCategory("TestCategory", pageable);
    }

    @Test
    void updateProduct_existingProduct_updatesAndSaves() {
        ProductRequestDto updatedDto = new ProductRequestDto(
                "Updated Product",
                "Updated description",
                new BigDecimal("20.0"),
                "UpdatedCategory",
                10
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDto result = productService.updateProduct(1L, updatedDto);

        assertEquals("Updated Product", result.name());
        assertEquals("UpdatedCategory", result.category());
        assertEquals(new BigDecimal("20.0"), result.price());
        assertEquals(10, result.stock());

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
        ProductRequestDto newDto = new ProductRequestDto(
                "New Product",
                "Some description",
                new BigDecimal("30.0"),
                "NewCategory",
                15
        );

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDto result = productService.createProduct(newDto);

        assertNotNull(result);
        assertEquals("New Product", result.name());

        verify(productRepository, times(1)).save(any(Product.class));
    }
}