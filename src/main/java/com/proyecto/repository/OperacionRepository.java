package com.proyecto.repository;

import com.proyecto.model.Operacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OperacionRepository extends MongoRepository<Operacion, String> {
}