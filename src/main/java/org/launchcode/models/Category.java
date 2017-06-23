package org.launchcode.models;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 6/19/2017.
 */

@Entity
public class Category {

    @GeneratedValue
    @Id
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Cheese> cheeses = new ArrayList<Cheese>();

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<Cheese> getCheese() {
        return cheeses;
    }

    public void setCheese(List<Cheese> cheeses) {
        this.cheeses = cheeses;
    }

    public void addCheese(Cheese newCheese) {
        this.cheeses.add(newCheese);
    }
}
