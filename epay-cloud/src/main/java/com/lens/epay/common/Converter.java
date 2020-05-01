package com.lens.epay.common;

import java.util.List;
import java.util.Set;

/**
 * DTO-Entity-Resource Converter Interface.
 */
public interface Converter<DTO, Entity, Resource> {

    Resource toResource(Entity entity);

    Entity toEntity(DTO dto);

    List<Resource> toResources(List<Entity> entities);

    Set<Resource> toResources(Set<Entity> entities);

}
