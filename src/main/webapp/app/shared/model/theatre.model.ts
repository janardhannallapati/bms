export interface ITheatre {
  theatreId?: number;
  theatreName?: string | null;
  noOfScreens?: number;
}

export const defaultValue: Readonly<ITheatre> = {};
