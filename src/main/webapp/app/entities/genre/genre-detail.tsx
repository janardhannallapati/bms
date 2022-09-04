import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './genre.reducer';

export const GenreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const genreEntity = useAppSelector(state => state.genre.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="genreDetailsHeading">
          <Translate contentKey="bmsApp.genre.detail.title">Genre</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="genreId">
              <Translate contentKey="bmsApp.genre.genreId">Genre Id</Translate>
            </span>
          </dt>
          <dd>{genreEntity.genreId}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bmsApp.genre.name">Name</Translate>
            </span>
          </dt>
          <dd>{genreEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/genre" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/genre/${genreEntity.genreId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GenreDetail;
