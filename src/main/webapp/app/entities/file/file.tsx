import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './file.reducer';

export const File = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const fileList = useAppSelector(state => state.file.entities);
  const loading = useAppSelector(state => state.file.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="file-heading" data-cy="FileHeading">
        <Translate contentKey="fileConverterApp.file.home.title">Files</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="fileConverterApp.file.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/file/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="fileConverterApp.file.home.createLabel">Create new File</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {fileList && fileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="fileConverterApp.file.id">Id</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fileName')}>
                  <Translate contentKey="fileConverterApp.file.fileName">File Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileName')} />
                </th>
                <th className="hand" onClick={sort('fileType')}>
                  <Translate contentKey="fileConverterApp.file.fileType">File Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileType')} />
                </th>
                <th className="hand" onClick={sort('lastModified')}>
                  <Translate contentKey="fileConverterApp.file.lastModified">Last Modified</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModified')} />
                </th>
                <th className="hand" onClick={sort('converted')}>
                  <Translate contentKey="fileConverterApp.file.converted">Converted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('converted')} />
                </th>
                <th className="hand" onClick={sort('s3Url')}>
                  <Translate contentKey="fileConverterApp.file.s3Url">S 3 Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('s3Url')} />
                </th>
                <th className="hand" onClick={sort('category')}>
                  <Translate contentKey="fileConverterApp.file.category">Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('category')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fileList.map((file, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/file/${file.id}`} color="link" size="sm">
                      {file.id}
                    </Button>
                  </td>
                  <td>{file.fileName}</td>
                  <td>{file.fileType}</td>
                  <td>{file.lastModified ? <TextFormat type="date" value={file.lastModified} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{file.converted ? 'true' : 'false'}</td>
                  <td>{file.s3Url}</td>
                  <td>{file.category}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/file/${file.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/file/${file.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/file/${file.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="fileConverterApp.file.home.notFound">No Files found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default File;
