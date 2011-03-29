package org.alfresco.samples.restbinding.connector;

public class Filter {

	private String filterData;
	private String filterId;
	
	
	public Filter(String filterData, String filterId) {
		super();
		this.filterData = filterData;
		this.filterId = filterId;
	}

	public String getFilterData() {
		return filterData;
	}
	
	public void setFilterData(String filterData) {
		this.filterData = filterData;
	}
	
	public String getFilterId() {
		return filterId;
	}
	
	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}
	
}
