import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './actor.reducer';

export const ActorDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const actorEntity = useAppSelector(state => state.actor.entity);
  const updateSuccess = useAppSelector(state => state.actor.updateSuccess);

  const handleClose = () => {
    navigate('/actor' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(actorEntity.actorId));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="actorDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="bmsApp.actor.delete.question">
        <Translate contentKey="bmsApp.actor.delete.question" interpolate={{ id: actorEntity.actorId }}>
          Are you sure you want to delete this Actor?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-actor" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ActorDeleteDialog;
