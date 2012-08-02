package pl.gda.pg.eti.kernelhive.gui.graph.execution;

import java.net.URL;

public class GraphExecution implements IGraphExecution {
	
	private URL inputDataUrl = null;
	private byte[] graphStream = null;
	private String username = null;
	private char[] password = null;
	
	@Override
	public void setInputDataUrl(URL url) {
		inputDataUrl = url;
	}

	@Override
	public void setSerializedGraphStream(byte[] stream) {
		graphStream = stream;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setPassword(char[] password) {
		this.password = password;
	}

	@Override
	public int execute() {
		if(inputDataUrl!=null&&graphStream!=null){
			
		}
		return 0;
	}
}
