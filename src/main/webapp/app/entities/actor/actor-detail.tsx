import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './actor.reducer';

export const ActorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const actorEntity = useAppSelector(state => state.actor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="actorDetailsHeading">
          <Translate contentKey="bmsApp.actor.detail.title">Actor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="actorId">
              <Translate contentKey="bmsApp.actor.actorId">Actor Id</Translate>
            </span>
          </dt>
          <dd>{actorEntity.actorId}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="bmsApp.actor.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{actorEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="bmsApp.actor.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{actorEntity.lastName}</dd>
        </dl>
        <Button tag={Link} to="/actor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/actor/${actorEntity.actorId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActorDetail;
