package com.bakery.bakery_management.service.Impl;


import com.bakery.bakery_management.domain.dto.Request.ProductRequest;
import com.bakery.bakery_management.domain.dto.Response.ProductResponse;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.ProductMapper;
import com.bakery.bakery_management.repository.ProductRepository;
import com.bakery.bakery_management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        extends AdminBaseServiceImpl<Product, ProductRequest, ProductResponse, Long>
        implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return repository;
    }

    @Override
    protected AdminBaseMapper<ProductRequest, ProductResponse, Product> getMapper() {
        return mapper;
    }

    @Override
    protected Long getId(Product entity) {
        return entity.getId();
    }

    // ===== BUSINESS =====

    @Override
    protected void validateCreate(ProductRequest request) {
        if (repository.existsByCode(request.getCode())) {
            throw new BusinessException("PRODUCT_ALREADY_EXISTS");
        }
    }

    @Override
    protected void validateUpdate(ProductRequest request, Product entity) {
        if (request.getCode() != null &&
                !repository.existsByCode(request.getCode())) {
            throw new BusinessException("PRODUCT_NOT_EXISTS");
        }
    }
}
