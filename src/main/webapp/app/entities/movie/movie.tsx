import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMovie } from 'app/shared/model/movie.model';
import { getEntities } from './movie.reducer';

export const Movie = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'movieId'), location.search)
  );

  const movieList = useAppSelector(state => state.movie.entities);
  const loading = useAppSelector(state => state.movie.loading);
  const totalItems = useAppSelector(state => state.movie.totalItems);

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
      <h2 id="movie-heading" data-cy="MovieHeading">
        <Translate contentKey="bmsApp.movie.home.title">Movies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bmsApp.movie.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/movie/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bmsApp.movie.home.createLabel">Create new Movie</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {movieList && movieList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('movieId')}>
                  <Translate contentKey="bmsApp.movie.movieId">Movie Id</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="bmsApp.movie.title">Title</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="bmsApp.movie.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('releaseYear')}>
                  <Translate contentKey="bmsApp.movie.releaseYear">Release Year</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('length')}>
                  <Translate contentKey="bmsApp.movie.length">Length</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rating')}>
                  <Translate contentKey="bmsApp.movie.rating">Rating</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bmsApp.movie.language">Language</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {movieList.map((movie, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/movie/${movie.movieId}`} color="link" size="sm">
                      {movie.movieId}
                    </Button>
                  </td>
                  <td>{movie.title}</td>
                  <td>{movie.description}</td>
                  <td>{movie.releaseYear}</td>
                  <td>{movie.length}</td>
                  <td>
                    <Translate contentKey={`bmsApp.Rating.${movie.rating}`} />
                  </td>
                  <td>{movie.language ? <Link to={`/language/${movie.language.languageId}`}>{movie.language.languageId}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/movie/${movie.movieId}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/movie/${movie.movieId}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/movie/${movie.movieId}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="bmsApp.movie.home.notFound">No Movies found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={movieList && movieList.length > 0 ? '' : 'd-none'}>
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

export default Movie;
