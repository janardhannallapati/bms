import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILanguage } from 'app/shared/model/language.model';
import { getEntities as getLanguages } from 'app/entities/language/language.reducer';
import { IActor } from 'app/shared/model/actor.model';
import { getEntities as getActors } from 'app/entities/actor/actor.reducer';
import { IGenre } from 'app/shared/model/genre.model';
import { getEntities as getGenres } from 'app/entities/genre/genre.reducer';
import { IMovie } from 'app/shared/model/movie.model';
import { Rating } from 'app/shared/model/enumerations/rating.model';
import { getEntity, updateEntity, createEntity, reset } from './movie.reducer';

export const MovieUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const languages = useAppSelector(state => state.language.entities);
  const actors = useAppSelector(state => state.actor.entities);
  const genres = useAppSelector(state => state.genre.entities);
  const movieEntity = useAppSelector(state => state.movie.entity);
  const loading = useAppSelector(state => state.movie.loading);
  const updating = useAppSelector(state => state.movie.updating);
  const updateSuccess = useAppSelector(state => state.movie.updateSuccess);
  const ratingValues = Object.keys(Rating);

  const handleClose = () => {
    navigate('/movie' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLanguages({}));
    dispatch(getActors({}));
    dispatch(getGenres({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...movieEntity,
      ...values,
      actors: mapIdList(values.actors),
      genres: mapIdList(values.genres),
      language: languages.find(it => it.languageId.toString() === values.language.toString()),
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
          rating: 'G',
          ...movieEntity,
          language: movieEntity?.language?.languageId,
          actors: movieEntity?.actors?.map(e => e.actorId.toString()),
          genres: movieEntity?.genres?.map(e => e.genreId.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.movie.home.createOrEditLabel" data-cy="MovieCreateUpdateHeading">
            <Translate contentKey="bmsApp.movie.home.createOrEditLabel">Create or edit a Movie</Translate>
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
                  name="movieId"
                  required
                  readOnly
                  id="movie-movieId"
                  label={translate('bmsApp.movie.movieId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.movie.title')}
                id="movie-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 128, message: translate('entity.validation.maxlength', { max: 128 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.movie.description')}
                id="movie-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.movie.releaseYear')}
                id="movie-releaseYear"
                name="releaseYear"
                data-cy="releaseYear"
                type="text"
                validate={{
                  min: { value: 1870, message: translate('entity.validation.min', { min: 1870 }) },
                  max: { value: 2100, message: translate('entity.validation.max', { max: 2100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('bmsApp.movie.length')} id="movie-length" name="length" data-cy="length" type="text" />
              <ValidatedField label={translate('bmsApp.movie.rating')} id="movie-rating" name="rating" data-cy="rating" type="select">
                {ratingValues.map(rating => (
                  <option value={rating} key={rating}>
                    {translate('bmsApp.Rating.' + rating)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="movie-language"
                name="language"
                data-cy="language"
                label={translate('bmsApp.movie.language')}
                type="select"
                required
              >
                <option value="" key="0" />
                {languages
                  ? languages.map(otherEntity => (
                      <option value={otherEntity.languageId} key={otherEntity.languageId}>
                        {otherEntity.languageId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField label={translate('bmsApp.movie.actor')} id="movie-actor" data-cy="actor" type="select" multiple name="actors">
                <option value="" key="0" />
                {actors
                  ? actors.map(otherEntity => (
                      <option value={otherEntity.actorId} key={otherEntity.actorId}>
                        {otherEntity.actorId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label={translate('bmsApp.movie.genre')} id="movie-genre" data-cy="genre" type="select" multiple name="genres">
                <option value="" key="0" />
                {genres
                  ? genres.map(otherEntity => (
                      <option value={otherEntity.genreId} key={otherEntity.genreId}>
                        {otherEntity.genreId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/movie" replace color="info">
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

export default MovieUpdate;
