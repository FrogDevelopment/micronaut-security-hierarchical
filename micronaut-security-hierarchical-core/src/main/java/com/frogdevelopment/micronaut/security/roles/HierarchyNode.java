package com.frogdevelopment.micronaut.security.roles;

import java.util.ArrayList;
import java.util.List;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record HierarchyNode(String key,
                            Boolean isLeaf,
                            List<HierarchyNode> children) {

    public static Builder builder(final String key) {
        return new Builder(key);
    }

    public static class Builder {
        private final String key;
        private List<Builder> childrenBuilder;

        private Builder(String key) {
            this.key = key;
        }

        public void addChild(Builder child) {
            if (this.childrenBuilder == null) {
                this.childrenBuilder = new ArrayList<>();
            }
            this.childrenBuilder.add(child);
        }

        public HierarchyNode build() {
            if (CollectionUtils.isEmpty(childrenBuilder)) {
                return new HierarchyNode(key, true, null);
            } else {
                var children = childrenBuilder.stream()
                        .map(Builder::build)
                        .toList();
                return new HierarchyNode(key, false, children);
            }
        }
    }
}
