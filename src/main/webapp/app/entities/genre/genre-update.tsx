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
import { IGenre } from 'app/shared/model/genre.model';
import { getEntity, updateEntity, createEntity, reset } from './genre.reducer';

export const GenreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const movies = useAppSelector(state => state.movie.entities);
  const genreEntity = useAppSelector(state => state.genre.entity);
  const loading = useAppSelector(state => state.genre.loading);
  const updating = useAppSelector(state => state.genre.updating);
  const updateSuccess = useAppSelector(state => state.genre.updateSuccess);

  const handleClose = () => {
    navigate('/genre' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMovies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...genreEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...genreEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.genre.home.createOrEditLabel" data-cy="GenreCreateUpdateHeading">
            <Translate contentKey="bmsApp.genre.home.createOrEditLabel">Create or edit a Genre</Translate>
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
                  name="genreId"
                  required
                  readOnly
                  id="genre-genreId"
                  label={translate('bmsApp.genre.genreId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.genre.name')}
                id="genre-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 25, message: translate('entity.validation.maxlength', { max: 25 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/genre" replace color="info">
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

export default GenreUpdate;
