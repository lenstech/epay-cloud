package com.lens.epay.common;

import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO-Entity-Resource Converter Interface.
 */
public interface Converter<DTO, Entity, Resource> {

    Resource toResource(Entity entity);

    Entity toEntity(DTO dto);

    List<Resource> toResources(List<Entity> entities);

    Set<Resource> toResources(Set<Entity> entities);

//    Entity idToEntity(UUID id);
}
