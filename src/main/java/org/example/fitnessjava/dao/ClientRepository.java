package org.example.fitnessjava.dao;

import org.example.fitnessjava.pojo.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByOpenid(String openid);
}
