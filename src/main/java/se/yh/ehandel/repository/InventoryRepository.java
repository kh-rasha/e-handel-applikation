package se.yh.ehandel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yh.ehandel.domain.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
