package org.ius.gradcit.database.domain.relationship;

import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.domain.node.User;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "HAS_INTEREST")
public class HasInterest {

    @Id
    Long id;

    @StartNode
    private User user;

    @EndNode
    private Thematics interest;

    @Property
    private float weight;

    @Property
    private float interestWeight;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getInterestWeight() {
        return interestWeight;
    }

    public void setInterestWeight(float interestWeight) {
        this.interestWeight = interestWeight;
    }
}
