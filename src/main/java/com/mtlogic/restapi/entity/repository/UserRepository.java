package com.mtlogic.restapi.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtlogic.restapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
    public User findByEmailAddress(String emailAddress);
    public User findById(Long id);
    public User findByAuthToken(String token);
}