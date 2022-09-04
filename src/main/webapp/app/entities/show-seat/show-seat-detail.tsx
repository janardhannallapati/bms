import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './show-seat.reducer';

export const ShowSeatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const showSeatEntity = useAppSelector(state => state.showSeat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="showSeatDetailsHeading">
          <Translate contentKey="bmsApp.showSeat.detail.title">ShowSeat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="showSeatId">
              <Translate contentKey="bmsApp.showSeat.showSeatId">Show Seat Id</Translate>
            </span>
          </dt>
          <dd>{showSeatEntity.showSeatId}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="bmsApp.showSeat.price">Price</Translate>
            </span>
          </dt>
          <dd>{showSeatEntity.price}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="bmsApp.showSeat.status">Status</Translate>
            </span>
          </dt>
          <dd>{showSeatEntity.status}</dd>
          <dt>
            <Translate contentKey="bmsApp.showSeat.seat">Seat</Translate>
          </dt>
          <dd>{showSeatEntity.seat ? showSeatEntity.seat.seatId : ''}</dd>
          <dt>
            <Translate contentKey="bmsApp.showSeat.show">Show</Translate>
          </dt>
          <dd>{showSeatEntity.show ? showSeatEntity.show.showId : ''}</dd>
          <dt>
            <Translate contentKey="bmsApp.showSeat.booking">Booking</Translate>
          </dt>
          <dd>{showSeatEntity.booking ? showSeatEntity.booking.bookingId : ''}</dd>
        </dl>
        <Button tag={Link} to="/show-seat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/show-seat/${showSeatEntity.showSeatId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShowSeatDetail;
