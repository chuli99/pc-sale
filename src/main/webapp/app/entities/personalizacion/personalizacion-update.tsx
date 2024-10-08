import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDispositivo } from 'app/shared/model/dispositivo.model';
import { getEntities as getDispositivos } from 'app/entities/dispositivo/dispositivo.reducer';
import { IPersonalizacion } from 'app/shared/model/personalizacion.model';
import { getEntity, updateEntity, createEntity, reset } from './personalizacion.reducer';

export const PersonalizacionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dispositivos = useAppSelector(state => state.dispositivo.entities);
  const personalizacionEntity = useAppSelector(state => state.personalizacion.entity);
  const loading = useAppSelector(state => state.personalizacion.loading);
  const updating = useAppSelector(state => state.personalizacion.updating);
  const updateSuccess = useAppSelector(state => state.personalizacion.updateSuccess);

  const handleClose = () => {
    navigate('/personalizacion');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getDispositivos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...personalizacionEntity,
      ...values,
      dispositivo: dispositivos.find(it => it.id.toString() === values.dispositivo.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...personalizacionEntity,
          dispositivo: personalizacionEntity?.dispositivo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="servidorApiApp.personalizacion.home.createOrEditLabel" data-cy="PersonalizacionCreateUpdateHeading">
            <Translate contentKey="servidorApiApp.personalizacion.home.createOrEditLabel">Create or edit a Personalizacion</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="personalizacion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('servidorApiApp.personalizacion.nombre')}
                id="personalizacion-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('servidorApiApp.personalizacion.descripcion')}
                id="personalizacion-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="personalizacion-dispositivo"
                name="dispositivo"
                data-cy="dispositivo"
                label={translate('servidorApiApp.personalizacion.dispositivo')}
                type="select"
                required
              >
                <option value="" key="0" />
                {dispositivos
                  ? dispositivos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personalizacion" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PersonalizacionUpdate;
