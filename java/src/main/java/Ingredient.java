public class Ingredient {
    private int id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private Double requiredQuantity; // AJOUTÉ : pour la nouvelle colonne
    private Dish dish;

    // Constructeurs
    public Ingredient() {}

    // Getters et Setters
    public Double getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(Double requiredQuantity) { this.requiredQuantity = requiredQuantity; }

    public Double getPrice() { return price; }
    // ... autres getters/setters ...

    public String getDishName() {
        return (dish != null) ? dish.getName() : "No Dish";
    }
}