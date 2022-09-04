import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/actor">
        <Translate contentKey="global.menu.entities.actor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/genre">
        <Translate contentKey="global.menu.entities.genre" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/language">
        <Translate contentKey="global.menu.entities.language" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/movie">
        <Translate contentKey="global.menu.entities.movie" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        <Translate contentKey="global.menu.entities.city" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/address">
        <Translate contentKey="global.menu.entities.address" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/theatre">
        <Translate contentKey="global.menu.entities.theatre" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/screen">
        <Translate contentKey="global.menu.entities.screen" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/seat">
        <Translate contentKey="global.menu.entities.seat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/show-seat">
        <Translate contentKey="global.menu.entities.showSeat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/show">
        <Translate contentKey="global.menu.entities.show" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/booking">
        <Translate contentKey="global.menu.entities.booking" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/customer">
        <Translate contentKey="global.menu.entities.customer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
