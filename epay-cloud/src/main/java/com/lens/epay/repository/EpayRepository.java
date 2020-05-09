package com.lens.epay.repository;

import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface EpayRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    @Override
    default Optional<T> findById(ID id) {
        return Optional.ofNullable(findOneById(id));
    }

    @Named("findOneById")
    T findOneById(ID id);

    void deleteById(ID id);

    void delete(T entity);
}
