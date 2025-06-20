package notimpressed.devs.productcataloguecachesystem.mapper;

import notimpressed.devs.productcataloguecachesystem.dto.ProductRequestDto;
import notimpressed.devs.productcataloguecachesystem.dto.ProductResponseDto;
import notimpressed.devs.productcataloguecachesystem.model.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(dto.category());
        product.setStock(dto.stock());
        return product;
    }

    public static ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCategory(),
            product.getStock()
        );
    }
}
