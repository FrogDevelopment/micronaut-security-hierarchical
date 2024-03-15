package com.frogdevelopment.micronaut.security.roles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.micronaut.security.token.config.TokenConfigurationProperties;

@ExtendWith(MockitoExtension.class)
class HierarchyRolesFinderTest {

    private static final String HIERARCHY = """
            root > 0
            root > 1
            0 > 0-0
            0-0 > 0-0-0
            0-0 > 0-0-1
            0-0-1 > 0-0-1-0
            0-0-1 > 0-0-1-1 > 0-0-1-1-1
            0-0 > 0-0-2
            0 > 0-1
            0-1 > 0-1-0
            0-1 > 0-1-1
            0 > 0-2
            1 > 1-0 > 1-0-0""";

    @Test
    void shouldReturnOnlySelectedAndChildrenRoles() {
        // given
        var tokenConfiguration = new TokenConfigurationProperties();
        var hierarchyProperties = new HierarchyProperties();
        hierarchyProperties.setHierarchy(HIERARCHY);
        var rootNode = hierarchyProperties.getRootNode();

        var rolesFinder = new HierarchyRolesFinder(tokenConfiguration, rootNode);
        var attributes = Map.<String, Object>of("roles", List.of("0-1", "1"));

        // when
        var allRoles = rolesFinder.resolveRoles(attributes);

        // then
        assertThat(allRoles).containsExactlyInAnyOrder("0-1", "0-1-0", "0-1-1", "1", "1-0", "1-0-0");
    }

}
