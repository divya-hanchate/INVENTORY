package com.example.IMS.dto;






import lombok.Data;

@Data
public class InventoryItemDTO {

    private String skuId;  // SKU ID of the item

    private String itemName;  // Name of the item

    private int quantity;  // Quantity for blocking, supplying, or canceling
}
