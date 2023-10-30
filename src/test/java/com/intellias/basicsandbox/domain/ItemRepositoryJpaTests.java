package com.intellias.basicsandbox.domain;

import com.intellias.basicsandbox.persistence.ItemRepository;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
class ItemRepositoryJpaTests {

    @Inject
    ItemRepository itemRepository;

    @Test
    @Transactional
    void findAllItems() {
        var item1 = new ItemEntity(null, "Item name", null, "UAH");
        var item2 = new ItemEntity(null, "Item name 2", null, "USD");

        itemRepository.persist(item1);
        itemRepository.persist(item2);

        Iterable<ItemEntity> actualItems = itemRepository.listAll();

        assertThat(StreamSupport.stream(actualItems.spliterator(), false)
                .filter(item -> item.getId().equals(item1.getId()) || item.getId().equals(item2.getId()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    @Transactional
    void findItemByIdWhenExisting() {
        var item = new ItemEntity(null, "Item name", null, "UAH");
        itemRepository.persist(item);

        ItemEntity actualItem = itemRepository.findByUUID(item.getId());

        assertThat(actualItem).isNotNull();
        assertThat(actualItem.getId()).isEqualTo(item.getId());
    }

    @Test
    @Transactional
    void findItemByIdWhenNotExisting() {
        ItemEntity actualItem = itemRepository.findByUUID(UUID.randomUUID());
        assertThat(actualItem).isNull();
    }

    @Test
    @Transactional
    void deleteById() {
        var item = new ItemEntity(null, "Item name", null, "UAH");
        itemRepository.persist(item);

        itemRepository.deleteByUUID(item.getId());

        assertThat(itemRepository.findByUUID(item.getId())).isNull();
    }

    @Test
    @Transactional
    void updateItemNameById() {
        var initialItem = new ItemEntity(null, "Item name", null, "UAH");
        itemRepository.persist(initialItem);

        String newName = "Item Name Changed";
        itemRepository.updateItemName(initialItem.getId(), newName);

        ItemEntity updatedItem = itemRepository.findByUUID(initialItem.getId());

        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getName()).isEqualTo(newName);
    }

}
