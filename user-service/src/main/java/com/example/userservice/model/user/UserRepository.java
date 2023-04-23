package com.example.userservice.model.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Query("select u from UserEntity u where u.userId =:id")
    UserEntity findByUserId(@Param("id") String userId);

}
