export interface ICountry {
  countryId?: number;
  countryCode?: string;
  countryName?: string;
}

export const defaultValue: Readonly<ICountry> = {};
