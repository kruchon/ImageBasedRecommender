package org.ius.gradcit.database.domain.node;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "INTERESTED_IN", direction = Relationship.OUTGOING)
    private List<Thematics> interest = new ArrayList<>();

    @Property
    @Index(unique = true)
    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Thematics> getInterest() {
        return interest;
    }

    public void setInterest(List<Thematics> interest) {
        this.interest = interest;
    }
}
