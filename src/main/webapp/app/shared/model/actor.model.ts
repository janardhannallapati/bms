import { IMovie } from 'app/shared/model/movie.model';

export interface IActor {
  actorId?: number;
  firstName?: string;
  lastName?: string;
  movies?: IMovie[] | null;
}

export const defaultValue: Readonly<IActor> = {};
