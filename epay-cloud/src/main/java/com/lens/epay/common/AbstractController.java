package com.lens.epay.common;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.NOT_AUTHORIZED_FOR_OPERATION;
import static com.lens.epay.constant.HttpSuccessMessagesConstants.SUCCESSFULLY_DELETED;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Component
public abstract class AbstractController<T extends AbstractEntity, ID extends Serializable, DTO, RES> {


    protected abstract AbstractService<T, ID, DTO, RES> getService();

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    private AuthorizationConfig authorizationConfig;

    public abstract void setSaveRole();

    public abstract void setGetRole();

    public abstract void setGetAllRole();

    public abstract void setUpdateRole();

    public abstract void setDeleteRole();

    public abstract void setEntityName();

    protected Role saveRole;
    protected Role getRole;
    protected Role getAllRole;
    protected Role updateRole;
    protected Role deleteRole;
    protected String entityName;

    @ApiOperation(value = "Create Object, it can be done by authorization")
    @PostMapping
    public RES save(@RequestHeader("Authorization") String token, @RequestBody @Valid DTO dto) {
        setEntityName();
        logger.info(String.format("Saving the " + entityName + "Dto with id: %s.", dto));
        setSaveRole();
        UUID userId = authorizationConfig.permissionCheck(token, saveRole);
        return getService().save(dto, userId);
    }

    @ApiOperation(value = "Get Object")
    @GetMapping
    public RES get(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam ID objectId) {
        setEntityName();
        logger.info(String.format("Requesting " + entityName + " id: %s records.", objectId));
        setGetRole();
        if (getRole == null) {
            return getService().get(objectId);
        } else if (token == null) {
            throw new UnauthorizedException(NOT_AUTHORIZED_FOR_OPERATION);
        }
        authorizationConfig.permissionCheck(token, getRole);
        return getService().get(objectId);
    }


    @ApiOperation(value = "Get Multiple Object")
    @PostMapping("/multiple")
    public List<RES> get(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody List<ID> objectIds) {
        logger.debug("Requesting  multiple " + entityName + " records.");
        setGetRole();
        if (getRole == null) {
            return getService().getMultiple(objectIds);
        } else if (token == null) {
            throw new UnauthorizedException(NOT_AUTHORIZED_FOR_OPERATION);
        }
        authorizationConfig.permissionCheck(token, getRole);
        return getService().getMultiple(objectIds);
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all")
    public List<RES> getAll(@RequestHeader(value = "Authorization", required = false) String token) {
        setEntityName();
        setGetAllRole();
        logger.info("Requesting all records of " + entityName + ".");
        if (getAllRole == null) {
            return getService().getAll();
        } else if (token == null) {
            throw new UnauthorizedException(NOT_AUTHORIZED_FOR_OPERATION);
        }
        authorizationConfig.permissionCheck(token, getAllRole);
        return getService().getAll();
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all/{page}")
    public Page<RES> getAllWithPage(@RequestHeader(value = "Authorization", required = false) String token,
                                    @PathVariable("page") int pageNo,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) Boolean desc) {
        setEntityName();
        setGetAllRole();
        logger.info("Requesting all records" + entityName + " by page.");
        if (getAllRole == null) {
            return getService().getAllWithPage(pageNo, sortBy, desc);
        } else if (token == null) {
            throw new UnauthorizedException(NOT_AUTHORIZED_FOR_OPERATION);
        }
        authorizationConfig.permissionCheck(token, getAllRole);
        return getService().getAllWithPage(pageNo, sortBy, desc);
    }

    @ApiOperation(value = "Update Object, it can be done by authorization")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RES update(@RequestHeader("Authorization") String token,
                      @RequestBody @Valid DTO dto,
                      @RequestParam ID objectId) {
        setEntityName();
        logger.info(String.format("Request to update a " + entityName + " object record with id: %s.", objectId));
        setUpdateRole();
        UUID userId = authorizationConfig.permissionCheck(token, updateRole);
        return getService().put(objectId, dto, userId);
    }

    @ApiOperation(value = "Delete Object,  it can be done by authorization", response = void.class)
    @DeleteMapping
    public String delete(@RequestHeader("Authorization") String token,
                         @RequestParam ID objectId) {
        setEntityName();
        logger.info(String.format("Request to delete a " + entityName + "object record with id: %s.", objectId));
        setDeleteRole();
        UUID userId = authorizationConfig.permissionCheck(token, deleteRole);
        getService().delete(objectId, userId);
        return SUCCESSFULLY_DELETED;
    }
}
