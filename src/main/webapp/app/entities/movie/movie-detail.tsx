import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './movie.reducer';

export const MovieDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const movieEntity = useAppSelector(state => state.movie.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="movieDetailsHeading">
          <Translate contentKey="bmsApp.movie.detail.title">Movie</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="movieId">
              <Translate contentKey="bmsApp.movie.movieId">Movie Id</Translate>
            </span>
          </dt>
          <dd>{movieEntity.movieId}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="bmsApp.movie.title">Title</Translate>
            </span>
          </dt>
          <dd>{movieEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="bmsApp.movie.description">Description</Translate>
            </span>
          </dt>
          <dd>{movieEntity.description}</dd>
          <dt>
            <span id="releaseYear">
              <Translate contentKey="bmsApp.movie.releaseYear">Release Year</Translate>
            </span>
          </dt>
          <dd>{movieEntity.releaseYear}</dd>
          <dt>
            <span id="length">
              <Translate contentKey="bmsApp.movie.length">Length</Translate>
            </span>
          </dt>
          <dd>{movieEntity.length}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="bmsApp.movie.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{movieEntity.rating}</dd>
          <dt>
            <Translate contentKey="bmsApp.movie.language">Language</Translate>
          </dt>
          <dd>{movieEntity.language ? movieEntity.language.languageId : ''}</dd>
          <dt>
            <Translate contentKey="bmsApp.movie.actor">Actor</Translate>
          </dt>
          <dd>
            {movieEntity.actors
              ? movieEntity.actors.map((val, i) => (
                  <span key={val.actorId}>
                    <a>{val.actorId}</a>
                    {movieEntity.actors && i === movieEntity.actors.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="bmsApp.movie.genre">Genre</Translate>
          </dt>
          <dd>
            {movieEntity.genres
              ? movieEntity.genres.map((val, i) => (
                  <span key={val.genreId}>
                    <a>{val.genreId}</a>
                    {movieEntity.genres && i === movieEntity.genres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/movie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/movie/${movieEntity.movieId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MovieDetail;
