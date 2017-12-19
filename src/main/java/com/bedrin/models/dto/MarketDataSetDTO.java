package com.bedrin.models.dto;

import java.util.ArrayList;
import java.util.Arrays;

public class MarketDataSetDTO {

	private ArrayList<MarketDataUnitDTO> set;

	public MarketDataSetDTO(MarketDataUnitDTO... dtos) {
		setSet(new ArrayList<MarketDataUnitDTO>());
		getSet().addAll(Arrays.<MarketDataUnitDTO>asList(dtos));
	}
	
	public ArrayList<MarketDataUnitDTO> getSet() {
		return set;
	}

	public void setSet(ArrayList<MarketDataUnitDTO> set) {
		this.set = set;
	}
	
}
