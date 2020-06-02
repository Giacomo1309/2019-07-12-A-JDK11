package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods() {
		String sql = "SELECT * FROM food";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Food> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"), res.getString("display_name")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Condiment> listAllCondiments() {
		String sql = "SELECT * FROM condiment";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Condiment> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"), res.getString("display_name"),
							res.getDouble("condiment_calories"), res.getDouble("condiment_saturated_fats")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Portion> listAllPortions() {
		String sql = "SELECT * FROM `portion`";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Portion> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"), res.getDouble("portion_amount"),
							res.getString("portion_display_name"), res.getDouble("calories"),
							res.getDouble("saturated_fats"), res.getInt("food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void getCibi(Map<Integer, Food> idMap, int porzioni) {
		String sql = "SELECT portion.food_code, display_name " + "FROM `portion`, food "
				+ "WHERE food.food_code = `portion`.food_code " + "GROUP BY food_code " + "HAVING COUNT(*) = ? ORDER BY display_name   ";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, porzioni);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					Integer codiceCibo = res.getInt("portion.food_code");
					String descrizioneCibo = res.getString("display_name");
					if (!idMap.containsKey(codiceCibo)) {
						Food f = new Food(codiceCibo, descrizioneCibo);
						idMap.put(codiceCibo, f);
					}

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public List<Adiacenza> getAdiacenze(Map<Integer, Food> idMap) {
		String sql = "SELECT f1.food_code, f2.food_code,AVG(condiment.condiment_calories) AS PESO " + 
				"FROM food_condiment AS f1, food_condiment AS f2, condiment " + 
				"WHERE f1.condiment_code = f2.condiment_code AND f1.food_code > f2.food_code AND f1.condiment_code= condiment.condiment_code "+ 
				"GROUP BY f1.food_code, f2.food_code";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Adiacenza> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					int c1  = res.getInt("f1.food_code");
					int c2 = res.getInt("f2.food_code");
					Double peso = res.getDouble("PESO");
					if(idMap.containsKey(c1) && idMap.containsKey(c2)) {
						Adiacenza a = new Adiacenza(idMap.get(c1),idMap.get(c2),peso);
						list.add(a);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		
	}
}
