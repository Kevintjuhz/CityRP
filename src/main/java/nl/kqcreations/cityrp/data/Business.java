package nl.kqcreations.cityrp.data;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Business {
	@Setter
	private String name;
	@Setter
	private String plotName = null;
	@Setter
	private BankAccount bankAccount = null;
	@Setter
	private UUID owner;
	@Setter
	private BusinessType businessType;

	private Map<UUID, Worker> workers = new HashMap<>();

	public Business(String name, UUID owner, BusinessType businessType) {
		this.name = name;
		this.owner = owner;
		this.businessType = businessType;
	}

	public void addWorker(UUID worker) {
		workers.putIfAbsent(worker, new Worker(worker));
	}

	public void addWorker(UUID worker, JobFunction jobFunction) {
		workers.putIfAbsent(worker, new Worker(worker, jobFunction));
	}

	public void removeWorker(UUID worker) {
		workers.remove(worker);
	}

	// ---------------------------
	// Custom Classes
	// ---------------------------

	@Getter
	public class Worker {

		private UUID uuid;

		@Setter
		private JobFunction function;

		@Setter
		private String customTitle = null;

		public Worker(UUID uuid) {
			this.uuid = uuid;
			this.function = JobFunction.EMPLOYEE;
		}

		public Worker(UUID uuid, JobFunction function) {
			this.uuid = uuid;
			this.function = function;
		}

		public void promote() {
			this.function = function.getNext();
		}

		public void demote() {
			this.function = function.getPrevious();
		}

	}

	// ---------------------------
	// Enums
	// ---------------------------

	public enum JobFunction {
		EMPLOYEE("Employee", "Empl"), MANAGER("Manager", "Mgr"), CFO("CFO", "CFO");

		@Getter
		private final String label;
		@Getter
		private final String shortLabel;

		JobFunction(String label, String shortLabel) {
			this.label = label;
			this.shortLabel = shortLabel;
		}

		public JobFunction getNext() {
			JobFunction[] levels = values();
			if (this.ordinal() == levels.length) {
				return this;
			}
			return levels[this.ordinal() + 1];
		}

		public JobFunction getPrevious() {
			JobFunction[] levels = values();
			if (this.ordinal() == 0) {
				return this;
			}
			return levels[this.ordinal() - 1];
		}
	}

	public enum BusinessType {
		FOOD_SHOP("Food Shop"),
		CLOTHING_SHOP("Clothing Shop"),
		ARCHITECT("Architect"),
		FIREWORKS_SHOP("Fireworks Shop"),
		CASINO("Casino"),
		ENTERTAINMENT("Entertainment");

		private final String label;

		BusinessType(String label) {
			this.label = label;
		}
	}
}
