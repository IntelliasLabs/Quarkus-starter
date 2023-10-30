package com.intellias.basicsandbox.persistence;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<ItemEntity> {

    @Transactional
    public int updateItemName(UUID itemId, String name) {
        return update("name = ?1 where id = ?2", name, itemId);
    }

    public ItemEntity findByUUID(UUID uuid) {
        return find("uuid", uuid).firstResult();
    }

    public void deleteByUUID(UUID uuid) {
        delete("uuid", uuid);
    }
}
