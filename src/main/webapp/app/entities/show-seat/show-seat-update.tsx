import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISeat } from 'app/shared/model/seat.model';
import { getEntities as getSeats } from 'app/entities/seat/seat.reducer';
import { IShow } from 'app/shared/model/show.model';
import { getEntities as getShows } from 'app/entities/show/show.reducer';
import { IBooking } from 'app/shared/model/booking.model';
import { getEntities as getBookings } from 'app/entities/booking/booking.reducer';
import { IShowSeat } from 'app/shared/model/show-seat.model';
import { SeatStatus } from 'app/shared/model/enumerations/seat-status.model';
import { getEntity, updateEntity, createEntity, reset } from './show-seat.reducer';

export const ShowSeatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const seats = useAppSelector(state => state.seat.entities);
  const shows = useAppSelector(state => state.show.entities);
  const bookings = useAppSelector(state => state.booking.entities);
  const showSeatEntity = useAppSelector(state => state.showSeat.entity);
  const loading = useAppSelector(state => state.showSeat.loading);
  const updating = useAppSelector(state => state.showSeat.updating);
  const updateSuccess = useAppSelector(state => state.showSeat.updateSuccess);
  const seatStatusValues = Object.keys(SeatStatus);

  const handleClose = () => {
    navigate('/show-seat' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSeats({}));
    dispatch(getShows({}));
    dispatch(getBookings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...showSeatEntity,
      ...values,
      seat: seats.find(it => it.seatId.toString() === values.seat.toString()),
      show: shows.find(it => it.showId.toString() === values.show.toString()),
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
      ? {}
      : {
          status: 'AVAIALBLE',
          ...showSeatEntity,
          seat: showSeatEntity?.seat?.seatId,
          show: showSeatEntity?.show?.showId,
          booking: showSeatEntity?.booking?.bookingId,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bmsApp.showSeat.home.createOrEditLabel" data-cy="ShowSeatCreateUpdateHeading">
            <Translate contentKey="bmsApp.showSeat.home.createOrEditLabel">Create or edit a ShowSeat</Translate>
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
                  name="showSeatId"
                  required
                  readOnly
                  id="show-seat-showSeatId"
                  label={translate('bmsApp.showSeat.showSeatId')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bmsApp.showSeat.price')}
                id="show-seat-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('bmsApp.showSeat.status')}
                id="show-seat-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {seatStatusValues.map(seatStatus => (
                  <option value={seatStatus} key={seatStatus}>
                    {translate('bmsApp.SeatStatus.' + seatStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="show-seat-seat"
                name="seat"
                data-cy="seat"
                label={translate('bmsApp.showSeat.seat')}
                type="select"
                required
              >
                <option value="" key="0" />
                {seats
                  ? seats.map(otherEntity => (
                      <option value={otherEntity.seatId} key={otherEntity.seatId}>
                        {otherEntity.seatId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="show-seat-show"
                name="show"
                data-cy="show"
                label={translate('bmsApp.showSeat.show')}
                type="select"
                required
              >
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
              <ValidatedField
                id="show-seat-booking"
                name="booking"
                data-cy="booking"
                label={translate('bmsApp.showSeat.booking')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/show-seat" replace color="info">
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

export default ShowSeatUpdate;
