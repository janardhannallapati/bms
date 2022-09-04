import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Genre from './genre';
import GenreDetail from './genre-detail';
import GenreUpdate from './genre-update';
import GenreDeleteDialog from './genre-delete-dialog';

const GenreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Genre />} />
    <Route path="new" element={<GenreUpdate />} />
    <Route path=":id">
      <Route index element={<GenreDetail />} />
      <Route path="edit" element={<GenreUpdate />} />
      <Route path="delete" element={<GenreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GenreRoutes;
