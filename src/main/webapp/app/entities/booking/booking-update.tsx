import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IShow } from 'app/shared/model/show.model';
import { getEntities as getShows } from 'app/entities/show/show.reducer';
import { IBooking } from 'app/shared/model/booking.model';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';
import { getEntity, updateEntity, createEntity, reset } from './booking.reducer';

export const BookingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customers = useAppSelector(state => state.customer.entities);
  const shows = useAppSelector(state => state.show.entities);
  const bookingEntity = useAppSelector(state => state.booking.entity);
  const loading = useAppSelector(state => state.booking.loading);
  const updating = useAppSelector(state => state.booking.updating);
  const updateSuccess = useAppSelector(state => state.booking.updateSuccess);
  const bookingStatusValues = Object.keys(BookingStatus);

  const handleClose = () => {
    navigate('/booking' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCustomers({}));
    dispatch(getShows({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...bookingEntity,
      ...values,
      customer: customers.find(it => it.customerId.toString() === values.customer.toString()),
      show: shows.find(it => it.showId.toString() === values.show.toString()),
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
          timestamp: displayDefaultDateTime(),
        }
      : {
          status: 'RESERVED',
          ...bookingEntity,
          timestamp: convertDateTimeFromServer(bookingEntity.timestamp),
          customer: bookingEntity?.customer?.customerId,
          show: bookingEntity?.show?.showId,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.booking.home.createOrEditLabel" data-cy="BookingCreateUpdateHeading">
            <Translate contentKey="bmsApp.booking.home.createOrEditLabel">Create or edit a Booking</Translate>
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
                  name="bookingId"
                  required
                  readOnly
                  id="booking-bookingId"
                  label={translate('bmsApp.booking.bookingId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.booking.timestamp')}
                id="booking-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('bmsApp.booking.status')} id="booking-status" name="status" data-cy="status" type="select">
                {bookingStatusValues.map(bookingStatus => (
                  <option value={bookingStatus} key={bookingStatus}>
                    {translate('bmsApp.BookingStatus.' + bookingStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="booking-customer"
                name="customer"
                data-cy="customer"
                label={translate('bmsApp.booking.customer')}
                type="select"
                required
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.customerId} key={otherEntity.customerId}>
                        {otherEntity.customerId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField id="booking-show" name="show" data-cy="show" label={translate('bmsApp.booking.show')} type="select" required>
                <option value="" key="0" />
                {shows
                  ? shows.map(otherEntity => (
                      <option value={otherEntity.showId} key={otherEntity.showId}>
                        {otherEntity.showId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/booking" replace color="info">
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

export default BookingUpdate;
