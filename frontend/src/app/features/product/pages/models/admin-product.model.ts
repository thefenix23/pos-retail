// Modelo para la administracion de productos (CRUD)
// Es más completo que el product del POS porque aquí editamos todos los campos.
export interface AdminProduct {
  id: number;
  sku: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  active: boolean;
  categoryId: number | null;
}

// Lo que el formulario envía al crear o editar (sin id, el backend lo asigna al crear).
export interface ProductFormData {
  sku: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  active: boolean;
  categoryId: number | null;
}
