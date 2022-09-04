import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITheatre } from 'app/shared/model/theatre.model';
import { getEntity, updateEntity, createEntity, reset } from './theatre.reducer';

export const TheatreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const theatreEntity = useAppSelector(state => state.theatre.entity);
  const loading = useAppSelector(state => state.theatre.loading);
  const updating = useAppSelector(state => state.theatre.updating);
  const updateSuccess = useAppSelector(state => state.theatre.updateSuccess);

  const handleClose = () => {
    navigate('/theatre' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...theatreEntity,
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
          ...theatreEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.theatre.home.createOrEditLabel" data-cy="TheatreCreateUpdateHeading">
            <Translate contentKey="bmsApp.theatre.home.createOrEditLabel">Create or edit a Theatre</Translate>
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
                  name="theatreId"
                  required
                  readOnly
                  id="theatre-theatreId"
                  label={translate('bmsApp.theatre.theatreId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.theatre.theatreName')}
                id="theatre-theatreName"
                name="theatreName"
                data-cy="theatreName"
                type="text"
                validate={{
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.theatre.noOfScreens')}
                id="theatre-noOfScreens"
                name="noOfScreens"
                data-cy="noOfScreens"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/theatre" replace color="info">
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

export default TheatreUpdate;
