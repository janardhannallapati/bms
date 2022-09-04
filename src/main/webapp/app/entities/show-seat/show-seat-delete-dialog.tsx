import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './show-seat.reducer';

export const ShowSeatDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const showSeatEntity = useAppSelector(state => state.showSeat.entity);
  const updateSuccess = useAppSelector(state => state.showSeat.updateSuccess);

  const handleClose = () => {
    navigate('/show-seat' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(showSeatEntity.showSeatId));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="showSeatDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="bmsApp.showSeat.delete.question">
        <Translate contentKey="bmsApp.showSeat.delete.question" interpolate={{ id: showSeatEntity.showSeatId }}>
          Are you sure you want to delete this ShowSeat?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-showSeat" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ShowSeatDeleteDialog;
