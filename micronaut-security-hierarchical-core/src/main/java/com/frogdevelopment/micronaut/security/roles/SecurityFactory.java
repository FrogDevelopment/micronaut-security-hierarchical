package com.frogdevelopment.micronaut.security.roles;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.security.token.DefaultRolesFinder;
import io.micronaut.security.token.RolesFinder;
import io.micronaut.security.token.config.TokenConfiguration;

@Factory
public class SecurityFactory {

    @Context
    @Replaces(bean = DefaultRolesFinder.class)
    RolesFinder hierarchyRolesFinder(final TokenConfiguration tokenConfiguration, final HierarchyProperties hierarchyProperties) {
        return new HierarchyRolesFinder(tokenConfiguration, HierarchyUtils.getRootNode(hierarchyProperties));
    }
}
