import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Actor from './actor';
import ActorDetail from './actor-detail';
import ActorUpdate from './actor-update';
import ActorDeleteDialog from './actor-delete-dialog';

const ActorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Actor />} />
    <Route path="new" element={<ActorUpdate />} />
    <Route path=":id">
      <Route index element={<ActorDetail />} />
      <Route path="edit" element={<ActorUpdate />} />
      <Route path="delete" element={<ActorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActorRoutes;
