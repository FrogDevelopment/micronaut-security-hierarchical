package com.frogdevelopment.micronaut.security.roles;

import lombok.Data;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.security.config.SecurityConfigurationProperties;

@Data
@ConfigurationProperties(HierarchyProperties.PREFIX)
public class HierarchyProperties {

    public static final String PREFIX = SecurityConfigurationProperties.PREFIX + ".hierarchy";

    public static final Character DEFAULT_ROLE_SEPARATOR = '>';
    public static final String DEFAULT_LINE_BREAK = System.lineSeparator();

    private Character roleSeparator = DEFAULT_ROLE_SEPARATOR;
    private String lineBreak = DEFAULT_LINE_BREAK;
    private String representation;

}
