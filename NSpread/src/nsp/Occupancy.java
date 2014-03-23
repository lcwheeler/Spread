package nsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.vividsolutions.jts.geom.Coordinate;

public class Occupancy implements Cloneable {

	private String name = "";
	private boolean infested = false;
	private boolean wasInfested = false;
	private long ageOfInfestation = 0;
	private long cumulativeAgeOfInfestation = 0;
	private int stageOfInfestation = 0;
	private int maxInfestation = 0;
	private double habitatSuitability = 1d;
	private Disperser disperser;
	private List<Coordinate> propagules = new ArrayList<Coordinate>();
	private Map<String, Long> controls = new TreeMap<String, Long>();
	private boolean NODATA = false;

	public Occupancy() {
	}

	public Occupancy(String name) {
		this.name = name;
	}

	public void addControl(String control) {
		if (!controls.containsKey(control)) {
			controls.put(control, 0l);
		}
	}

	public void clearInfestation() {
		this.infested = false;
		this.stageOfInfestation = 0;
		this.maxInfestation = 0;
	}

	public void clearPropagules() {
		propagules = new ArrayList<Coordinate>();
	}

	@Override
	public Occupancy clone() {
		Occupancy occ = new Occupancy();
		occ.ageOfInfestation = ageOfInfestation;
		occ.habitatSuitability = habitatSuitability;
		occ.disperser = disperser.clone();
		occ.infested = infested;
		occ.stageOfInfestation = stageOfInfestation;
		occ.maxInfestation = maxInfestation;
		occ.name = name;
		List<Coordinate> propagules_c = new ArrayList<Coordinate>();
		for (Coordinate c : propagules) {
			propagules_c.add((Coordinate) c.clone());
		}
		for (String s : controls.keySet()) {
			occ.controls.put(s, controls.get(s).longValue());
		}
		occ.propagules = propagules_c;
		return occ;
	}

	public void disperse() {
		try {
			propagules = disperser.disperse();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public long getAgeOfInfestation() {
		return ageOfInfestation;
	}

	public Map<String, Long> getControls() {
		return controls;
	}

	public long getControlTime(String control) {
		return controls.get(control);
	}

	public long getCumulativeAgeOfInfestation() {
		return cumulativeAgeOfInfestation;
	}

	public Disperser getDisperser() {
		return disperser;
	}

	public double getHabitatSuitability() {
		return habitatSuitability;
	}

	public int getMaxInfestation() {
		return maxInfestation;
	}

	public String getName() {
		return name;
	}

	public List<Coordinate> getPropagules() {
		return propagules;
	}

	public int getStageOfInfestation() {
		return stageOfInfestation;
	}

	public boolean hasControl(String control) {
		return controls.containsKey(control);
	}

	public boolean hasNoData() {
		return NODATA;
	}

	public void incrementInfestationTime(long increment) {
		if (infested) {
			ageOfInfestation += increment;
			cumulativeAgeOfInfestation += increment;
		}
	}

	public boolean isInfested() {
		return infested;
	}

	public void removeControl(String control) {
		controls.remove(control);
	}

	public void setAgeOfInfestation(long ageOfInfestation) {
		this.ageOfInfestation = ageOfInfestation;
	}

	public void setControlTime(String control, long controlTime) {
		controls.put(control, controlTime);
	}

	public void setDisperser(Disperser disperser) {
		this.disperser = disperser;
	}

	public void setHabitatSuitability(double habitatSuitability) {
		this.habitatSuitability = habitatSuitability;
	}

	public void setInfested(boolean infested) {
		this.infested = infested;

		if (infested) {
			wasInfested = true;
			this.stageOfInfestation = 1;
			this.maxInfestation = 1;
		} else {
			this.stageOfInfestation = 0;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNoData(boolean noData) {
		this.NODATA = noData;
	}

	public void setPropagules(List<Coordinate> propagules) {
		this.propagules = propagules;
	}

	public void setStageOfInfestation(int stageOfInfestation) {
		this.stageOfInfestation = stageOfInfestation;
		if (stageOfInfestation > maxInfestation) {
			maxInfestation = stageOfInfestation;
		}
	}

	public boolean wasInfested() {
		return wasInfested;
	}

}