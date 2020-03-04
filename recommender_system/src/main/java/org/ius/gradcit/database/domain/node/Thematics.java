package org.ius.gradcit.database.domain.node;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity(label = "Token")
public class Thematics {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "NEAREST_TO", direction = Relationship.UNDIRECTED)
    private List<Thematics> nearest;

    @Relationship(type = "RECOGNIZED_IN", direction = Relationship.OUTGOING)
    private List<Image> images = new ArrayList<>();

    @Property
    private List<Float> embedding;

    @Index(unique = true)
    @Property
    private String word;

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Thematics> getNearest() {
        return nearest;
    }

    public void setNearest(List<Thematics> nearest) {
        this.nearest = nearest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
