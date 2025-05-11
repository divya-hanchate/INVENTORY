package com.example.IMS.service;


import com.example.IMS.dto.InventoryItemDTO;
import com.example.IMS.entity.InventoryItem;
import com.example.IMS.exception.ItemNotFoundException;
import com.example.IMS.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // Supply items to the inventory
    @Transactional
    public String supply(InventoryItemDTO itemDTO) {
        // Try to find the item by its SKU ID
        Optional<InventoryItem> itemOptional = inventoryRepository.findBySkuId(itemDTO.getSkuId());

        // If the item is not found, create a new item and add it to the database
        InventoryItem item = itemOptional.orElseGet(() -> {
            // Create a new InventoryItem if not found
            InventoryItem newItem = new InventoryItem();
            newItem.setSkuId(itemDTO.getSkuId());
            newItem.setItemName(itemDTO.getItemName());  // Assuming itemDTO has itemName
            newItem.setQuantity(itemDTO.getQuantity());

            // Save the new item to the database
            inventoryRepository.save(newItem);
            return newItem;
        });

        // If item already exists, just update the quantity
        if (item.getSkuId().equals(itemDTO.getSkuId())) {
            item.setQuantity(item.getQuantity() + itemDTO.getQuantity());
            inventoryRepository.save(item);
        }

        return "Supplied " + itemDTO.getQuantity() + " units of item '" + item.getItemName() + "'. New stock: " + item.getQuantity();
    }


    // Block a specific quantity of item for a customer
    @Transactional
    public String blockItem(InventoryItemDTO itemDTO) {
        Optional<InventoryItem> itemOptional = inventoryRepository.findBySkuId(itemDTO.getSkuId());
        InventoryItem item = itemOptional.orElseThrow(() -> new ItemNotFoundException("Item not found"));

        if (item.getQuantity() >= itemDTO.getQuantity()) {
            item.setQuantity(item.getQuantity() - itemDTO.getQuantity());  // Block the specified quantity
            inventoryRepository.save(item);
            return "Blocked " + itemDTO.getQuantity() + " units of item '" + item.getItemName() + "' for the customer.";
        } else {
            return "Not enough stock. Only " + item.getQuantity() + " units available.";
        }
    }

    // Cancel blocked items (restore stock)
    @Transactional
    public String cancelBlock(InventoryItemDTO itemDTO) {
        Optional<InventoryItem> itemOptional = inventoryRepository.findBySkuId(itemDTO.getSkuId());
        InventoryItem item = itemOptional.orElseThrow(() -> new ItemNotFoundException("Item not found"));

        item.setQuantity(item.getQuantity() + itemDTO.getQuantity());  // Restore the blocked quantity
        inventoryRepository.save(item);

        return "Cancelled block for " + itemDTO.getQuantity() + " units of item '" + item.getItemName() + "'. New stock: " + item.getQuantity();
    }

    // Check stock availability of an item
    public String checkAvailableStock(String skuId) {
        Optional<InventoryItem> itemOptional = inventoryRepository.findBySkuId(skuId);
        if (itemOptional.isPresent()) {
            return "Available stock for item '" + itemOptional.get().getItemName() + "': " + itemOptional.get().getQuantity();
        }
        return "Item with SKU ID '" + skuId + "' not found in inventory.";
    }
}

