import dayjs from 'dayjs';
import { IBooking } from 'app/shared/model/booking.model';

export interface IPayment {
  paymentId?: number;
  amount?: number;
  timestamp?: string;
  couponId?: string | null;
  paymentMethod?: string | null;
  booking?: IBooking;
}

export const defaultValue: Readonly<IPayment> = {};
