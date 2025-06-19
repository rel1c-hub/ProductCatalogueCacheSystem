package notimpressed.devs.productcataloguecachesystem.model;

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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Size(max = 255, message = "Description can't be longer than 256 characters.")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price can't be null. Please enter the price.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Category can't be blank. Please enter the category.")
    private String category;

    @NotNull(message = "Stock can't be null. Please enter the stock value.")
    @Min(value = 0, message = "Stock can't be negative")
    private Integer stock;

    private LocalDateTime createdDate;

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
