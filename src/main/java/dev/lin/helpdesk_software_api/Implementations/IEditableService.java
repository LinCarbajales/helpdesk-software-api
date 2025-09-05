package dev.lin.helpdesk_software_api.Implementations;

import java.util.List;

public interface IEditableService<T, U> {
    List<T> getAllEntities();
    T storeEntity(U dtoRequest);
    T showById(Long id);
}