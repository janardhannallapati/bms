import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './theatre.reducer';

export const TheatreDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const theatreEntity = useAppSelector(state => state.theatre.entity);
  const updateSuccess = useAppSelector(state => state.theatre.updateSuccess);

  const handleClose = () => {
    navigate('/theatre' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(theatreEntity.theatreId));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="theatreDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="bmsApp.theatre.delete.question">
        <Translate contentKey="bmsApp.theatre.delete.question" interpolate={{ id: theatreEntity.theatreId }}>
          Are you sure you want to delete this Theatre?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-theatre" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default TheatreDeleteDialog;
