import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ITheatre, defaultValue } from 'app/shared/model/theatre.model';

const initialState: EntityState<ITheatre> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/theatres';

// Actions

export const getEntities = createAsyncThunk('theatre/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<ITheatre[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'theatre/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITheatre>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'theatre/create_entity',
  async (entity: ITheatre, thunkAPI) => {
    const result = await axios.post<ITheatre>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'theatre/update_entity',
  async (entity: ITheatre, thunkAPI) => {
    const result = await axios.put<ITheatre>(`${apiUrl}/${entity.theatreId}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'theatre/partial_update_entity',
  async (entity: ITheatre, thunkAPI) => {
    const result = await axios.patch<ITheatre>(`${apiUrl}/${entity.theatreId}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'theatre/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ITheatre>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const TheatreSlice = createEntitySlice({
  name: 'theatre',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = TheatreSlice.actions;

// Reducer
export default TheatreSlice.reducer;
