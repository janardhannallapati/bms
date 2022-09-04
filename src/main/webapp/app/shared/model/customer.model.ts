import { IAddress } from 'app/shared/model/address.model';

export interface ICustomer {
  customerId?: number;
  firstName?: string;
  lastName?: string;
  email?: string | null;
  address?: IAddress;
}

export const defaultValue: Readonly<ICustomer> = {};
