package org.ius.gradcit.database.domain.node;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@NodeEntity
public class Image {

    public Image() {}

    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    @Property
    private String externalId;

    @Index
    @Property
    private Date whenPublicated;

    public Date getWhenPublicated() {
        return whenPublicated;
    }

    public void setWhenPublicated(Date whenPublicated) {
        this.whenPublicated = whenPublicated;
    }

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
}
