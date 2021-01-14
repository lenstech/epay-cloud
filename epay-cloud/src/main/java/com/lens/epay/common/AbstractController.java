package com.lens.epay.common;

import com.lens.epay.configuration.AuthorizationConfig;
import com.lens.epay.enums.Role;
import com.lens.epay.exception.BadRequestException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthorizationConfig authorizationConfig;

    protected abstract AbstractService<T, ID, DTO, RES> getService();

    public abstract Role getSaveRole();

    public abstract Role getGetRole();

    public abstract Role getGetAllRole();

    public abstract Role getUpdateRole();

    public abstract Role getDeleteRole();

    @ApiOperation(value = "Create Object, it can be done by authorization")
    @PostMapping
    public RES save(@RequestHeader("Authorization") String token, @Valid @RequestBody DTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        logger.info(String.format("Saving the Dto with id: %s.", dto));
        UUID userId = authorizationConfig.permissionCheck(token, getSaveRole());
        return getService().save(dto, userId);
    }

    @ApiOperation(value = "Get Object")
    @GetMapping
    public RES get(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam ID objectId) {
        logger.info(String.format("Requesting  id: %s records.", objectId));
        UUID userId = authorizationConfig.permissionCheck(token, getGetRole());
        return getService().get(objectId, userId);
    }

    @ApiOperation(value = "Get Multiple Object")
    @PostMapping("/multiple")
    public List<RES> get(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody List<ID> objectIds) {
        logger.info("Requesting  multiple %s  records.");
        authorizationConfig.permissionCheck(token, getGetRole());
        return getService().getMultiple(objectIds);
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all")
    public List<RES> getAll(@RequestHeader(value = "Authorization", required = false) String token) {
        UUID userId = authorizationConfig.permissionCheck(token, getGetAllRole());
        logger.info(String.format("Requesting all records with userId %s", userId));
        return getService().getAll(userId);
    }

    @ApiOperation(value = "Get All Object", responseContainer = "List")
    @GetMapping("/all/{page}")
    public Page<RES> getAllWithPage(@RequestHeader(value = "Authorization", required = false) String token,
                                    @PathVariable("page") int pageNo,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) Sort.Direction direction) {
        UUID userId = authorizationConfig.permissionCheck(token, getGetAllRole());
        logger.info(String.format("Requesting all records with userId %s by page", userId));
        return getService().getAllWithPage(pageNo, sortBy, direction, userId);
    }

    @ApiOperation(value = "Update Object, it can be done by authorization")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RES update(@RequestHeader("Authorization") String token,
                      @RequestBody @Valid DTO dto,
                      BindingResult bindingResult,
                      @RequestParam ID objectId) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        logger.info(String.format("Request to update a object record with id: %s.", objectId));
        UUID userId = authorizationConfig.permissionCheck(token, getUpdateRole());
        return getService().put(objectId, dto, userId);
    }

    @ApiOperation(value = "Delete Object,  it can be done by authorization", response = void.class)
    @DeleteMapping
    public String delete(@RequestHeader("Authorization") String token,
                         @RequestParam ID objectId) {
        logger.info(String.format("Request to delete an object record with id: %s.", objectId));
        UUID userId = authorizationConfig.permissionCheck(token, getDeleteRole());
        getService().delete(objectId, userId);
        return SUCCESSFULLY_DELETED;
    }
}
