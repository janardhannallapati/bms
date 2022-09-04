import { IMovie } from 'app/shared/model/movie.model';

export interface IGenre {
  genreId?: number;
  name?: string;
  movies?: IMovie[] | null;
}

export const defaultValue: Readonly<IGenre> = {};
