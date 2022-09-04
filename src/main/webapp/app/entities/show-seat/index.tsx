import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ShowSeat from './show-seat';
import ShowSeatDetail from './show-seat-detail';
import ShowSeatUpdate from './show-seat-update';
import ShowSeatDeleteDialog from './show-seat-delete-dialog';

const ShowSeatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ShowSeat />} />
    <Route path="new" element={<ShowSeatUpdate />} />
    <Route path=":id">
      <Route index element={<ShowSeatDetail />} />
      <Route path="edit" element={<ShowSeatUpdate />} />
      <Route path="delete" element={<ShowSeatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShowSeatRoutes;
