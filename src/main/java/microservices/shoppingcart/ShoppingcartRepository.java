package microservices.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingcartRepository extends JpaRepository<Shoppingcart, Integer> {
}
