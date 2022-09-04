import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShow } from 'app/shared/model/show.model';
import { getEntities } from './show.reducer';

export const Show = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'showId'), location.search)
  );

  const showList = useAppSelector(state => state.show.entities);
  const loading = useAppSelector(state => state.show.loading);
  const totalItems = useAppSelector(state => state.show.totalItems);

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
      <h2 id="show-heading" data-cy="ShowHeading">
        <Translate contentKey="bmsApp.show.home.title">Shows</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bmsApp.show.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/show/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bmsApp.show.home.createLabel">Create new Show</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {showList && showList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('showId')}>
                  <Translate contentKey="bmsApp.show.showId">Show Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="bmsApp.show.date">Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="bmsApp.show.startTime">Start Time</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="bmsApp.show.endTime">End Time</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bmsApp.show.movie">Movie</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bmsApp.show.screen">Screen</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {showList.map((show, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/show/${show.showId}`} color="link" size="sm">
                      {show.showId}
                    </Button>
                  </td>
                  <td>{show.date ? <TextFormat type="date" value={show.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{show.startTime ? <TextFormat type="date" value={show.startTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{show.endTime ? <TextFormat type="date" value={show.endTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{show.movie ? <Link to={`/movie/${show.movie.movieId}`}>{show.movie.movieId}</Link> : ''}</td>
                  <td>{show.screen ? <Link to={`/screen/${show.screen.screenId}`}>{show.screen.screenId}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/show/${show.showId}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/show/${show.showId}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/show/${show.showId}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="bmsApp.show.home.notFound">No Shows found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={showList && showList.length > 0 ? '' : 'd-none'}>
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

export default Show;
