package com.lens.epay.common;

import com.lens.epay.exception.BadRequestException;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.repository.EpayRepository;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
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

    public RES save(DTO dto, UUID userId) {
        T entity = getConverter().toEntity(dto);
        return getConverter().toResource(getRepository().save(saveOperations(entity,dto,userId)));
    }

    public RES get(ID id) {
        T entity;
        try {
            entity = getRepository().findOneById(id);
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
        return getConverter().toResource(entity);
    }

    public List<RES> getAll() {
        return getConverter().toResources(getRepository().findAll());
    }

    public Page<RES> getAllWithPage(int pageNumber, String sortBy, Boolean desc) {
        PageRequest pageable;
        if (desc == null) {
            desc = true;
        }
        try {
            if (desc) {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.DESC, sortBy);
            } else {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, sortBy);
            }
            return getRepository().findAll(pageable).map(getConverter()::toResource);
        } catch (Exception e) {
            pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            return getRepository().findAll(pageable).map(getConverter()::toResource);
        }
    }

    @Modifying
    public RES put(ID id, DTO updatedDto, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        T oldEntity;
        Optional<T> oldEntityOpt = getRepository().findById(id);
        if (!oldEntityOpt.isPresent()) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        } else {
            oldEntity = oldEntityOpt.get();
        }
        if (updatedDto == null) {
            throw new BadRequestException(DTO_CANNOT_BE_EMPTY);
        }
        try {
            T updated = getConverter().toEntity(updatedDto);
            updated.setId(oldEntity.getId());
            updated.setCreatedDate(oldEntity.getCreatedDate());
            updated.setVersion(oldEntity.getVersion());
            updated = putOperations(oldEntity, updated, userId);
            return getConverter().toResource(getRepository().save(updated));
        } catch (Exception e) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
    }

    @Transactional
    @Modifying
    public void delete(ID id) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        try {
            deleteOperations(id);
            getRepository().deleteById(id);
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
    }

    @Named("fromIdToEntity")
    public T fromIdToEntity(ID id) {
        try {
            return getRepository().findOneById(id);
        } catch (NullPointerException e) {
            throw new NotFoundException(ID_IS_NOT_EXIST);
        }
    }

    protected T putOperations(T oldEntity, T newEntity, UUID userId) {
        return newEntity;
    }

    protected T saveOperations(T entity, DTO dto, UUID userId) {
        return entity;
    }

    protected void deleteOperations(ID id) {
    }

}
