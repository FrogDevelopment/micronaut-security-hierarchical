package com.frogdevelopment.micronaut.security.roles;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.security.config.SecurityConfigurationProperties;

@Data
@ConfigurationProperties(HierarchyProperties.PREFIX)
public class HierarchyProperties {

    public static final String PREFIX = SecurityConfigurationProperties.PREFIX + ".hierarchy";

    public static final String DEFAULT_HIERARCHY_SEPARATOR = ">";
    public static final String DEFAULT_LINE_BREAK = "\n";

    private String hierarchySeparator = DEFAULT_HIERARCHY_SEPARATOR;
    private String lineBreak = DEFAULT_LINE_BREAK;
    private String representation;

    public HierarchyNode getRootNode() {
        final var hierarchyPattern = Pattern.compile("\\s*%s\\s*".formatted(hierarchySeparator));

        final var notRoots = new HashSet<String>();
        final var mapHierarchies = new HashMap<String, List<String>>();
        Stream.of(representation.split(lineBreak))
                .map(String::trim)
                .map(hierarchyPattern::split)
                .forEach(roles -> {
                    for (var i = 1; i < roles.length; i++) {
                        final var higherRole = roles[i - 1];
                        final var lowerRole = roles[i];

                        notRoots.add(lowerRole);
                        mapHierarchies.computeIfAbsent(higherRole, k -> new ArrayList<>()).add(lowerRole);
                    }
                });

        final var roots = new ArrayList<>(mapHierarchies.keySet());
        roots.removeAll(notRoots);
        if (roots.size() != 1) {
            throw new IllegalArgumentException("Missing root for hierarchy roles");
        }

        final var mapNodes = new HashMap<String, HierarchyNode.Builder>();
        mapHierarchies.forEach((keyParent, childrenKeys) -> {
            final var parent = mapNodes.computeIfAbsent(keyParent, HierarchyNode::builder);
            childrenKeys.stream()
                    .map(keyChild -> mapNodes.computeIfAbsent(keyChild, HierarchyNode::builder))
                    .forEach(parent::addChild);
        });

        return mapNodes.get(roots.getFirst()).build();
    }
}
