package notimpressed.devs.productcataloguecachesystem.service;

import notimpressed.devs.productcataloguecachesystem.dto.ProductRequestDto;
import notimpressed.devs.productcataloguecachesystem.dto.ProductResponseDto;
import notimpressed.devs.productcataloguecachesystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    ProductResponseDto getProductById(Long id);
    Page<ProductResponseDto> getProductsByCategory(String category, Pageable pageable);
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);
}
