import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Theatre from './theatre';
import TheatreDetail from './theatre-detail';
import TheatreUpdate from './theatre-update';
import TheatreDeleteDialog from './theatre-delete-dialog';

const TheatreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Theatre />} />
    <Route path="new" element={<TheatreUpdate />} />
    <Route path=":id">
      <Route index element={<TheatreDetail />} />
      <Route path="edit" element={<TheatreUpdate />} />
      <Route path="delete" element={<TheatreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TheatreRoutes;
