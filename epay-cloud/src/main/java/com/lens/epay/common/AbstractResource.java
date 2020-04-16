package com.lens.epay.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.lens.epay.constant.GeneralConstants.RESOURCE_DATE_TIME_FORMAT;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */
@Getter
@Setter
public class AbstractResource {
    private UUID id;

    private String name;

    @JsonFormat(pattern = RESOURCE_DATE_TIME_FORMAT)
    private ZonedDateTime createdDate;

    @JsonFormat(pattern = RESOURCE_DATE_TIME_FORMAT)
    private ZonedDateTime lastModifiedDate;

}
