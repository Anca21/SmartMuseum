/**
 * 
 */
package kth.id2209.curator;

/**
 * @author pradeeppeiris
 *
 */
public interface CuratorGui {
	/**
	 * Set Profiler Agent
	 * @param agent
	 * 			Profiler Agent
	 */			
	void setAgent(CuratorAgent agent);

	/**
	 * Show GUI
	 */
	void show();
	
	/**
	 * Update Tour suggestions
	 */
	void addArtifact();
}
