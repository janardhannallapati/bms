import { ICountry } from 'app/shared/model/country.model';

export interface ICity {
  cityId?: number;
  cityName?: string;
  country?: ICountry;
}

export const defaultValue: Readonly<ICity> = {};
