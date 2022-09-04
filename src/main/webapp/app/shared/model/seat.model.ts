import { IScreen } from 'app/shared/model/screen.model';
import { IShow } from 'app/shared/model/show.model';

export interface ISeat {
  seatId?: number;
  seatNumber?: number;
  seatDescr?: string | null;
  type?: string;
  screen?: IScreen;
  shows?: IShow[] | null;
}

export const defaultValue: Readonly<ISeat> = {};
