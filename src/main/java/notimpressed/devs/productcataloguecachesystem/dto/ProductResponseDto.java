package notimpressed.devs.productcataloguecachesystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        Integer stock
) {}
