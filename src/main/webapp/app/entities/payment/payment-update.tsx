import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBooking } from 'app/shared/model/booking.model';
import { getEntities as getBookings } from 'app/entities/booking/booking.reducer';
import { IPayment } from 'app/shared/model/payment.model';
import { getEntity, updateEntity, createEntity, reset } from './payment.reducer';

export const PaymentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bookings = useAppSelector(state => state.booking.entities);
  const paymentEntity = useAppSelector(state => state.payment.entity);
  const loading = useAppSelector(state => state.payment.loading);
  const updating = useAppSelector(state => state.payment.updating);
  const updateSuccess = useAppSelector(state => state.payment.updateSuccess);

  const handleClose = () => {
    navigate('/payment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBookings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...paymentEntity,
      ...values,
      booking: bookings.find(it => it.bookingId.toString() === values.booking.toString()),
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
          ...paymentEntity,
          timestamp: convertDateTimeFromServer(paymentEntity.timestamp),
          booking: paymentEntity?.booking?.bookingId,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.payment.home.createOrEditLabel" data-cy="PaymentCreateUpdateHeading">
            <Translate contentKey="bmsApp.payment.home.createOrEditLabel">Create or edit a Payment</Translate>
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
                  name="paymentId"
                  required
                  readOnly
                  id="payment-paymentId"
                  label={translate('bmsApp.payment.paymentId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.payment.amount')}
                id="payment-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('bmsApp.payment.timestamp')}
                id="payment-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.payment.couponId')}
                id="payment-couponId"
                name="couponId"
                data-cy="couponId"
                type="text"
                validate={{
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <ValidatedField
                label={translate('bmsApp.payment.paymentMethod')}
                id="payment-paymentMethod"
                name="paymentMethod"
                data-cy="paymentMethod"
                type="text"
                validate={{
                  maxLength: { value: 45, message: translate('entity.validation.maxlength', { max: 45 }) },
                }}
              />
              <ValidatedField
                id="payment-booking"
                name="booking"
                data-cy="booking"
                label={translate('bmsApp.payment.booking')}
                type="select"
                required
              >
                <option value="" key="0" />
                {bookings
                  ? bookings.map(otherEntity => (
                      <option value={otherEntity.bookingId} key={otherEntity.bookingId}>
                        {otherEntity.bookingId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment" replace color="info">
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

export default PaymentUpdate;
