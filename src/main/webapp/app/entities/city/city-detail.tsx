import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './city.reducer';

export const CityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cityEntity = useAppSelector(state => state.city.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cityDetailsHeading">
          <Translate contentKey="bmsApp.city.detail.title">City</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="cityId">
              <Translate contentKey="bmsApp.city.cityId">City Id</Translate>
            </span>
          </dt>
          <dd>{cityEntity.cityId}</dd>
          <dt>
            <span id="cityName">
              <Translate contentKey="bmsApp.city.cityName">City Name</Translate>
            </span>
          </dt>
          <dd>{cityEntity.cityName}</dd>
          <dt>
            <Translate contentKey="bmsApp.city.country">Country</Translate>
          </dt>
          <dd>{cityEntity.country ? cityEntity.country.countryId : ''}</dd>
        </dl>
        <Button tag={Link} to="/city" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/city/${cityEntity.cityId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CityDetail;
