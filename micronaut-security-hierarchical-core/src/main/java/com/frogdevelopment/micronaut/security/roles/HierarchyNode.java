package com.frogdevelopment.micronaut.security.roles;

import java.util.ArrayList;
import java.util.List;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record HierarchyNode(String key, List<HierarchyNode> children) {

    static Builder builder(final String key) {
        return new Builder(key);
    }

    static class Builder {
        private final String key;
        private List<Builder> childrenBuilder;

        private Builder(final String key) {
            this.key = key;
        }

        void addChild(final Builder child) {
            if (this.childrenBuilder == null) {
                this.childrenBuilder = new ArrayList<>();
            }
            this.childrenBuilder.add(child);
        }

        HierarchyNode build() {
            if (CollectionUtils.isEmpty(childrenBuilder)) {
                return new HierarchyNode(key, null);
            } else {
                var children = childrenBuilder.stream()
                        .map(Builder::build)
                        .toList();
                return new HierarchyNode(key, children);
            }
        }
    }
}
