package com.lens.epay.repository;

import com.lens.epay.model.entity.Department;
import com.lens.epay.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends EpayRepository<User, UUID> {

    User findUserById(UUID id);

    Set<User> findByIdIn(Collection<UUID> ids);

    User findByEmail(String email);

    User findUserByName(String name);

    Boolean existsByEmail(String email);

    Set<User> findUsersByDepartment(Department department);

    @Query(value = "select * from users where name like '%:name%'", nativeQuery = true)
    List<User> findUsersByName(String name);

    List<User> findAll();

}
