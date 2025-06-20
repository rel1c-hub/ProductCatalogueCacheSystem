package notimpressed.devs.productcataloguecachesystem.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notimpressed.devs.productcataloguecachesystem.dto.ProductRequestDto;
import notimpressed.devs.productcataloguecachesystem.dto.ProductResponseDto;
import notimpressed.devs.productcataloguecachesystem.mapper.ProductMapper;
import notimpressed.devs.productcataloguecachesystem.repository.ProductRepository;
import notimpressed.devs.productcataloguecachesystem.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RateLimiter(name = "productApiLimiter")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponseDto> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponseDto> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsByCategory(category, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        return productService.createProduct(productRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        return productService.updateProduct(id, productRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
