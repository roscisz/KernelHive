package pl.gda.pg.eti.kernelhive.common.kernel.repository;

public enum KernelRoleEnum {
	MERGE("merge"), PARTITION("partition"), PROCESS("process");

	private String name;

	private KernelRoleEnum(final String name) {
		this.name = name;
	}

	public static KernelRoleEnum getRoleByName(final String name) {
		for (final KernelRoleEnum kre : KernelRoleEnum.values()) {
			if (kre.getName().equals(name)) {
				return kre;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

}
