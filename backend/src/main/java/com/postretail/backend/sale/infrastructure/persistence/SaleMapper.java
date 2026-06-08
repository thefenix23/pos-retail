package com.postretail.backend.sale.infrastructure.persistence;

import com.postretail.backend.sale.domain.model.Sale;
import com.postretail.backend.sale.domain.model.SaleItem;

import java.util.List;

public class SaleMapper {

    private SaleMapper() {}

    public static SaleJpaEntity toJpaEntity(Sale sale) {
        SaleJpaEntity entity = new SaleJpaEntity();
        entity.setStatus(sale.getStatus());
        entity.setTotal(sale.getTotal());
        entity.setPaymentMethodId(sale.getPaymentMethodId());

        for (SaleItem item : sale.getItems()) {
            SaleItemJpaEntity itemEntity = new SaleItemJpaEntity();
            itemEntity.setProductId(item.getProductId());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setUnitPrice(item.getUnitPrice());
            itemEntity.setSubtotal(item.getSubtotal());
            entity.addItem(itemEntity); // mantiene la relación sincronizada
        }

        return entity;
    }

    public static Sale toDomain(SaleJpaEntity entity) {
        List<SaleItem> items = entity
                .getItems()
                .stream()
                .map(i -> new SaleItem(
                        i.getProductId(),
                        i.getQuantity(),
                        i.getUnitPrice()))
                .toList();

        return new Sale(
                entity.getId(),
                items,
                entity.getPaymentMethodId()
        );
    }
}
