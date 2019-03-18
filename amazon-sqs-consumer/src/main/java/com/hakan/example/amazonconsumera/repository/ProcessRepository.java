package com.hakan.example.amazonconsumera.repository;

import com.hakan.example.amazonconsumera.model.ProcessMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends CrudRepository<ProcessMessage, String> {
}
