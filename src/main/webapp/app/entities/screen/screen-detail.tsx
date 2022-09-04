import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './screen.reducer';

export const ScreenDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const screenEntity = useAppSelector(state => state.screen.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="screenDetailsHeading">
          <Translate contentKey="bmsApp.screen.detail.title">Screen</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="screenId">
              <Translate contentKey="bmsApp.screen.screenId">Screen Id</Translate>
            </span>
          </dt>
          <dd>{screenEntity.screenId}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bmsApp.screen.name">Name</Translate>
            </span>
          </dt>
          <dd>{screenEntity.name}</dd>
          <dt>
            <span id="totalSeats">
              <Translate contentKey="bmsApp.screen.totalSeats">Total Seats</Translate>
            </span>
          </dt>
          <dd>{screenEntity.totalSeats}</dd>
          <dt>
            <Translate contentKey="bmsApp.screen.theatre">Theatre</Translate>
          </dt>
          <dd>{screenEntity.theatre ? screenEntity.theatre.theatreId : ''}</dd>
        </dl>
        <Button tag={Link} to="/screen" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/screen/${screenEntity.screenId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ScreenDetail;
