import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './file.reducer';

export const FileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fileEntity = useAppSelector(state => state.file.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileDetailsHeading">
          <Translate contentKey="fileConverterApp.file.detail.title">File</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="fileConverterApp.file.id">Id</Translate>
            </span>
          </dt>
          <dd>{fileEntity.id}</dd>
          <dt>
            <span id="fileName">
              <Translate contentKey="fileConverterApp.file.fileName">File Name</Translate>
            </span>
          </dt>
          <dd>{fileEntity.fileName}</dd>
          <dt>
            <span id="fileType">
              <Translate contentKey="fileConverterApp.file.fileType">File Type</Translate>
            </span>
          </dt>
          <dd>{fileEntity.fileType}</dd>
          <dt>
            <span id="lastModified">
              <Translate contentKey="fileConverterApp.file.lastModified">Last Modified</Translate>
            </span>
          </dt>
          <dd>
            {fileEntity.lastModified ? <TextFormat value={fileEntity.lastModified} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="converted">
              <Translate contentKey="fileConverterApp.file.converted">Converted</Translate>
            </span>
          </dt>
          <dd>{fileEntity.converted ? 'true' : 'false'}</dd>
          <dt>
            <span id="s3Url">
              <Translate contentKey="fileConverterApp.file.s3Url">S 3 Url</Translate>
            </span>
          </dt>
          <dd>{fileEntity.s3Url}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="fileConverterApp.file.category">Category</Translate>
            </span>
          </dt>
          <dd>{fileEntity.category}</dd>
        </dl>
        <Button tag={Link} to="/file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/file/${fileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileDetail;
