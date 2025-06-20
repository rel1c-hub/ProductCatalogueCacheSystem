package notimpressed.devs.productcataloguecachesystem.repository;

import notimpressed.devs.productcataloguecachesystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    Page<Product> findByCategory(String category, Pageable pageable);
}
