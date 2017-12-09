package com.bedrin.models.pojo;

public class Addon implements java.io.Serializable {

	private static final long serialVersionUID = -3226913180196270998L;
	
	private String name;
	private String description;
	private boolean enabled;
	private int cost;
	private long id;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Addon)) {
			return false;
		} else {
			if (((Addon)obj).getId() == this.getId()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public String toString() {
		return "[" + getId() + "]: " + getName() + " : " + getCost() + " : " + isEnabled();
	}
	
}
