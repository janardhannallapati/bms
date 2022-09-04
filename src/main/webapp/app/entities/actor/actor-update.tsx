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
import { IActor } from 'app/shared/model/actor.model';
import { getEntity, updateEntity, createEntity, reset } from './actor.reducer';

export const ActorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const movies = useAppSelector(state => state.movie.entities);
  const actorEntity = useAppSelector(state => state.actor.entity);
  const loading = useAppSelector(state => state.actor.loading);
  const updating = useAppSelector(state => state.actor.updating);
  const updateSuccess = useAppSelector(state => state.actor.updateSuccess);

  const handleClose = () => {
    navigate('/actor' + location.search);
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
      ...actorEntity,
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
          ...actorEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.actor.home.createOrEditLabel" data-cy="ActorCreateUpdateHeading">
            <Translate contentKey="bmsApp.actor.home.createOrEditLabel">Create or edit a Actor</Translate>
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
                  name="actorId"
                  required
                  readOnly
                  id="actor-actorId"
                  label={translate('bmsApp.actor.actorId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.actor.firstName')}
                id="actor-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.actor.lastName')}
                id="actor-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/actor" replace color="info">
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

export default ActorUpdate;
