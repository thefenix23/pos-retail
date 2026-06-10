// Lo que enviamos al backend para crear una venta
export interface CreateSaleRequest {
  items: SaleItemRequest[];
  paymentMethodId: number;
}

export interface SaleItemRequest {
  productId: number;
  quantity: number;
}

// Lo que recibimos del backend (el ticket)
export interface SaleResponse {
  id: number;
  status: string;
  total: number;
  paymentMethodId: number;
  items: SaleResponseItem[];
}

export interface SaleResponseItem {
  productId: number;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}
