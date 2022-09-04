import { ILanguage } from 'app/shared/model/language.model';
import { IActor } from 'app/shared/model/actor.model';
import { IGenre } from 'app/shared/model/genre.model';
import { Rating } from 'app/shared/model/enumerations/rating.model';

export interface IMovie {
  movieId?: number;
  title?: string;
  description?: string | null;
  releaseYear?: number | null;
  length?: number | null;
  rating?: Rating;
  language?: ILanguage;
  actors?: IActor[];
  genres?: IGenre[];
}

export const defaultValue: Readonly<IMovie> = {};
