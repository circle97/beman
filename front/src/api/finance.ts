import request from '@/utils/request';

export interface BillRequest {
  description: string;
  amount: number;
  type: 'expense' | 'income';
  date: string;
  category: string;
  isShared: boolean;
}

export interface FinanceSummary {
  totalIncome: number;
  totalExpense: number;
  sharedExpense: number;
  personalExpense: number;
  savingRate: number;
  categories: Array<{name: string, amount: number, percentage: number}>;
}

export const getFinanceSummary = () => {
  return request<FinanceSummary>({
    url: '/finance/summary',
    method: 'get'
  });
};

export const addBill = (data: BillRequest) => {
  return request({
    url: '/finance/bill',
    method: 'post',
    data
  });
};

export const getBills = (period: string) => {
  return request<Array<any>>(
    url: '/finance/bills',
    method: 'get',
    params: { period }
  );
};