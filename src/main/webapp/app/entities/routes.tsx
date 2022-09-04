import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Actor from './actor';
import Genre from './genre';
import Language from './language';
import Movie from './movie';
import City from './city';
import Country from './country';
import Address from './address';
import Theatre from './theatre';
import Screen from './screen';
import Seat from './seat';
import ShowSeat from './show-seat';
import Show from './show';
import Booking from './booking';
import Customer from './customer';
import Payment from './payment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="actor/*" element={<Actor />} />
        <Route path="genre/*" element={<Genre />} />
        <Route path="language/*" element={<Language />} />
        <Route path="movie/*" element={<Movie />} />
        <Route path="city/*" element={<City />} />
        <Route path="country/*" element={<Country />} />
        <Route path="address/*" element={<Address />} />
        <Route path="theatre/*" element={<Theatre />} />
        <Route path="screen/*" element={<Screen />} />
        <Route path="seat/*" element={<Seat />} />
        <Route path="show-seat/*" element={<ShowSeat />} />
        <Route path="show/*" element={<Show />} />
        <Route path="booking/*" element={<Booking />} />
        <Route path="customer/*" element={<Customer />} />
        <Route path="payment/*" element={<Payment />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
