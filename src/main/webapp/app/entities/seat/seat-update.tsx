import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IScreen } from 'app/shared/model/screen.model';
import { getEntities as getScreens } from 'app/entities/screen/screen.reducer';
import { IShow } from 'app/shared/model/show.model';
import { getEntities as getShows } from 'app/entities/show/show.reducer';
import { ISeat } from 'app/shared/model/seat.model';
import { getEntity, updateEntity, createEntity, reset } from './seat.reducer';

export const SeatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const screens = useAppSelector(state => state.screen.entities);
  const shows = useAppSelector(state => state.show.entities);
  const seatEntity = useAppSelector(state => state.seat.entity);
  const loading = useAppSelector(state => state.seat.loading);
  const updating = useAppSelector(state => state.seat.updating);
  const updateSuccess = useAppSelector(state => state.seat.updateSuccess);

  const handleClose = () => {
    navigate('/seat' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getScreens({}));
    dispatch(getShows({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...seatEntity,
      ...values,
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
      ? {}
      : {
          ...seatEntity,
          screen: seatEntity?.screen?.screenId,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.seat.home.createOrEditLabel" data-cy="SeatCreateUpdateHeading">
            <Translate contentKey="bmsApp.seat.home.createOrEditLabel">Create or edit a Seat</Translate>
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
                  name="seatId"
                  required
                  readOnly
                  id="seat-seatId"
                  label={translate('bmsApp.seat.seatId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.seat.seatNumber')}
                id="seat-seatNumber"
                name="seatNumber"
                data-cy="seatNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('bmsApp.seat.seatDescr')}
                id="seat-seatDescr"
                name="seatDescr"
                data-cy="seatDescr"
                type="text"
                validate={{
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.seat.type')}
                id="seat-type"
                name="type"
                data-cy="type"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                id="seat-screen"
                name="screen"
                data-cy="screen"
                label={translate('bmsApp.seat.screen')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/seat" replace color="info">
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

export default SeatUpdate;
