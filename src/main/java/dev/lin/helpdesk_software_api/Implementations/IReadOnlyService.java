package dev.lin.helpdesk_software_api.Implementations;

import java.util.List;
import java.util.Optional;

public interface IReadOnlyService<T> {
    List<T> getAllEntities();
    Optional<T> showById(Long id);
}