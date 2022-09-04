import actor from 'app/entities/actor/actor.reducer';
import genre from 'app/entities/genre/genre.reducer';
import language from 'app/entities/language/language.reducer';
import movie from 'app/entities/movie/movie.reducer';
import city from 'app/entities/city/city.reducer';
import country from 'app/entities/country/country.reducer';
import address from 'app/entities/address/address.reducer';
import theatre from 'app/entities/theatre/theatre.reducer';
import screen from 'app/entities/screen/screen.reducer';
import seat from 'app/entities/seat/seat.reducer';
import showSeat from 'app/entities/show-seat/show-seat.reducer';
import show from 'app/entities/show/show.reducer';
import booking from 'app/entities/booking/booking.reducer';
import customer from 'app/entities/customer/customer.reducer';
import payment from 'app/entities/payment/payment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  actor,
  genre,
  language,
  movie,
  city,
  country,
  address,
  theatre,
  screen,
  seat,
  showSeat,
  show,
  booking,
  customer,
  payment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
