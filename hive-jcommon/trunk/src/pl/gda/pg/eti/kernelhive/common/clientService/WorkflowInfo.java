package pl.gda.pg.eti.kernelhive.common.clientService;

public class WorkflowInfo {
	
	public enum WorkflowState {
		PENDING("pending"), COMPLETED("completed"), TERMINATED("terminated"), SUBMITTED(
				"submitted"), ERROR("error"), WARNING("warning"), PROCESSING(
				"processing");

		private String state;

		WorkflowState(String state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return state;
		}
	}
	
	public int ID;
	public String name;
	public WorkflowState state;
	public String result;
	

}
