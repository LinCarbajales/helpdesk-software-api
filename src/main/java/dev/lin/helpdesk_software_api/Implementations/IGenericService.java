package dev.lin.helpdesk_software_api.Implementations;

import java.util.List;
import java.util.Optional;

public interface IGenericService<T, U> {
    List<T> getAllEntities();
    T storeEntity(U dtoRequest);
    Optional<T> showById(Long id);
}