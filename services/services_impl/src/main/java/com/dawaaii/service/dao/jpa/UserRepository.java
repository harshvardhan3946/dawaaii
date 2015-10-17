package com.dawaaii.service.dao.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawaaii.service.security.model.Role;
import com.dawaaii.service.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	List<User> findAllByRoles(List<Role> roles);

	List<User> findByFirstNameLike(String firstName, Pageable pageable);

	List<User> findByLastNameLike(String lastname, Pageable pageable);

	List<User> findByFirstNameLikeAndLastNameLike(String firstName, String lastName, Pageable pageable);

	long countByFirstNameLikeAndLastNameLikeAndEmailLike(String firstName,String lastName, String email);

	List<User> findByFirstNameLikeAndLastNameLikeAndEmailLike(String firstName,String lastName, String email,Pageable pageable);
}
