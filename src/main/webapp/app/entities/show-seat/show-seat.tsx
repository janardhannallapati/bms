import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShowSeat } from 'app/shared/model/show-seat.model';
import { getEntities } from './show-seat.reducer';

export const ShowSeat = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'showSeatId'), location.search)
  );

  const showSeatList = useAppSelector(state => state.showSeat.entities);
  const loading = useAppSelector(state => state.showSeat.loading);
  const totalItems = useAppSelector(state => state.showSeat.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="show-seat-heading" data-cy="ShowSeatHeading">
        <Translate contentKey="bmsApp.showSeat.home.title">Show Seats</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bmsApp.showSeat.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/show-seat/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bmsApp.showSeat.home.createLabel">Create new Show Seat</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {showSeatList && showSeatList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('showSeatId')}>
                  <Translate contentKey="bmsApp.showSeat.showSeatId">Show Seat Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="bmsApp.showSeat.price">Price</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="bmsApp.showSeat.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bmsApp.showSeat.seat">Seat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bmsApp.showSeat.show">Show</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bmsApp.showSeat.booking">Booking</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {showSeatList.map((showSeat, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/show-seat/${showSeat.showSeatId}`} color="link" size="sm">
                      {showSeat.showSeatId}
                    </Button>
                  </td>
                  <td>{showSeat.price}</td>
                  <td>
                    <Translate contentKey={`bmsApp.SeatStatus.${showSeat.status}`} />
                  </td>
                  <td>{showSeat.seat ? <Link to={`/seat/${showSeat.seat.seatId}`}>{showSeat.seat.seatId}</Link> : ''}</td>
                  <td>{showSeat.show ? <Link to={`/show/${showSeat.show.showId}`}>{showSeat.show.showId}</Link> : ''}</td>
                  <td>{showSeat.booking ? <Link to={`/booking/${showSeat.booking.bookingId}`}>{showSeat.booking.bookingId}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/show-seat/${showSeat.showSeatId}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/show-seat/${showSeat.showSeatId}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/show-seat/${showSeat.showSeatId}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="bmsApp.showSeat.home.notFound">No Show Seats found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={showSeatList && showSeatList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ShowSeat;
