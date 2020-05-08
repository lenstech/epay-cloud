package com.lens.epay.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Emir Gökdemir
 * on 7 May 2020
 */
@ConfigurationProperties(prefix = "iyzico")
@Setter
@Getter
public class IyzicoConfig {

    public static final String locale = "tr";
    public static final String currency = "TL";
}
