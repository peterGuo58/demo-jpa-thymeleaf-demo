package com.zq.springboot.demojpathymeleafdemo.repositorys;

import com.zq.springboot.demojpathymeleafdemo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select  u from User  u")
    Page<User> findList(Pageable pageable);
    User findById(long id);
    User findByUserName(String username);
    void deleteById(Long id);





}
