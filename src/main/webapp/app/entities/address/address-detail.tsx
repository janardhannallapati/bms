import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './address.reducer';

export const AddressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const addressEntity = useAppSelector(state => state.address.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="addressDetailsHeading">
          <Translate contentKey="bmsApp.address.detail.title">Address</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="addressId">
              <Translate contentKey="bmsApp.address.addressId">Address Id</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressId}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="bmsApp.address.address">Address</Translate>
            </span>
          </dt>
          <dd>{addressEntity.address}</dd>
          <dt>
            <span id="address2">
              <Translate contentKey="bmsApp.address.address2">Address 2</Translate>
            </span>
          </dt>
          <dd>{addressEntity.address2}</dd>
          <dt>
            <span id="district">
              <Translate contentKey="bmsApp.address.district">District</Translate>
            </span>
          </dt>
          <dd>{addressEntity.district}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="bmsApp.address.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{addressEntity.postalCode}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="bmsApp.address.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{addressEntity.phone}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="bmsApp.address.location">Location</Translate>
            </span>
          </dt>
          <dd>{addressEntity.location}</dd>
          <dt>
            <Translate contentKey="bmsApp.address.city">City</Translate>
          </dt>
          <dd>{addressEntity.city ? addressEntity.city.cityId : ''}</dd>
        </dl>
        <Button tag={Link} to="/address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/address/${addressEntity.addressId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AddressDetail;
