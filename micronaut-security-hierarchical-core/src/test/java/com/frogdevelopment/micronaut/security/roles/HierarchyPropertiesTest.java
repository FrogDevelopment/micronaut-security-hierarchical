package com.frogdevelopment.micronaut.security.roles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HierarchyPropertiesTest {

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
    void shouldThrowAnException_when_no_root() {
        // given
        var hierarchyProperties = new HierarchyProperties();
        var hierarchyRoles = """
                0 > 1
                2 > 3""";
        hierarchyProperties.setHierarchy(hierarchyRoles);

        // when
        var caught = catchThrowable(hierarchyProperties::getRootNode);

        // then
        assertThat(caught)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing root for hierarchy roles");
    }

    @Test
    void shouldReturnHierarchyAsTree() {
        // given
        var hierarchyProperties = new HierarchyProperties();
        hierarchyProperties.setHierarchy(HIERARCHY);

        // when
        var root = hierarchyProperties.getRootNode();

        // then
        var parents = root.children();
        assertThat(parents).hasSize(2);

        var node_0 = parents.getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0.key()).isEqualTo("0");
            softAssertions.assertThat(node_0.isLeaf()).isFalse();
            softAssertions.assertThat(node_0.children()).hasSize(3);
        });

        var node_0_0 = node_0.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0.key()).isEqualTo("0-0");
            softAssertions.assertThat(node_0_0.isLeaf()).isFalse();
            softAssertions.assertThat(node_0_0.children()).hasSize(3);
        });

        var node_0_0_0 = node_0_0.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0_0.key()).isEqualTo("0-0-0");
            softAssertions.assertThat(node_0_0_0.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_0_0.children()).isNull();
        });

        var node_0_0_1 = node_0_0.children().get(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0_1.key()).isEqualTo("0-0-1");
            softAssertions.assertThat(node_0_0_1.isLeaf()).isFalse();
            softAssertions.assertThat(node_0_0_1.children()).hasSize(2);
        });

        var node_0_0_1_0 = node_0_0_1.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0_1_0.key()).isEqualTo("0-0-1-0");
            softAssertions.assertThat(node_0_0_1_0.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_0_1_0.children()).isNull();
        });

        var node_0_0_1_1 = node_0_0_1.children().get(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0_1_1.key()).isEqualTo("0-0-1-1");
            softAssertions.assertThat(node_0_0_1_1.isLeaf()).isFalse();
            softAssertions.assertThat(node_0_0_1_1.children()).hasSize(1);
        });

        var node_0_0_1_1_1 = node_0_0_1_1.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0_1_1_1.key()).isEqualTo("0-0-1-1-1");
            softAssertions.assertThat(node_0_0_1_1_1.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_0_1_1_1.children()).isNull();
        });

        var node_0_0_2 = node_0_0.children().get(2);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_0_2.key()).isEqualTo("0-0-2");
            softAssertions.assertThat(node_0_0_2.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_0_2.children()).isNull();
        });

        var node_0_1 = node_0.children().get(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_1.key()).isEqualTo("0-1");
            softAssertions.assertThat(node_0_1.isLeaf()).isFalse();
            softAssertions.assertThat(node_0_1.children()).hasSize(2);
        });

        var node_0_1_0 = node_0_1.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_1_0.key()).isEqualTo("0-1-0");
            softAssertions.assertThat(node_0_1_0.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_1_0.children()).isNull();
        });

        var node_0_1_1 = node_0_1.children().get(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_1_1.key()).isEqualTo("0-1-1");
            softAssertions.assertThat(node_0_1_1.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_1_1.children()).isNull();
        });

        var node_0_2 = node_0.children().get(2);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_0_2.key()).isEqualTo("0-2");
            softAssertions.assertThat(node_0_2.isLeaf()).isTrue();
            softAssertions.assertThat(node_0_2.children()).isNull();
        });

        var node_1 = parents.get(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_1.key()).isEqualTo("1");
            softAssertions.assertThat(node_1.isLeaf()).isFalse();
            softAssertions.assertThat(node_1.children()).hasSize(1);
        });

        var node_1_0 = node_1.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_1_0.key()).isEqualTo("1-0");
            softAssertions.assertThat(node_1_0.isLeaf()).isFalse();
            softAssertions.assertThat(node_1_0.children()).hasSize(1);
        });

        var node_1_0_0 = node_1_0.children().getFirst();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(node_1_0_0.key()).isEqualTo("1-0-0");
            softAssertions.assertThat(node_1_0_0.isLeaf()).isTrue();
            softAssertions.assertThat(node_1_0_0.children()).isNull();
        });
    }

}
