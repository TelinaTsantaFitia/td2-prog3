import java.util.List;

public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    public Double getDishPrice() {
        if (ingredients == null) return 0.0;
        return ingredients.stream().mapToDouble(Ingredient::getPrice).sum();
    }
    // Getters and Setters...
    public List<Ingredient> findIngredients(int page, int size) {
        String sql = "SELECT * FROM ingredient LIMIT ? OFFSET ?";
        // Implementation using PreparedStatement...
    }

}


