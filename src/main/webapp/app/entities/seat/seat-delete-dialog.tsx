import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './seat.reducer';

export const SeatDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const seatEntity = useAppSelector(state => state.seat.entity);
  const updateSuccess = useAppSelector(state => state.seat.updateSuccess);

  const handleClose = () => {
    navigate('/seat' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(seatEntity.seatId));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="seatDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="bmsApp.seat.delete.question">
        <Translate contentKey="bmsApp.seat.delete.question" interpolate={{ id: seatEntity.seatId }}>
          Are you sure you want to delete this Seat?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-seat" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SeatDeleteDialog;
