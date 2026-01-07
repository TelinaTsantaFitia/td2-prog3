import java.util.List;


public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum type;
    private List<Ingredient> ingredients;


    public Double getDishPrice() {
        return ingredients.stream()
                .mapToDouble(Ingredient::getPrice)
                .sum();
    }


// getters & setters
}