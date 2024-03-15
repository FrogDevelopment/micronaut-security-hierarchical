package com.frogdevelopment.micronaut.security.roles;

import static java.util.Collections.emptyList;
import static java.util.stream.Stream.ofNullable;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.token.DefaultRolesFinder;
import io.micronaut.security.token.config.TokenConfiguration;

@Replaces(bean = DefaultRolesFinder.class)
public class HierarchyRolesFinder extends DefaultRolesFinder {

    @Getter(AccessLevel.PACKAGE)
    private final HierarchyNode root;

    public HierarchyRolesFinder(final TokenConfiguration tokenConfiguration,
                                final HierarchyNode root) {
        super(tokenConfiguration);
        this.root = root;
    }

    @Override
    public @NonNull List<String> resolveRoles(@Nullable Map<String, Object> attributes) {
        final var resolvedRoles = super.resolveRoles(attributes);
        if (resolvedRoles.isEmpty()) {
            return emptyList();
        }

        final var flatRoles = new ArrayList<String>();

        checkIfNodeHasRole(resolvedRoles, List.of(root), flatRoles);

        return Collections.unmodifiableList(flatRoles);
    }

    private static void checkIfNodeHasRole(final Collection<String> currentRoles, final Collection<HierarchyNode> nodes,
                                           final Collection<String> flatRoles) {
        ofNullable(nodes)
                .flatMap(Collection::stream)
                .forEach(node -> {
                    if (currentRoles.contains(node.key())) {
                        flatRoles.add(node.key());
                        addAllChildren(node.children(), flatRoles);
                    } else {
                        checkIfNodeHasRole(currentRoles, node.children(), flatRoles);
                    }
                });
    }

    private static void addAllChildren(final Collection<HierarchyNode> nodes, final Collection<String> flatRoles) {
        ofNullable(nodes)
                .flatMap(Collection::stream)
                .forEach(node -> {
                    flatRoles.add(node.key());
                    addAllChildren(node.children(), flatRoles);
                });
    }

}
