package com.lens.epay.common;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.HttpSuccessMessagesConstants.SUCCESSFULLY_DELETED;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Component
public abstract class AbstractController<T extends AbstractEntity<ID>, ID extends Serializable, DTO, RES> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);
    protected Role saveRole;
    protected Role getRole;
    protected Role getAllRole;
    protected Role updateRole;
    protected Role deleteRole;
    protected String entityName;
    @Autowired
    private AuthorizationConfig authorizationConfig;

    protected abstract AbstractService<T, ID, DTO, RES> getService();

    public abstract void setSaveRole();

    public abstract void setGetRole();

    public abstract void setGetAllRole();

    public abstract void setUpdateRole();

    public abstract void setDeleteRole();

    public abstract void setEntityName();

    @ApiOperation(value = "Create Object, it can be done by authorization")
    @PostMapping
    public RES save(@RequestHeader("Authorization") String token, @Valid @RequestBody DTO dto) {
        setEntityName();
        logger.info(String.format("Saving the %s Dto with id: %s.", entityName, dto));
        setSaveRole();
        UUID userId = authorizationConfig.permissionCheck(token, saveRole);
        return getService().save(dto, userId);
    }

    @ApiOperation(value = "Get Object")
    @GetMapping
    public RES get(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam ID objectId) {
        setEntityName();
        logger.info(String.format("Requesting %s  id: %s records.", entityName, objectId));
        setGetRole();
        UUID userId = authorizationConfig.permissionCheck(token, getRole);
        return getService().get(objectId, userId);
    }

    @ApiOperation(value = "Get Multiple Object")
    @PostMapping("/multiple")
    public List<RES> get(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody List<ID> objectIds) {
        setEntityName();
        logger.info("Requesting  multiple %s  records.");
        setGetRole();
        authorizationConfig.permissionCheck(token, getRole);
        return getService().getMultiple(objectIds);
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all")
    public List<RES> getAll(@RequestHeader(value = "Authorization", required = false) String token) {
        setGetAllRole();
        setEntityName();
        UUID userId = authorizationConfig.permissionCheck(token, getAllRole);
        logger.info(String.format("Requesting all records of %s with userId %s", entityName, userId));
        return getService().getAll(userId);
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all/{page}")
    public Page<RES> getAllWithPage(@RequestHeader(value = "Authorization", required = false) String token,
                                    @PathVariable("page") int pageNo,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) Sort.Direction direction) {
        setGetAllRole();
        setEntityName();
        UUID userId = authorizationConfig.permissionCheck(token, getAllRole);
        logger.info(String.format("Requesting all records of %s with userId %s by page", entityName, userId));
        return getService().getAllWithPage(pageNo, sortBy, direction, userId);
    }

    @ApiOperation(value = "Update Object, it can be done by authorization")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RES update(@RequestHeader("Authorization") String token,
                      @RequestBody @Valid DTO dto,
                      @RequestParam ID objectId) {
        setEntityName();
        logger.info(String.format("Request to update a %s  object record with id: %s.", entityName, objectId));
        setUpdateRole();
        UUID userId = authorizationConfig.permissionCheck(token, updateRole);
        return getService().put(objectId, dto, userId);
    }

    @ApiOperation(value = "Delete Object,  it can be done by authorization", response = void.class)
    @DeleteMapping
    public String delete(@RequestHeader("Authorization") String token,
                         @RequestParam ID objectId) {
        setEntityName();
        logger.info(String.format("Request to delete a %s object record with id: %s.", entityName, objectId));
        setDeleteRole();
        UUID userId = authorizationConfig.permissionCheck(token, deleteRole);
        getService().delete(objectId, userId);
        return SUCCESSFULLY_DELETED;
    }
}
