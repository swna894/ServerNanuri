package com.order2david.shop.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.order2david.shop.model.IsShow;
import com.order2david.shop.model.Shop;
import com.order2david.supplier.model.Supplier;
import com.order2david.supplier.repository.SupplierRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShopJdbcRepository {

	@Autowired
	private SupplierRepository supplierRepository;

	private final JdbcTemplate jdbcTemplate;

	public void execute(String sql) {
		this.jdbcTemplate.execute(sql);
	}

	public void initialAbbrColumn() {
		List<Supplier> suppliers = supplierRepository.findAll();

		for (Supplier supplier : suppliers) {
			String SQL = "ALTER TABLE shop ADD " + supplier.getAbbr() + " bit NOT NULL DEFAULT true ";
			execute(SQL);
		}
	}

	public void newColumn(Supplier supplier) {	
		String SQL = "ALTER TABLE shop ADD " + supplier.getAbbr() + " bit NOT NULL DEFAULT true ";
		execute(SQL);
	}
	
	public void deleteColumn(Supplier supplier) {	
		String SQL = "ALTER TABLE shop DROP " + supplier.getAbbr();
		execute(SQL);
	}
	
	
	public void postColumns(List<Shop> shops) {		
		String company = null;
		for (Shop shop : shops) {
			String SQL = "update shop set ";
			List<IsShow> isShows = shop.getIsShow();
			for (int i = 0; i < isShows.size(); i++) {
				IsShow isShow = isShows.get(i);
				SQL = SQL + isShow.getAbbr() + " =  " + isShow.getIs();
				company = isShow.getShop();
				if (i < isShows.size() - 1)
					SQL = SQL + ", ";
			}
			SQL = SQL + " where company = '" + company + "'";
			jdbcTemplate.update(SQL);
		}
	}

	public Map<String, List<IsShow>> findAllAbbrs() {
		String SQL = "SELECT * FROM shop";
		List<Supplier> suppliers = supplierRepository.findAll();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);

		Map<String, List<IsShow>> maps = new HashMap<>();
		for (Map<String, Object> row : rows) {
			List<IsShow> isShows = new ArrayList<IsShow>();
			IsShow isShow;
			String company = null;
			for (Supplier supplier : suppliers) {
				isShow = new IsShow();
				company = (String) row.get("company");
				String abbr = supplier.getAbbr();
				isShow.setAbbr(abbr);
				isShow.setShop(company);
				isShow.setIs((Boolean) row.get(abbr));
				isShows.add(isShow);
			}
			maps.put(company, isShows);
		}
		return maps;
	}

}
