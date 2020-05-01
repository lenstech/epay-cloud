package com.lens.epay.common;

import com.lens.epay.repository.EpayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * Created by Emir Gökdemir
 * on 1 May 2020
 */

public abstract class AbstractMapperUtil<T extends AbstractEntity, ID extends Serializable> {

    public abstract EpayRepository<T, ID> getRepository();

    private static final Logger logger = LoggerFactory.getLogger(AbstractMapperUtil.class);


}
