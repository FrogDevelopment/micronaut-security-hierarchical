package com.frogdevelopment.micronaut.security.roles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HierarchyUtils {

    // example ROLE_A[ > ]ROLE_B -> [\s*>\s*]
    private static final String HIERARCHY_FORMAT = "\\s*%s\\s*";

    static HierarchyNode getRootNode(final HierarchyProperties properties) {
        final var hierarchyPattern = Pattern.compile(HIERARCHY_FORMAT.formatted(properties.getRoleSeparator()));

        final var notRoots = new HashSet<String>();
        final var nodesByRole = new HashMap<String, HierarchyNode.Builder>();
        Stream.of(properties.getRepresentation().split(properties.getLineBreak()))
                .map(String::trim)
                .map(hierarchyPattern::split)
                .forEach(roles -> {
                    for (var i = 0; i < roles.length - 1; i++) {
                        final var parentRole = roles[i];
                        final var childRole = roles[i + 1];

                        notRoots.add(childRole);

                        final var parentNode = nodesByRole.computeIfAbsent(parentRole, HierarchyNode::builder);
                        final var childNode = nodesByRole.computeIfAbsent(childRole, HierarchyNode::builder);
                        parentNode.addChild(childNode);
                    }
                });

        nodesByRole.keySet().removeAll(notRoots);
        if (nodesByRole.size() != 1) {
            throw new IllegalStateException("Missing root for hierarchy roles");
        }

        // todo find a better way ?
        return nodesByRole.values().stream().toList().getFirst().build();
    }
}
