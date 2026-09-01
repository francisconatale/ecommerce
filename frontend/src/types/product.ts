export interface Product {
  id: string;
  name: string;
  image: string;
  price: number;
  originalPrice?: number;
  currency?: string;
  isOnSale?: boolean;
}
