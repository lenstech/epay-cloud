package com.lens.epay.common;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.HttpSuccessMessagesConstants.SUCCESSFULLY_DELETED;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Component
public abstract class AbstractController<T extends AbstractEntity, ID extends Serializable, DTO, RES> {

    protected abstract AbstractService<T, ID, DTO, RES> getService();

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    private AuthorizationConfig authorizationConfig;

    public Role getMinRole() {
        return this.minRole;
    }

    public abstract void setMinRole();

    protected Role minRole;

    @ApiOperation(value = "Create Object, it can be done by authorization")
    @PostMapping
    public RES save(@RequestHeader("Authorization") String token, @RequestBody DTO dto) {
        LOGGER.debug(String.format("Saving the dto [%s].", dto));
        setMinRole();
        UUID userId = authorizationConfig.permissionCheck(token, minRole);
        return getService().save(dto,userId);
    }

    @ApiOperation(value = "Get Object")
    @GetMapping
    public RES get(@RequestHeader("Authorization") String token, @RequestParam ID objectId) {
        LOGGER.debug("Requesting {id} records.");
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return getService().get(objectId);
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all")
    public List<RES> getAll(@RequestHeader("Authorization") String token) {
        LOGGER.debug("Requesting all records.");
        authorizationConfig.permissionCheck(token, Role.BASIC_USER);
        return getService().getAll();
    }

    @ApiOperation(value = "Update Object, it can be done by authorization")
    @PutMapping
    public RES update(@RequestHeader("Authorization") String token,
                                 @RequestBody DTO dto,
                                 @RequestParam ID objectId) {
        LOGGER.debug(String.format("Request to update the record [%s].", objectId));
        setMinRole();
        UUID userId = authorizationConfig.permissionCheck(token, minRole);
        return getService().put(objectId, dto, userId);
    }

    @ApiOperation(value = "Delete Object,  it can be done by authorization", response = void.class)
    @DeleteMapping
    public String delete(@RequestHeader("Authorization") String token,
                                 @RequestParam ID objectId) {
        LOGGER.debug(String.format("Request to delete the record [%s].", objectId));
        setMinRole();
        authorizationConfig.permissionCheck(token, minRole);
        getService().delete(objectId);
        return SUCCESSFULLY_DELETED;
    }
}
