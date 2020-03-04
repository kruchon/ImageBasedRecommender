package org.ius.gradcit.database.domain.relationship;

import org.ius.gradcit.database.domain.node.Image;
import org.ius.gradcit.database.domain.node.Thematics;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "RECOGNIZED_IN")
public class RecognizedIn {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Thematics thematics;

    @EndNode
    private Image image;

    @Property
    private float probability;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Thematics getThematics() {
        return thematics;
    }

    public void setThematics(Thematics thematics) {
        this.thematics = thematics;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
