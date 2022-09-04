import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMovie } from 'app/shared/model/movie.model';
import { getEntities as getMovies } from 'app/entities/movie/movie.reducer';
import { IScreen } from 'app/shared/model/screen.model';
import { getEntities as getScreens } from 'app/entities/screen/screen.reducer';
import { ISeat } from 'app/shared/model/seat.model';
import { getEntities as getSeats } from 'app/entities/seat/seat.reducer';
import { IShow } from 'app/shared/model/show.model';
import { getEntity, updateEntity, createEntity, reset } from './show.reducer';

export const ShowUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const movies = useAppSelector(state => state.movie.entities);
  const screens = useAppSelector(state => state.screen.entities);
  const seats = useAppSelector(state => state.seat.entities);
  const showEntity = useAppSelector(state => state.show.entity);
  const loading = useAppSelector(state => state.show.loading);
  const updating = useAppSelector(state => state.show.updating);
  const updateSuccess = useAppSelector(state => state.show.updateSuccess);

  const handleClose = () => {
    navigate('/show' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMovies({}));
    dispatch(getScreens({}));
    dispatch(getSeats({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    const entity = {
      ...showEntity,
      ...values,
      seats: mapIdList(values.seats),
      movie: movies.find(it => it.movieId.toString() === values.movie.toString()),
      screen: screens.find(it => it.screenId.toString() === values.screen.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          ...showEntity,
          startTime: convertDateTimeFromServer(showEntity.startTime),
          endTime: convertDateTimeFromServer(showEntity.endTime),
          movie: showEntity?.movie?.movieId,
          screen: showEntity?.screen?.screenId,
          seats: showEntity?.seats?.map(e => e.seatId.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.show.home.createOrEditLabel" data-cy="ShowCreateUpdateHeading">
            <Translate contentKey="bmsApp.show.home.createOrEditLabel">Create or edit a Show</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="showId"
                  required
                  readOnly
                  id="show-showId"
                  label={translate('bmsApp.show.showId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.show.date')}
                id="show-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.show.startTime')}
                id="show-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.show.endTime')}
                id="show-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField id="show-movie" name="movie" data-cy="movie" label={translate('bmsApp.show.movie')} type="select" required>
                <option value="" key="0" />
                {movies
                  ? movies.map(otherEntity => (
                      <option value={otherEntity.movieId} key={otherEntity.movieId}>
                        {otherEntity.movieId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="show-screen"
                name="screen"
                data-cy="screen"
                label={translate('bmsApp.show.screen')}
                type="select"
                required
              >
                <option value="" key="0" />
                {screens
                  ? screens.map(otherEntity => (
                      <option value={otherEntity.screenId} key={otherEntity.screenId}>
                        {otherEntity.screenId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField label={translate('bmsApp.show.seat')} id="show-seat" data-cy="seat" type="select" multiple name="seats">
                <option value="" key="0" />
                {seats
                  ? seats.map(otherEntity => (
                      <option value={otherEntity.seatId} key={otherEntity.seatId}>
                        {otherEntity.seatId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/show" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ShowUpdate;
