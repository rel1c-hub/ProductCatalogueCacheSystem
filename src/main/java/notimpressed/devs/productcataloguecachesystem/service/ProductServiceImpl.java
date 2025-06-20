package notimpressed.devs.productcataloguecachesystem.service;

import lombok.RequiredArgsConstructor;
import notimpressed.devs.productcataloguecachesystem.dto.ProductRequestDto;
import notimpressed.devs.productcataloguecachesystem.dto.ProductResponseDto;
import notimpressed.devs.productcataloguecachesystem.exception.ProductNotFoundException;
import notimpressed.devs.productcataloguecachesystem.mapper.ProductMapper;
import notimpressed.devs.productcataloguecachesystem.model.Product;
import notimpressed.devs.productcataloguecachesystem.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toDto);
    }

    @Cacheable(value = "product", key = "#id")
    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        ProductResponseDto dto = ProductMapper.toDto(product);
        return dto;
    }

    @Cacheable(value = "productsByCategory", key = "#category + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable)
                .map(ProductMapper::toDto);
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = ProductMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return ProductMapper.toDto(saved);
    }

    @Caching(evict = {
            @CacheEvict(value = "product", key = "#id"),
            @CacheEvict(value = "productsByCategory", allEntries = true)
    })
    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setCategory(dto.category());
        existing.setStock(dto.stock());

        Product updated = productRepository.save(existing);
        return ProductMapper.toDto(updated);
    }

    @Caching(evict = {
            @CacheEvict(value = "product", key = "#id"),
            @CacheEvict(value = "productsByCategory", allEntries = true)
    })
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
