package MetroSystemBackend;

import android.content.res.Resources;
import android.test.InstrumentationTestCase;
import au.com.bytecode.opencsv.CSVReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.myapp.R;

public class MetroBuilder {

	// TODO : Refactor using metroLine mediator
	public subwaySystem buildSubwayFromLineCSV(InputStream inStream) throws Exception {
		subwaySystem subSystem = new subwaySystem();
		MSTMetro mstSub = new MSTMetro();
		CSVReader reader = null;
		
		/*InstrumentationTestCase it = new InstrumentationTestCase();
		Resources res = it.getInstrumentation().getContext().getResources();*/
		
		reader = new CSVReader(new InputStreamReader(inStream));
		
		String[] nextLine, stationAttr;
		Integer sIndex, sPrevDistance, sPrevTime, sPrevCost, sPrev;
		String sName, otherMetroLines, currentMetroLine;
		while ((nextLine = reader.readNext()) != null) {
			currentMetroLine = nextLine[0];
			sPrev = null;
			for (String val : nextLine) {
				if (val.contains(";")) { // Skip first line declaration
					stationAttr = val.split(";");
					sIndex = Integer.parseInt(stationAttr[0]);
					sName = stationAttr[1];
					sPrevDistance = Integer.parseInt(stationAttr[3]);
					sPrevTime = Integer.parseInt(stationAttr[4]);
					sPrevCost = Integer.parseInt(stationAttr[5]);
					otherMetroLines = stationAttr[2];
					if (!subSystem.checkNodeExists(sIndex)) {
						subSystem.addNode(sIndex, sName, currentMetroLine);
					}

					if (otherMetroLines.length() > 0) {
						for (String l : otherMetroLines.split("&")) {
							subSystem.addLineToStation(l, sIndex);
						}
					}

					if (sPrev != null) {
						subSystem.connectStations(sIndex, sPrev, sPrevDistance,
								sPrevCost, sPrevTime);
					}
					sPrev = sIndex;
				}
			}
		}

		return subSystem;
		
		/*mstSub.setNodeSet(subSystem.getNodeSet());
		mstSub.setEdgeSet(subSystem.getEdgeSet());
		mstSub.setLineStationMediator(subSystem.getLineStationMediator());
		mstSub.kruskalAlgorithm();

		return mstSub;*/
	}

	// A horribily written function littered with code smells.
	public MSTMetro buildSubwayFromCSV(String filepath) throws Exception {

		subwaySystem subSystem = new subwaySystem();

		CSVReader reader = null;
		CSVReader reader2 = null;
		try {
			reader = new CSVReader(new FileReader(filepath));
			reader2 = new CSVReader(new FileReader(filepath));
		} catch (IOException e) {
			System.out.println("Invalid File Path");
			e.printStackTrace();
		}

		Integer stationIndex = 1;
		while (reader2.readNext() != null) {
			subSystem.addNode(stationIndex, "No", "No");
			stationIndex++;
		}

		String[] nextLine;
		String[] edgeAttr;
		Integer sIndex = 1;
		while ((nextLine = reader.readNext()) != null) {
			Integer connectStationIndex = 1;
			// nextLine[] is an array of values from the line
			for (String val : nextLine) {
				val = val.replace("(", "");
				val = val.replace(")", "");
				if (val.contains(";")) {
					edgeAttr = val.split(";");
					if (edgeAttr.length == 3) {
						Integer distance = Integer.parseInt(edgeAttr[0]);
						Integer cost = Integer.parseInt(edgeAttr[1]);
						Integer time = Integer.parseInt(edgeAttr[2]);
						subSystem.connectStations(sIndex, connectStationIndex,
								distance, cost, time);
					} else {
						throw new Exception("An edge must be define with three values separated by a delimiter ;");
					}
				}
				connectStationIndex++;
			}
			sIndex++;
		}

		MSTMetro mstSub = new MSTMetro();
		mstSub.setEdgeSet(subSystem.getEdgeSet());
		mstSub.setNodeSet(subSystem.getNodeSet());
		mstSub.kruskalAlgorithm();

		return mstSub;
	}
	
	public MSTMetro buildMSTMetro(subwaySystem subSystem) {
		MSTMetro mstSub = new MSTMetro();
		
		mstSub.setNodeSet(subSystem.getNodeSet());
		mstSub.setEdgeSet(subSystem.getEdgeSet());
		mstSub.setLineStationMediator(subSystem.getLineStationMediator());
		mstSub.kruskalAlgorithm();

		return mstSub;
	}

	public MSTMetro buildSubwayFromMatrix(int[][] mapMatrix) {
		subwaySystem subSystem = new subwaySystem();
		MSTMetro mstMetro = new MSTMetro();

		for (int i = 0; i < mapMatrix[0].length; i++) {
			for (int j = 0; j < i; j++) {
				if (!subSystem.checkNodeExists(j + 1)) {
					subSystem.addNode(j + 1, "No", "No");
				}
				if (mapMatrix[i][j] > 0) {
					try {
						subSystem.connectStations(i + 1, j + 1, mapMatrix[i][j], 1, 1);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		mstMetro.setNodeSet(null);

		return null;
	}
 
	public static void main(String args[]) {
		MetroBuilder subBuilder1 = new MetroBuilder();
		MSTMetro mstSub;
		/*try {
			mstSub = subBuilder1.buildSubwayFromLineCSV("C:/Users/chiu.sintung/workspace/TokyoMetro/SubwayMaps/Book2.csv");
			for (int i=1; i<=76; i++) {
				for (int j=1; j<=76; j++) {
					if (i != j) {
						mstSub.getShortestPath(i, j);
					}
				}
			}
			mstSub.getShortestPath(11, 27);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}