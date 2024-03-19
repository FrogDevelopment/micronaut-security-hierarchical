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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.token.DefaultRolesFinder;
import io.micronaut.security.token.config.TokenConfiguration;

public class HierarchyRolesFinder extends DefaultRolesFinder {

    @Getter(AccessLevel.PACKAGE)
    private final HierarchyNode root;

    public HierarchyRolesFinder(final TokenConfiguration tokenConfiguration, final HierarchyNode root) {
        super(tokenConfiguration);
        this.root = root;
    }

    @Override
    public @NonNull List<String> resolveRoles(@Nullable final Map<String, Object> attributes) {
        final var resolvedRoles = super.resolveRoles(attributes);

        return getEffectiveRoles(resolvedRoles);
    }

    @NonNull
//    @VisibleForTesting
    List<String> getEffectiveRoles(final Collection<String> grantedRoles) {
        if (grantedRoles.isEmpty()) {
            return emptyList();
        }

        final var effectiveRoles = new ArrayList<String>();

        checkIfNodeHasRole(grantedRoles, List.of(root), effectiveRoles);

        return Collections.unmodifiableList(effectiveRoles);
    }

    private static void checkIfNodeHasRole(final Collection<String> currentRoles, final List<HierarchyNode> nodes,
            final List<String> effectiveRoles) {
        ofNullable(nodes)
                .flatMap(Collection::stream)
                .forEach(node -> {
                    if (currentRoles.contains(node.key())) {
                        effectiveRoles.add(node.key());
                        addAllChildren(node.children(), effectiveRoles);
                    } else {
                        checkIfNodeHasRole(currentRoles, node.children(), effectiveRoles);
                    }
                });
    }

    private static void addAllChildren(final List<HierarchyNode> nodes, final List<String> flatRoles) {
        ofNullable(nodes)
                .flatMap(List::stream)
                .forEach(node -> {
                    flatRoles.add(node.key());
                    addAllChildren(node.children(), flatRoles);
                });
    }

}
