import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { IShow } from 'app/shared/model/show.model';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';

export interface IBooking {
  bookingId?: number;
  timestamp?: string;
  status?: BookingStatus;
  customer?: ICustomer;
  show?: IShow;
}

export const defaultValue: Readonly<IBooking> = {};
