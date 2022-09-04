import { ISeat } from 'app/shared/model/seat.model';
import { IShow } from 'app/shared/model/show.model';
import { IBooking } from 'app/shared/model/booking.model';
import { SeatStatus } from 'app/shared/model/enumerations/seat-status.model';

export interface IShowSeat {
  showSeatId?: number;
  price?: number;
  status?: SeatStatus;
  seat?: ISeat;
  show?: IShow;
  booking?: IBooking;
}

export const defaultValue: Readonly<IShowSeat> = {};
