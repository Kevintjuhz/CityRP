package nl.kqcreations.cityrp.data.mongo_data.business;

import lombok.Getter;
import lombok.Setter;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccount;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Business {
	private String name;
	private String world;
	private int businessId;
	private String plotName = null;
	private BankAccount bankAccount = null;
	private UUID owner;
	private BusinessType businessType;

	private final Map<UUID, Worker> workers = new HashMap<>();

	public Business(int businessId, String name, BusinessType businessType, String world) {
		this.name = name;
		this.businessId = businessId;
		this.businessType = businessType;
		this.world = world;
	}

	public void addWorker(Player worker) {
		workers.putIfAbsent(worker.getUniqueId(), new Worker(worker));
	}

	public void addWorker(Player worker, JobFunction jobFunction) {
		workers.putIfAbsent(worker.getUniqueId(), new Worker(worker, jobFunction));
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
		private String name;

		@Setter
		private JobFunction function;

		@Setter
		private String customTitle = null;

		public Worker(Player worker) {
			this.uuid = worker.getUniqueId();
			this.name = worker.getName();
			this.function = JobFunction.EMPLOYEE;
		}

		public Worker(Player worker, JobFunction function) {
			this.uuid = worker.getUniqueId();
			this.name = worker.getName();
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

		BusinessType(String label) {
		}
	}
}
