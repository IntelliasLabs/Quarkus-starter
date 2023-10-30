package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.controller.dto.*;
import com.intellias.basicsandbox.controller.mapper.ItemMapper;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

@Path(ItemController.API_VERSION + ItemController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ItemController {

    public static final String PATH = "items";
    public static final String API_VERSION = "/api/v1/";

    @Inject
    ItemService itemService;

    @POST
    @RolesAllowed("USER")
    public Response save(@Valid @RequestBody final ItemDTO item) {
        log.debug("Item to save: {}", item);
        ItemEntity saved = itemService.save(ItemMapper.INSTANCE.toEntity(item));
        log.info("Item successfully saved: {}", saved);
        return Response.status(Response.Status.CREATED).entity(ItemMapper.INSTANCE.toDTO(saved)).build();
    }

    @PUT
    @RolesAllowed("USER")
    public ItemDTO update(@Valid @RequestBody final ItemDTO item) {
        log.debug("Item with id {} to update: {}", item.getId(), item);
        ItemEntity updated = itemService.update(item.getId(), ItemMapper.INSTANCE.toEntity(item));
        log.info("Item with id {} successfully updated: {}", item.getId(), updated);
        return ItemMapper.INSTANCE.toDTO(updated);
    }

    @GET
    @PermitAll
    @Path("/{id}")
    public ItemDTO getById(@PathParam("id") final UUID id) {
        return ItemMapper.INSTANCE.toDTO(itemService.getById(id));
    }

    @GET
    @Path("/{id}/{locale}")
    @Produces("application/hal+json;charset=utf8")
    @PermitAll
    public LocalizedItemDTO getByIdLocalized(@PathParam("id") final UUID id, @PathParam("locale") final Locale locale) {
        ItemEntity entity = itemService.getById(id);
        ResourceBundle localizedResource = ResourceBundle.getBundle("resourcebundle.messages", locale);
        ItemDTO itemDTO = ItemMapper.INSTANCE.toDTO(entity);
        final String localizationKeyPrefix = "currency.";
        String translatedValue = localizedResource.getString(localizationKeyPrefix + itemDTO.getCurrencyCode());
        return new LocalizedItemDTO(itemDTO, translatedValue);
    }

    @GET
    @PermitAll
    public List<ItemDTO> getAll() {
        List<ItemEntity> items = itemService.findAll();
        List<ItemDTO> itemDTOs = items.stream().map(ItemMapper.INSTANCE::toDTO).toList();
        log.info("Items found: {}", itemDTOs);
        return itemDTOs;
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response delete(@PathParam("id") final UUID id) {
        log.debug("Item with id {} to delete", id);
        itemService.delete(id);
        log.info("Item with id {} successfully deleted", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
