package dev.lin.helpdesk_software_api.Implementations;

import java.util.List;

public interface IGenericService<T, U> {
    List<T> getAllEntities();
    T storeEntity(U dtoRequest);
}