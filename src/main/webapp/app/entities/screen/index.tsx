import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Screen from './screen';
import ScreenDetail from './screen-detail';
import ScreenUpdate from './screen-update';
import ScreenDeleteDialog from './screen-delete-dialog';

const ScreenRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Screen />} />
    <Route path="new" element={<ScreenUpdate />} />
    <Route path=":id">
      <Route index element={<ScreenDetail />} />
      <Route path="edit" element={<ScreenUpdate />} />
      <Route path="delete" element={<ScreenDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ScreenRoutes;
