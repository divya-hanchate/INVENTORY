package com.example.IMS.controller;

import com.example.IMS.dto.InventoryItemDTO;
import com.example.IMS.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    // Supply items to inventory
    @PostMapping("/supply")
    public String supply(@RequestBody InventoryItemDTO itemDTO) {
        return service.supply(itemDTO);
    }

    // Block a specific quantity of item for a customer
    @PostMapping("/block")
    public String blockItem(@RequestBody InventoryItemDTO itemDTO) {
        return service.blockItem(itemDTO);
    }

    // Cancel blocked items (restore stock)
    @DeleteMapping("/block")
    public String cancelBlock( @RequestBody InventoryItemDTO itemDTO) {
        return service.cancelBlock(itemDTO);
    }

    // Check available stock of an item
    @GetMapping("/stock")
    public String getStock(@RequestParam String skuId) {
        return service.checkAvailableStock(skuId);
    }
}
