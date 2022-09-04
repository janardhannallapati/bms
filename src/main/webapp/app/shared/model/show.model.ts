import dayjs from 'dayjs';
import { IMovie } from 'app/shared/model/movie.model';
import { IScreen } from 'app/shared/model/screen.model';
import { ISeat } from 'app/shared/model/seat.model';

export interface IShow {
  showId?: number;
  date?: string;
  startTime?: string;
  endTime?: string;
  movie?: IMovie;
  screen?: IScreen;
  seats?: ISeat[];
}

export const defaultValue: Readonly<IShow> = {};
