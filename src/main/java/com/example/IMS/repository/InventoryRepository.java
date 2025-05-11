package com.example.IMS.repository;



import com.example.IMS.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySkuId(String skuId);  // Fetch item by SKU ID
}
