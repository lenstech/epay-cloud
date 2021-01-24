package com.lens.epay.common;

import com.lens.epay.constant.GeneralConstants;
import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.repository.EpayRepository;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;
import static com.lens.epay.constant.GeneralConstants.PAGE_SIZE;

/**
 * Created by Emir Gökdemir
 * on 23 Şub 2020
 */
public abstract class AbstractService<T extends AbstractEntity, ID extends Serializable, DTO, RES> {

    public abstract EpayRepository<T, ID> getRepository();

    public abstract Converter<DTO, T, RES> getConverter();

    @Transactional
    public RES save(DTO dto, UUID userId) {
        T entity = getConverter().toEntity(dto);
        return getConverter().toResource(getRepository().save(saveOperations(entity, dto, userId)));
    }

    public RES get(ID id, UUID userId) {
        T entity = fromIdToEntity(id);
        return getConverter().toResource(entity);
    }

    public List<RES> getMultiple(List<ID> ids) {
        List<T> entities;
        try {
            entities = getRepository().findAllByIdIn(ids);
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
        return getConverter().toResources(entities);
    }

    public List<RES> getAll(UUID userId) {
        return getConverter().toResources(getRepository().findAll());
    }

    public Page<RES> getAllWithPage(int pageNumber, String sortBy, Sort.Direction direction, UUID userId) {
        PageRequest pageable;
        if (sortBy == null) {
            pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(GeneralConstants.DEFAULT_SORT_BY).descending());
        } else {
            if (direction == null) {
                direction = Sort.Direction.DESC;
            }
            try {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, direction, sortBy);
            } catch (PropertyReferenceException exception) {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(GeneralConstants.DEFAULT_SORT_BY).descending());
            }
        }
        return getRepository().findAll(pageable).map(getConverter()::toResource);
    }

    @Modifying
    @Transactional
    public RES put(ID id, DTO updatedDto, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        T entity = fromIdToEntity(id);
        if (updatedDto == null) {
            throw new BadRequestException(DTO_CANNOT_BE_EMPTY);
        }
        try {
            getConverter().toEntityForUpdate(updatedDto, entity);
            entity = putOperations(entity, userId);
            return getConverter().toResource(getRepository().save(entity));
        } catch (Exception e) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
    }

    @Transactional
    @Modifying
    public void delete(ID id, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        try {
            deleteOperations(id, userId);
            getRepository().deleteById(id);
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
    }

    @Named("fromIdToEntity")
    public T fromIdToEntity(ID id) {
        Optional<T> entityOpt = getRepository().findById(id);
        if (!entityOpt.isPresent()) {
            throw new NotFoundException(getClass().getSimpleName().replace("Service", " ") + ID_IS_NOT_EXIST);
        } else {
            return entityOpt.get();
        }
    }

    protected T putOperations(T entity, UUID userId) {
        return entity;
    }

    protected T saveOperations(T entity, DTO dto, UUID userId) {
        return entity;
    }

    protected void deleteOperations(ID id, UUID userId) {
    }
}

