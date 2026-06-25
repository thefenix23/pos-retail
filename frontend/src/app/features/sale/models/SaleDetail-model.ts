import { SaleDetailItem } from './SaleDetailItem-model';

export interface SaleDetail {
  id: number;
  status: string;
  total: number;
  paymentMethodId: number;
  createdAt: string;
  items: SaleDetailItem[]
}
