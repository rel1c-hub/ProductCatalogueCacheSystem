package notimpressed.devs.productcataloguecachesystem.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Size(max = 255, message = "Description can't be longer than 255 characters")
        String description,

        @NotNull(message = "Price can't be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotBlank(message = "Category cannot be blank")
        String category,

        @NotNull(message = "Stock can't be null")
        @Min(value = 0, message = "Stock can't be negative")
        Integer stock
) {}
