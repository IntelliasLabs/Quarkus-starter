package com.intellias.basicsandbox.controller.mapper;

import com.intellias.basicsandbox.controller.dto.ItemDTO;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemEntity toEntity(final ItemDTO item);

    ItemDTO toDTO(final ItemEntity itemEntity);
}
