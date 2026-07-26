package com.mindflow.domain.relation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class KnowledgeRelationTest {

    @Test
    void createsRelationSuccessfully() {
        UUID sourceId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();

        KnowledgeRelation relation = KnowledgeRelation.create(
                sourceId,
                RelationType.USES,
                targetId,
                0.85,
                UUID.randomUUID()
        );

        assertNotNull(relation.id());
        assertEquals(sourceId, relation.sourceId());
        assertEquals(targetId, relation.targetId());
        assertEquals(RelationType.USES, relation.type());
        assertEquals(0.85, relation.confidence());
        assertNotNull(relation.createdBy());
        assertNotNull(relation.createdAt());
    }

    @Test
    void rejectsMissingRelationType() {
        assertThrows(
                NullPointerException.class,
                () -> KnowledgeRelation.create(UUID.randomUUID(), null, UUID.randomUUID())
        );
    }

    @Test
    void rejectsSelfRelation() {
        UUID objectId = UUID.randomUUID();

        assertThrows(
                IllegalArgumentException.class,
                () -> KnowledgeRelation.create(objectId, RelationType.RELATED_TO, objectId)
        );
    }

    @Test
    void rejectsConfidenceOutsideRange() {
        KnowledgeRelation relation = KnowledgeRelation.create(
                UUID.randomUUID(),
                RelationType.PART_OF,
                UUID.randomUUID()
        );

        assertThrows(IllegalArgumentException.class, () -> relation.updateConfidence(-0.01));
        assertThrows(IllegalArgumentException.class, () -> relation.updateConfidence(1.01));
    }
}
