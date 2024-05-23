package communicationFramework.config;


/**
 * 
 * @author Vasilije Mirkovic
 *
 */

public class MSConfig {
	private String name;
	private int portStartRange;
	private int portEndRange;
	
	/**
	 * 
	 * @param name
	 * @param portStartRange
	 * @param portEndRange
	 */
	public MSConfig(String name, int portStartRange, int portEndRange) {
		super();
		this.name = name;
		this.portStartRange = portStartRange;
		this.portEndRange = portEndRange;
	}
	
	/**
	 * 
	 * @return name of the Peer that is used in the properties file
	 */
	

	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return the minimum port of the Peer
	 */
	public int getPortStartRange() {
		return portStartRange;
	}
	/**
	 * 
	 * @param portStartRange
	 */
	public void setPortStartRange(int portStartRange) {
		this.portStartRange = portStartRange;
	}
	/**
	 * 
	 * @return the maximum port of the Peer
	 */
	public int getPortEndRange() {
		return portEndRange;
	}
	/**
	 * 
	 * @param portEndRange
	 */
	public void setPortEndRange(int portEndRange) {
		this.portEndRange = portEndRange;
	}	
}
