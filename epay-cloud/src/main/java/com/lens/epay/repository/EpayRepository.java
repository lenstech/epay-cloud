package com.lens.epay.repository;

import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface EpayRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    @Override
    default Optional<T> findById(ID id) {
        return Optional.ofNullable(findOneById(id));
    }

    @Named("findOneById")
    T findOneById(ID id);

    @Named("findAllByIdIn")
    List<T> findAllByIdIn(List<ID> ids);

    @Named("findByIdIn")
    Set<T> findByIdIn(Collection<ID> ids);

    void deleteById(ID id);

    void delete(T entity);
}
