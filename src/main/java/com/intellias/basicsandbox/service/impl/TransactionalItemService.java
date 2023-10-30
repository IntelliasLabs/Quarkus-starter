package com.intellias.basicsandbox.service.impl;

import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionalItemService implements ItemService {

    @Inject
    ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemEntity save(ItemEntity item) {
        itemRepository.persist(item);
        return item;
    }

    @Override
    @Transactional
    public ItemEntity update(UUID id, ItemEntity item) {
        ItemEntity attachedEntity = itemRepository.findByUUID(id);
        if (attachedEntity != null) {
            attachedEntity.setName(item.getName());
            attachedEntity.setCreditCard(item.getCreditCard());
            attachedEntity.setCurrencyCode(item.getCurrencyCode());
            return attachedEntity;
        }
        throw new ItemNotFoundException(id);
    }

    @Override
    public ItemEntity getById(UUID id) {
        return itemRepository.findByUUID(id);
    }

    @Override
    public List<ItemEntity> findAll() {
        return itemRepository.listAll();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        itemRepository.deleteByUUID(id);
    }
}
