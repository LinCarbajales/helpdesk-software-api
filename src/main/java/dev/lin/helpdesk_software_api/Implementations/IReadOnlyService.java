package dev.lin.helpdesk_software_api.Implementations;

import java.util.List;

public interface IReadOnlyService<T> {
    List<T> getAllEntities();
    T showById(Long id);
}