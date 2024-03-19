package com.frogdevelopment.micronaut.security.roles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
        hierarchyProperties.setRepresentation(HIERARCHY);
        // fixme create data directly
        var rootNode = HierarchyUtils.getRootNode(hierarchyProperties);

        var rolesFinder = new HierarchyRolesFinder(tokenConfiguration, rootNode);
        var roles = List.of("0-1", "1");

        // when
        var allRoles = rolesFinder.getEffectiveRoles(roles);

        // then
        assertThat(allRoles).containsExactlyInAnyOrder("0-1", "0-1-0", "0-1-1", "1", "1-0", "1-0-0");
    }

}
