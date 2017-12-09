package com.bedrin.models.dto;

import java.util.Arrays;
import java.util.List;

import com.bedrin.models.pojo.Addon;

public class AddonsSetDTO {
	private List<Addon> set;

	public List<Addon> getSet() {
		return set;
	}

	public void setSet(List<Addon> set) {
		this.set = set;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(set.toArray());
	}

}
