package se.yh.ehandel.service;

public interface InventoryService {
    int getStock(Long productId);
    void setStock(Long productId, int quantity);
    void adjustStock(Long productId, int delta);
}
