package notimpressed.devs.productcataloguecachesystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Setter
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Product ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Product name", example = "Apple iPhone 15")
    private String name;

    @Size(max = 255, message = "Description can't be longer than 256 characters.")
    @Schema(description = "Product description", example = "Latest model with improved camera")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price can't be null. Please enter the price.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;

    @NotBlank(message = "Category can't be blank. Please enter the category.")
    @Schema(description = "Product category", example = "Electronics")
    private String category;

    @NotNull(message = "Stock can't be null. Please enter the stock value.")
    @Min(value = 0, message = "Stock can't be negative")
    @Schema(description = "Available stock", example = "25")
    private Integer stock;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Schema(description = "Date the product was created", example = "2025-06-18 15:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Schema(description = "Date the product was last updated", example = "2025-06-18 15:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastUpdatedDate;

    @PrePersist
    public void onCreate() {
        createdDate = lastUpdatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        lastUpdatedDate = LocalDateTime.now();
    }
}
