export interface SaleSummaryModel {
  id: number;
  status: string;
  total: number;
  paymentMethodId: number;
  itemCount: number;
  createdAt: string;
}
