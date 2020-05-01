package nl.kqcreations.cityrp.data.mongo_data.business;

import lombok.Getter;

@Getter
public class Job {

	private Business.JobFunction function;
	private Integer businessId;

	public Job(Business.JobFunction function, Integer businessId) {
		this.function = function;
		this.businessId = businessId;
	}

}
