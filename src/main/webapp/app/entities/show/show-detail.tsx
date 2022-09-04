import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './show.reducer';

export const ShowDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const showEntity = useAppSelector(state => state.show.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="showDetailsHeading">
          <Translate contentKey="bmsApp.show.detail.title">Show</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="showId">
              <Translate contentKey="bmsApp.show.showId">Show Id</Translate>
            </span>
          </dt>
          <dd>{showEntity.showId}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="bmsApp.show.date">Date</Translate>
            </span>
          </dt>
          <dd>{showEntity.date ? <TextFormat value={showEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="bmsApp.show.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{showEntity.startTime ? <TextFormat value={showEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="bmsApp.show.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{showEntity.endTime ? <TextFormat value={showEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="bmsApp.show.movie">Movie</Translate>
          </dt>
          <dd>{showEntity.movie ? showEntity.movie.movieId : ''}</dd>
          <dt>
            <Translate contentKey="bmsApp.show.screen">Screen</Translate>
          </dt>
          <dd>{showEntity.screen ? showEntity.screen.screenId : ''}</dd>
          <dt>
            <Translate contentKey="bmsApp.show.seat">Seat</Translate>
          </dt>
          <dd>
            {showEntity.seats
              ? showEntity.seats.map((val, i) => (
                  <span key={val.seatId}>
                    <a>{val.seatId}</a>
                    {showEntity.seats && i === showEntity.seats.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/show" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/show/${showEntity.showId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShowDetail;
