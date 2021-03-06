/*******************************************************************************
 * Copyright Charles Darwin University 2014. All Rights Reserved.  
 * For review only, not for distribution.
 *******************************************************************************/
package spread.impl.process;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import spread.Mosaic;
import spread.Infestation;
import spread.Patch;
import spread.Process;

import spread.util.ControlType;

/**
 * Performs operations on a Mosaic pertaining to growth. Chiefly, increments the
 * age of infestation, and adjusts the level of infestation.
 * 
 */

public class Process_Growth implements Process, Cloneable {

	private Map<String, long[]> thresholds = new TreeMap<String, long[]>();
	private long timeIncrement = 1;

	/**
	 * Returns a clone/copy of the instance
	 */

	@Override
	public Process_Growth clone() {
		Process_Growth pg = new Process_Growth();
		Map<String, long[]> c_thresholds = new TreeMap<String, long[]>();
		Iterator<String> it = thresholds.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			c_thresholds.put(key, Arrays.copyOf(thresholds.get(key),
					thresholds.get(key).length));
		}

		pg.timeIncrement = timeIncrement;
		return pg;
	}

	/**
	 * Processes all patches in the Mosaic
	 */

	@Override
	public void process(Mosaic mosaic) {
		for (Integer key : mosaic.getPatches().keySet()) {
			process(mosaic.getPatches().get(key));
		}
	}

	/**
	 * Increments the infestation time of the patch (if infested) and adjusts
	 * their level of infestation correspondingly.
	 * 
	 * @param patch
	 *            - The patch to be processed
	 */

	private void process(Patch patch) {

		Iterator<String> it = patch.getInfestation().keySet().iterator();
		while (it.hasNext()) {

			String species = it.next();

			long[] th = Arrays.copyOf(thresholds.get(species),
					thresholds.get(species).length + 1);
			th[th.length - 1] = Long.MAX_VALUE;
			Infestation o = patch.getInfestation(species);

			o.incrementInfestationTime(timeIncrement);

			if (o.isInfested()
					&& !patch.hasControl(ControlType.CONTAINMENT)
					&& !patch.hasControl(ControlType.GROUND_CONTROL, species)
					&& !patch.hasControl(ControlType.CONTAINMENT_CORE_CONTROL,
							species)) {

				for (int j = 0; j < th.length; j++) {

					long aoi = o.getAgeOfInfestation();
					int ct = 0;

					while (aoi > th[ct]) {
						ct++;
					}

					o.setStageOfInfestation(ct + 1);
				}
			}
		}
	}

	/**
	 * Returns the amount of time currently being incremented by the growth
	 * process
	 * 
	 * @return - the amount of time currently being incremented by the growth
	 *         process
	 */

	public long getTimeIncrement() {
		return timeIncrement;
	}

	/**
	 * Sets the age thresholds defining the boundaries between different growth
	 * stages
	 * 
	 * @param thresholds
	 */

	public void setThresholds(Map<String, long[]> thresholds) {
		this.thresholds = thresholds;
	}

	/**
	 * Sets the amount of time currently being incremented by the growth process
	 * 
	 * @param timeIncrement
	 */

	public void setTimeIncrement(long timeIncrement) {
		this.timeIncrement = timeIncrement;
	}

	/**
	 * Resets the process
	 */

	@Override
	public void reset() {
	}
}