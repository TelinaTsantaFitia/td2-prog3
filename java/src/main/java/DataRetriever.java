import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    /* =======================
       CONSTANTES SQL
       ======================= */

    private static final String FIND_DISH_BY_ID =
            "SELECT d.id d_id, d.name d_name, d.type, " +
                    "i.id i_id, i.name i_name, i.price, i.category " +
                    "FROM dish d LEFT JOIN ingredient i ON d.id = i.dish_id " +
                    "WHERE d.id = ?";

    private static final String FIND_INGREDIENTS_PAGINATED =
            "SELECT * FROM ingredient LIMIT ? OFFSET ?";

    private static final String CHECK_INGREDIENT_EXISTS =
            "SELECT COUNT(*) FROM ingredient WHERE name = ?";

    private static final String INSERT_INGREDIENT =
            "INSERT INTO ingredient(name, price, category, dish_id) VALUES (?,?,?,?)";

    /* =======================
       a) Dish + ingrédients
       ======================= */

    public Dish findDishById(Integer id) {
        try (Connection con = DBConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(FIND_DISH_BY_ID)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            Dish dish = null;
            List<Ingredient> ingredients = new ArrayList<>();

            while (rs.next()) {
                if (dish == null) {
                    dish = mapDish(rs);
                }

                Ingredient ingredient = mapIngredient(rs, dish);
                if (ingredient != null) {
                    ingredients.add(ingredient);
                }
            }

            if (dish != null) {
                dish.setIngredients(ingredients);
            }
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* =======================
       b) Pagination ingrédients
       ======================= */

    public List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection con = DBConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(FIND_INGREDIENTS_PAGINATED)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ingredients.add(mapIngredient(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    /* =======================
       c) Création avec atomicité
       ======================= */

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {

        try (Connection con = DBConnection.getDBConnection()) {
            con.setAutoCommit(false);

            try {
                checkIngredientsExistence(con, newIngredients);
                insertIngredients(con, newIngredients);
                con.commit();
                return newIngredients;

            } catch (Exception e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* =======================
       MÉTHODES PRIVÉES
       ======================= */

    private Dish mapDish(ResultSet rs) throws SQLException {
        Dish dish = new Dish();
        dish.setId(rs.getInt("d_id"));
        dish.setName(rs.getString("d_name"));
        dish.setType(DishTypeEnum.valueOf(rs.getString("type")));
        return dish;
    }

    private Ingredient mapIngredient(ResultSet rs, Dish dish) throws SQLException {
        if (rs.getInt("i_id") == 0) return null;

        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getInt("i_id"));
        ingredient.setName(rs.getString("i_name"));
        ingredient.setPrice(rs.getDouble("price"));
        ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
        ingredient.setDish(dish);
        return ingredient;
    }

    private Ingredient mapIngredient(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getInt("id"));
        ingredient.setName(rs.getString("name"));
        ingredient.setPrice(rs.getDouble("price"));
        ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
        return ingredient;
    }

    private void checkIngredientsExistence(Connection con, List<Ingredient> ingredients)
            throws SQLException {

        try (PreparedStatement ps = con.prepareStatement(CHECK_INGREDIENT_EXISTS)) {
            for (Ingredient i : ingredients) {
                ps.setString(1, i.getName());
                ResultSet rs = ps.executeQuery();
                rs.next();

                if (rs.getInt(1) > 0) {
                    throw new RuntimeException("Ingredient déjà existant : " + i.getName());
                }
            }
        }
    }

    private void insertIngredients(Connection con, List<Ingredient> ingredients)
            throws SQLException {

        try (PreparedStatement ps = con.prepareStatement(INSERT_INGREDIENT)) {
            for (Ingredient i : ingredients) {
                ps.setString(1, i.getName());
                ps.setDouble(2, i.getPrice());
                ps.setString(3, i.getCategory().name());
                ps.setInt(4, i.getDish().getId());
                ps.executeUpdate();
            }
        }
    }
}
