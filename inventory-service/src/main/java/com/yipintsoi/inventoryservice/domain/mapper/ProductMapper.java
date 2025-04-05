package com.yipintsoi.inventoryservice.domain.mapper;

import com.yipintsoi.inventoryservice.domain.dto.ProductDTO;
import com.yipintsoi.inventoryservice.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);
}
