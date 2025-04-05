package com.yipintsoi.inventoryservice.service.impl;

import com.yipintsoi.inventoryservice.domain.dto.ProductDTO;
import com.yipintsoi.inventoryservice.domain.entity.Product;
import com.yipintsoi.inventoryservice.domain.mapper.ProductMapper;
import com.yipintsoi.inventoryservice.exception.CustomException;
import com.yipintsoi.inventoryservice.repository.ProductRepository;
import com.yipintsoi.inventoryservice.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new CustomException("Product not found with id: " + id));
        return productMapper.productToProductDTO(product);
    }
    
    // ใช้ circuit breaker กับการสร้างสินค้า
    @Override
    @CircuitBreaker(name = "inventoryServiceCircuitBreaker", fallbackMethod = "createProductFallback")
    public ProductDTO createProduct(ProductDTO productDTO) {
        // จำลองการเรียก external service (เช่น supplier check) ก่อนสร้างสินค้า
        externalInventoryCheck();
        
        Product product = productMapper.productDTOToProduct(productDTO);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Product saved = productRepository.save(product);
        return productMapper.productToProductDTO(saved);
    }
    
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existing = productRepository.findById(id)
                        .orElseThrow(() -> new CustomException("Product not found with id: " + id));
        existing.setName(productDTO.getName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setQuantity(productDTO.getQuantity());
        existing.setUpdatedAt(LocalDateTime.now());
        Product updated = productRepository.save(existing);
        return productMapper.productToProductDTO(updated);
    }
    
    @Override
    public void deleteProduct(Long id) {
        Product existing = productRepository.findById(id)
                        .orElseThrow(() -> new CustomException("Product not found with id: " + id));
        productRepository.delete(existing);
    }
    
    // จำลอง external call ที่อาจล้มเหลวด้วยโอกาส 30%
    private void externalInventoryCheck() {
        if (Math.random() < 0.3) {
            throw new RuntimeException("Simulated external inventory check failure");
        }
    }
    
    // fallback method สำหรับ createProduct เมื่อ externalInventoryCheck ล้มเหลว
    private ProductDTO createProductFallback(ProductDTO productDTO, Throwable t) {
        throw new CustomException("External inventory check failed: " + t.getMessage());
    }
}
