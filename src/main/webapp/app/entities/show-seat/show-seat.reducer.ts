import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IShowSeat, defaultValue } from 'app/shared/model/show-seat.model';

const initialState: EntityState<IShowSeat> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/show-seats';

// Actions

export const getEntities = createAsyncThunk('showSeat/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IShowSeat[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'showSeat/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IShowSeat>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'showSeat/create_entity',
  async (entity: IShowSeat, thunkAPI) => {
    const result = await axios.post<IShowSeat>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'showSeat/update_entity',
  async (entity: IShowSeat, thunkAPI) => {
    const result = await axios.put<IShowSeat>(`${apiUrl}/${entity.showSeatId}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'showSeat/partial_update_entity',
  async (entity: IShowSeat, thunkAPI) => {
    const result = await axios.patch<IShowSeat>(`${apiUrl}/${entity.showSeatId}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'showSeat/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IShowSeat>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ShowSeatSlice = createEntitySlice({
  name: 'showSeat',
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

export const { reset } = ShowSeatSlice.actions;

// Reducer
export default ShowSeatSlice.reducer;
