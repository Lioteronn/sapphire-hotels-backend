package com.lioteron.sapphirehotels.repository;

import com.lioteron.sapphirehotels.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(@NotNull @Email String email);

}
