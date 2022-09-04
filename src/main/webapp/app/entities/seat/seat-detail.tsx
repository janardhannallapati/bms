import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seat.reducer';

export const SeatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seatEntity = useAppSelector(state => state.seat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seatDetailsHeading">
          <Translate contentKey="bmsApp.seat.detail.title">Seat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="seatId">
              <Translate contentKey="bmsApp.seat.seatId">Seat Id</Translate>
            </span>
          </dt>
          <dd>{seatEntity.seatId}</dd>
          <dt>
            <span id="seatNumber">
              <Translate contentKey="bmsApp.seat.seatNumber">Seat Number</Translate>
            </span>
          </dt>
          <dd>{seatEntity.seatNumber}</dd>
          <dt>
            <span id="seatDescr">
              <Translate contentKey="bmsApp.seat.seatDescr">Seat Descr</Translate>
            </span>
          </dt>
          <dd>{seatEntity.seatDescr}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="bmsApp.seat.type">Type</Translate>
            </span>
          </dt>
          <dd>{seatEntity.type}</dd>
          <dt>
            <Translate contentKey="bmsApp.seat.screen">Screen</Translate>
          </dt>
          <dd>{seatEntity.screen ? seatEntity.screen.screenId : ''}</dd>
        </dl>
        <Button tag={Link} to="/seat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seat/${seatEntity.seatId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeatDetail;
