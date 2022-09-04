import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './country.reducer';

export const CountryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const countryEntity = useAppSelector(state => state.country.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="countryDetailsHeading">
          <Translate contentKey="bmsApp.country.detail.title">Country</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="countryId">
              <Translate contentKey="bmsApp.country.countryId">Country Id</Translate>
            </span>
          </dt>
          <dd>{countryEntity.countryId}</dd>
          <dt>
            <span id="countryCode">
              <Translate contentKey="bmsApp.country.countryCode">Country Code</Translate>
            </span>
          </dt>
          <dd>{countryEntity.countryCode}</dd>
          <dt>
            <span id="countryName">
              <Translate contentKey="bmsApp.country.countryName">Country Name</Translate>
            </span>
          </dt>
          <dd>{countryEntity.countryName}</dd>
        </dl>
        <Button tag={Link} to="/country" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/country/${countryEntity.countryId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CountryDetail;
