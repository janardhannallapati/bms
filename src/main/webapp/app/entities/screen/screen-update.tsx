import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITheatre } from 'app/shared/model/theatre.model';
import { getEntities as getTheatres } from 'app/entities/theatre/theatre.reducer';
import { IScreen } from 'app/shared/model/screen.model';
import { getEntity, updateEntity, createEntity, reset } from './screen.reducer';

export const ScreenUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const theatres = useAppSelector(state => state.theatre.entities);
  const screenEntity = useAppSelector(state => state.screen.entity);
  const loading = useAppSelector(state => state.screen.loading);
  const updating = useAppSelector(state => state.screen.updating);
  const updateSuccess = useAppSelector(state => state.screen.updateSuccess);

  const handleClose = () => {
    navigate('/screen' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTheatres({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...screenEntity,
      ...values,
      theatre: theatres.find(it => it.theatreId.toString() === values.theatre.toString()),
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
          ...screenEntity,
          theatre: screenEntity?.theatre?.theatreId,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.screen.home.createOrEditLabel" data-cy="ScreenCreateUpdateHeading">
            <Translate contentKey="bmsApp.screen.home.createOrEditLabel">Create or edit a Screen</Translate>
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
                  name="screenId"
                  required
                  readOnly
                  id="screen-screenId"
                  label={translate('bmsApp.screen.screenId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.screen.name')}
                id="screen-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.screen.totalSeats')}
                id="screen-totalSeats"
                name="totalSeats"
                data-cy="totalSeats"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="screen-theatre"
                name="theatre"
                data-cy="theatre"
                label={translate('bmsApp.screen.theatre')}
                type="select"
                required
              >
                <option value="" key="0" />
                {theatres
                  ? theatres.map(otherEntity => (
                      <option value={otherEntity.theatreId} key={otherEntity.theatreId}>
                        {otherEntity.theatreId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/screen" replace color="info">
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

export default ScreenUpdate;
