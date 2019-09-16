package mil.nga.elevation.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mil.nga.elevation.dao.TerrainDataFile;

/**
 * We noticed during testing that quite a few DEM cells identified in the 
 * database do not exist on disk.  This class was added to check for missing 
 * cells and write a list of them out to disk.
 * 
 * @author L. Craig Carpenter
 */
@Component
public class CellAvailabilityService {

    /**
     * Write all missing cells to the following file.
     */
	public static final String OUTPUT_FILE = "/var/log/applications/missing_cells.csv";
    
	/**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(CellAvailabilityService.class);
    
    /**
     * AutoWired reference to the data repository.
     */
	@Autowired
	TerrainDataFileService repository;
	
	public void CheckForMissingCells() {
		int counter = 0;
		Path outputFile = Paths.get(OUTPUT_FILE);
		
		List<TerrainDataFile> cells = repository.findAll();
		if ((cells != null) && (cells.size() > 0)) { 
			try (BufferedWriter writer = Files.newBufferedWriter(outputFile, Charset.forName("UTF-8"))) {
				LOGGER.info ("Processing [ "
						+ cells.size()
						+ " ] DEM cells.");
				for (TerrainDataFile file : cells) {
					if (!Files.exists(Paths.get(file.getUnixPath()))) {
						counter += 1;
						writer.write(file.toCSV());
						writer.newLine();
						LOGGER.info("Cell source [ "
								+ file.getSource()
								+ " ] with lat:[ "
								+ file.getLat()
								+ " ], lon:[ "
								+ file.getLon()
								+ " ] missing.");
					}
					writer.flush();
				}
				LOGGER.info("Total cells processed [ "
						+ cells.size()
						+ " ] - Total cells missing [ "
						+ counter
						+ " ].");
			}
			catch (IOException ioe) {
				LOGGER.error("IOException encountered while processing "
						+ "data file records. Error message => [ "
						+ ioe.getMessage()
						+ " ].");
			}
		}
		else {
			LOGGER.error("No records selected from the repository.");
		}
	}
	
}
