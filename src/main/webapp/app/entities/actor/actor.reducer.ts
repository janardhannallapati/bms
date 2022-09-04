import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IActor, defaultValue } from 'app/shared/model/actor.model';

const initialState: EntityState<IActor> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/actors';

// Actions

export const getEntities = createAsyncThunk('actor/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IActor[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'actor/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IActor>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'actor/create_entity',
  async (entity: IActor, thunkAPI) => {
    const result = await axios.post<IActor>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'actor/update_entity',
  async (entity: IActor, thunkAPI) => {
    const result = await axios.put<IActor>(`${apiUrl}/${entity.actorId}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'actor/partial_update_entity',
  async (entity: IActor, thunkAPI) => {
    const result = await axios.patch<IActor>(`${apiUrl}/${entity.actorId}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'actor/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IActor>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ActorSlice = createEntitySlice({
  name: 'actor',
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

export const { reset } = ActorSlice.actions;

// Reducer
export default ActorSlice.reducer;
