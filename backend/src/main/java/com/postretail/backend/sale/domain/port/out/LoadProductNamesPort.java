package com.postretail.backend.sale.domain.port.out;

import java.util.List;
import java.util.Map;

public interface LoadProductNamesPort {
    // Devuelve un mapa -> nombre, para enriquecer el detalle de la venta
    Map<Long, String> loadNamesByIds(List<Long> productIds);
}
