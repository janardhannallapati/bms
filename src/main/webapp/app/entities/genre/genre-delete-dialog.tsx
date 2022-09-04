import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './genre.reducer';

export const GenreDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const genreEntity = useAppSelector(state => state.genre.entity);
  const updateSuccess = useAppSelector(state => state.genre.updateSuccess);

  const handleClose = () => {
    navigate('/genre' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(genreEntity.genreId));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="genreDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="bmsApp.genre.delete.question">
        <Translate contentKey="bmsApp.genre.delete.question" interpolate={{ id: genreEntity.genreId }}>
          Are you sure you want to delete this Genre?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-genre" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default GenreDeleteDialog;
