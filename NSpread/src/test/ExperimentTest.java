package test;

//import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nsp.Experiment;
import nsp.MosaicWriter;
import nsp.Process;
import nsp.RandomGenerator;
import nsp.impl.Disperser_Continuous2D;
import nsp.impl.RasterMosaic;
import nsp.impl.output.MosaicWriter_Raster;
import nsp.impl.process.Process_Dispersal;
import nsp.impl.process.Process_Growth;
import nsp.impl.process.Process_Infestation;
import nsp.impl.process.Process_ManageAge;
import nsp.impl.random.RandomGenerator_Determined;

import org.junit.Before;
import org.junit.Test;

public class ExperimentTest {

	Experiment exp = new Experiment();
	RasterMosaic rm = new RasterMosaic();
	Disperser_Continuous2D d2;
	List<Process> processes;
	MosaicWriter ow;
	String species = "Test_1";

	@Before
	public void setup() {

		List<String> speciesList = new ArrayList<String>();
		speciesList.add("Test_1");
		speciesList.add("Test_2");
		rm.setSpeciesList(speciesList);

		try {
			rm.setPresenceMap("./resource files/test.txt", species);
		} catch (IOException e) {
			e.printStackTrace();
		}

		d2 = new Disperser_Continuous2D();
		RandomGenerator east = new RandomGenerator_Determined(0);
		// RandomGenerator north = new RandomGenerator_Determined(.25);
		// RandomGenerator west = new RandomGenerator_Determined(.5);
		// RandomGenerator south = new RandomGenerator_Determined(.75);
		RandomGenerator one = new RandomGenerator_Determined(1);
		ow = new MosaicWriter_Raster();
		ow.setFolder("C:/Temp/Rasters/Test");
		ow.setWriteHeader(false);
		d2.setDistanceGenerator(one);
		d2.setAngleGenerator(east);
		d2.setNumberGenerator(one);

		Map<String, long[]> thresholds = new TreeMap<String, long[]>();
		thresholds.put("Test_1", new long[] { 5, 8 });
		thresholds.put("Test_2", new long[] { 4, 9 });

		Process_Growth pg = new Process_Growth();
		pg.setThresholds(thresholds);

		try {
			rm.setPresenceMap("./resource files/test.txt", species);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,Long> waitTimes = new TreeMap<String,Long>();
		waitTimes.put("Test_1", 0l);
		waitTimes.put("Test_2", 3l);
		
		Process_Dispersal pd = new Process_Dispersal();
		
		pd.setWaitTimes(waitTimes);

		processes = new ArrayList<Process>();
		processes.add(pg);
		processes.add(pd);
		processes.add(new Process_Infestation());
		processes.add(new Process_ManageAge());

	}

	// @Test
	public void test() {

		exp.setIdentifier("Experiment 1");
		exp.setMosaic(rm);
		exp.setDisperser(species, d2);
		exp.setProcesses(processes);
		exp.setStartTime(0);
		exp.setEndTime(5);
		exp.setTimeIncrement(1);

		ow.setName("t0.txt");
		ow.write(exp.getMosaic(), species);
		exp.step();
		ow.setName("t1.txt");
		ow.write(exp.getMosaic(), species);
		exp.step();
		exp.step();
		ow.setName("t3.txt");
		ow.write(exp.getMosaic(), species);
	}

	@Test
	public void testStepWrite() {
		exp.setIdentifier("Experiment 1");
		exp.setMosaic(rm);
		exp.setDisperser(species, d2);
		exp.setProcesses(processes);
		exp.setStartTime(0);
		exp.setEndTime(12);
		exp.setTimeIncrement(1);
		exp.setOutputWriter(ow);
		exp.writeEachTimeStep(true);
		exp.setExperimentWriter(new ExperimentWriter_Null());
		exp.run();
	}
}