package myProject.voting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {


    @Column(name = "name")
    @NotBlank
    private String name;

/*   @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
   private List<Dish> dishes;*/

    public Restaurant() {
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                '}';
    }
}
