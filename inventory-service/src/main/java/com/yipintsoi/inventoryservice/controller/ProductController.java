package com.yipintsoi.inventoryservice.controller;

import com.yipintsoi.inventoryservice.domain.dto.ProductDTO;
import com.yipintsoi.inventoryservice.response.ApiResponse;
import com.yipintsoi.inventoryservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    
    public ProductController(ProductService productService) {
         this.productService = productService;
    }
    
    // Endpoint สำหรับดึงรายการสินค้าทั้งหมด
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
         List<ProductDTO> products = productService.getAllProducts();
         ApiResponse<List<ProductDTO>> response = ApiResponse.<List<ProductDTO>>builder()
             .status("success")
             .data(products)
             .message("Fetched products successfully")
             .build();
         return ResponseEntity.ok(response);
    }
    
    // Endpoint สำหรับดึงข้อมูลสินค้าโดย ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
         ProductDTO product = productService.getProductById(id);
         ApiResponse<ProductDTO> response = ApiResponse.<ProductDTO>builder()
             .status("success")
             .data(product)
             .message("Fetched product successfully")
             .build();
         return ResponseEntity.ok(response);
    }
    
    // Endpoint สำหรับสร้างสินค้าใหม่
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) {
         ProductDTO createdProduct = productService.createProduct(productDTO);
         ApiResponse<ProductDTO> response = ApiResponse.<ProductDTO>builder()
             .status("success")
             .data(createdProduct)
             .message("Product created successfully")
             .build();
         return ResponseEntity.ok(response);
    }
    
    // Endpoint สำหรับอัปเดตข้อมูลสินค้า
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
         ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
         ApiResponse<ProductDTO> response = ApiResponse.<ProductDTO>builder()
             .status("success")
             .data(updatedProduct)
             .message("Product updated successfully")
             .build();
         return ResponseEntity.ok(response);
    }
    
    // Endpoint สำหรับลบสินค้า
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
         productService.deleteProduct(id);
         ApiResponse<Void> response = ApiResponse.<Void>builder()
             .status("success")
             .message("Product deleted successfully")
             .build();
         return ResponseEntity.ok(response);
    }
}
