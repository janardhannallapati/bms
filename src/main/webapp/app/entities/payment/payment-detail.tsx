import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="bmsApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="paymentId">
              <Translate contentKey="bmsApp.payment.paymentId">Payment Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentId}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="bmsApp.payment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.amount}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="bmsApp.payment.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.timestamp ? <TextFormat value={paymentEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="couponId">
              <Translate contentKey="bmsApp.payment.couponId">Coupon Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.couponId}</dd>
          <dt>
            <span id="paymentMethod">
              <Translate contentKey="bmsApp.payment.paymentMethod">Payment Method</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentMethod}</dd>
          <dt>
            <Translate contentKey="bmsApp.payment.booking">Booking</Translate>
          </dt>
          <dd>{paymentEntity.booking ? paymentEntity.booking.bookingId : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.paymentId}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
