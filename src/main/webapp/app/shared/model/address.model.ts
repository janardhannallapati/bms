import { ICity } from 'app/shared/model/city.model';

export interface IAddress {
  addressId?: number;
  address?: string;
  address2?: string | null;
  district?: string;
  postalCode?: string;
  phone?: string;
  location?: string;
  city?: ICity;
}

export const defaultValue: Readonly<IAddress> = {};
