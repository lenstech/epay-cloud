package com.lens.epay.common;

import com.lens.epay.exception.BadRequestException;
import com.lens.epay.repository.EpayRepository;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.*;

/**
 * Created by Emir Gökdemir
 * on 23 Şub 2020
 */
public abstract class AbstractService<T extends AbstractEntity, ID extends Serializable, DTO, RES> {

    public abstract EpayRepository<T, ID> getRepository();

    public abstract Converter<DTO, T, RES> getConverter();

    public RES save(DTO dto, UUID userId) {
        return getConverter().toResource(getRepository().save(getConverter().toEntity(dto)));
    }

    public RES get(ID id) {
        T entity;
        try {
            entity = getRepository().findOneById(id);
        } catch (NullPointerException e) {
            throw new BadRequestException(ID_IS_NOT_EXIST);
        }
        return getConverter().toResource(entity);
    }

    public List<RES> getAll() {
        return getConverter().toResources(getRepository().findAll());
    }

    @Transactional
    public RES put(ID id, DTO updatedDto, UUID userId) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        T theReal = getRepository().findById(id).orElseThrow(() -> new BadRequestException(ID_IS_NOT_EXIST));
        if (updatedDto == null) {
            throw new BadRequestException(DTO_CANNOT_BE_EMPTY);
        }
        try {
            T updated = getConverter().toEntity(updatedDto);
            updated.setId(theReal.getId());
            updated.setCreatedDate(theReal.getCreatedDate());
            return getConverter().toResource(getRepository().save(updated));
        } catch (Exception e) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
    }


    @Transactional
    public void delete(ID id) {
        if (id == null) {
            throw new BadRequestException(ID_CANNOT_BE_EMPTY);
        }
        T entity = getRepository().findById(id).orElseThrow(() -> new BadRequestException(ID_IS_NOT_EXIST));
        getRepository().delete(entity);
    }

    @Named("fromIdToEntity")
    public T fromIdToEntity(ID id){
        try {
            return getRepository().findOneById(id);
        } catch (NullPointerException e) {
            throw new BadRequestException(ID_IS_NOT_EXIST);
        }
    }
}
