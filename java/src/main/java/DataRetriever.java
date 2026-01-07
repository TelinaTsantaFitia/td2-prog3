import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public List<Ingredient> findIngredientsByCriteria(String name, CategoryEnum category, String dishName, int page, int size) {
        List<Ingredient> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.* FROM ingredient i LEFT JOIN dish d ON i.id_dish = d.id WHERE 1=1 "
        );

        // Construction dynamique de la requête
        if (name != null) sql.append("AND i.name ILIKE ? ");
        if (category != null) sql.append("AND i.category = ?::category_enum ");
        if (dishName != null) sql.append("AND d.name ILIKE ? ");
        sql.append("LIMIT ? OFFSET ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // Remplissage dynamique des points d'interrogation (?)
            if (name != null) pstmt.setString(paramIndex++, "%" + name + "%");
            if (category != null) pstmt.setString(paramIndex++, category.name());
            if (dishName != null) pstmt.setString(paramIndex++, "%" + dishName + "%");

            pstmt.setInt(paramIndex++, size);
            pstmt.setInt(paramIndex++, (page - 1) * size);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                // Ne pas oublier la nouvelle colonne !
                ing.setRequiredQuantity((Double) rs.getObject("required_quantity"));
                results.add(ing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
