import { ITheatre } from 'app/shared/model/theatre.model';

export interface IScreen {
  screenId?: number;
  name?: string;
  totalSeats?: number;
  theatre?: ITheatre;
}

export const defaultValue: Readonly<IScreen> = {};
