package com.postretail.backend.product.infrastructure.web;

import com.postretail.backend.product.domain.exception.DuplicateSkuException;
import com.postretail.backend.product.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleNotFound(ProductNotFoundException ex) {
        return ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage()
                );
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public ProblemDetail handleDuplicateSku(DuplicateSkuException ex) {
        return ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        ex.getMessage()
                );
    }
}
