import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking.reducer';

export const BookingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingEntity = useAppSelector(state => state.booking.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingDetailsHeading">
          <Translate contentKey="bmsApp.booking.detail.title">Booking</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="bookingId">
              <Translate contentKey="bmsApp.booking.bookingId">Booking Id</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.bookingId}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="bmsApp.booking.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.timestamp ? <TextFormat value={bookingEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="bmsApp.booking.status">Status</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.status}</dd>
          <dt>
            <Translate contentKey="bmsApp.booking.customer">Customer</Translate>
          </dt>
          <dd>{bookingEntity.customer ? bookingEntity.customer.customerId : ''}</dd>
          <dt>
            <Translate contentKey="bmsApp.booking.show">Show</Translate>
          </dt>
          <dd>{bookingEntity.show ? bookingEntity.show.showId : ''}</dd>
        </dl>
        <Button tag={Link} to="/booking" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/booking/${bookingEntity.bookingId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingDetail;
