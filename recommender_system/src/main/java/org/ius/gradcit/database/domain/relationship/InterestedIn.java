package org.ius.gradcit.database.domain.relationship;

import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.domain.node.User;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "INTERESTED_IN")
public class InterestedIn {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private User user;

    @EndNode
    private Thematics thematics;

    @Property
    private float interestDegree;

    public float getInterestDegree() {
        return interestDegree;
    }

    public void setInterestDegree(float interestDegree) {
        this.interestDegree = interestDegree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Thematics getThematics() {
        return thematics;
    }

    public void setThematics(Thematics thematics) {
        this.thematics = thematics;
    }
}
