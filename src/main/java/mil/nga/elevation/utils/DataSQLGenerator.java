package mil.nga.elevation.utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.elevation.dao.TerrainDataFile;
import mil.nga.util.FileFinder;

/**
 * Command line utility to generate a <code>data.sql</code> file for 
 * populating the internal derby database with data for testing.
 *  
 * @author L. Craig Carpenter
 */
public class DataSQLGenerator implements Closeable {

    /**
     * Set up the logback system for use throughout the class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            DataSQLGenerator.class.getName());
    
    // Constants
    public static final String DEFAULT_CLASSIFICATION="UNCLASSIFIED";
    public static final String DEFAULT_OUTPUT_FILE="data.sql";
    
    // Parameters
    private String path;
    private String searchString; 
    private String type; 
    private int    quality;
    
    BufferedWriter writer;
    
    /**
     * Default constructor. 
     * @param path Top level path.
     * @param searchString String to use for searching for DTED files.
     * @param type The type of DTED file.
     * @param quality The quality value.
     */
    public DataSQLGenerator(
            String path, 
            String searchString, 
            String type, 
            String quality) { 
        this.path         = path;
        this.searchString = searchString;
        this.type         = type;
        this.quality      = Integer.parseInt(quality.trim());
    }
    
    /**
     * Write the String to the output stream.
     * @param data String to write.
     * @throws IOException 
     */
    private void write(String data) throws IOException {
        if (writer == null) {
            String fileName = System.getProperty("java.io.tmpdir") 
                    + File.separator + DEFAULT_OUTPUT_FILE;
            LOGGER.info("Output file [ {} ].", fileName);
            writer = new BufferedWriter(new FileWriter(fileName));
        }
        else {
            writer.write(data);
            writer.newLine();
        }
    }
    
    /**
     * Create the <code>data.sql</code> file containing insert statements
     * for each of the DTED files found. 
     * @throws IOException Thrown if there are problems writing to the target 
     * output file.
     */
    public void create() throws IOException {
        int counter = 0;
        List<Path> dtedFiles = getListing(path, searchString);
        if ((dtedFiles != null) && (dtedFiles.size() > 0)) {
            for (Path p : dtedFiles) {
                
                String lat = getLatFromListing(p);                
                String lon = getLonFromListing(p.toString());

                TerrainDataFile record = new TerrainDataFile();
                record.setLat(lat);
                record.setLon(lon);
                record.setSource(type);
                record.setQuality(quality);
                record.setMarking(DEFAULT_CLASSIFICATION);
                record.setUnixPath(p.toString());
                record.setWindowsPath(p.toString());
                record.setRowId(Integer.toString(counter));
                write(record.toInsertStatement());
                counter++;
            }
        }
    }
    
    /**
     * Extract the latitude value from the path.  The latitude is the 
     * file name.
     * @param path The absolute path to a file.
     * @return The latitude portion of the file.
     */
    public String getLatFromListing(Path path) {
        String lat = path.getFileName().toString();
        return lat.substring(0, lat.lastIndexOf('.'));
    }
    
    /**
     * Extract the longitude value from the path.  The longitude is the 
     * parent directory of the file. 
     * @param path The absolute path to a file.
     * @return The longitude portion of the file.
     */
    public String getLonFromListing(String path) {
        String lon = path.toString();
        lon = lon.substring(0, lon.lastIndexOf(File.separatorChar));
        lon = lon.substring(lon.lastIndexOf(File.separatorChar)+1, lon.length());
        return lon;
    }
    
    /**
     * Get listing of the filesystem from input parent path.
     * 
     * @param path Starting point for file search.
     * @param searchString String in filename to search for.
     * @return Results of recursive find.
     */
    public List<Path> getListing(
            String path, 
            String searchString) throws IOException { 
        return FileFinder.find(path, searchString);
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            
            // Set up the command line options.
            Options options = new Options();
            Option  option  = Option.builder()
                    .longOpt("output")
                    .argName("Output File")
                    .hasArg()
                    .desc("Path to the output file.")
                    .build();
            options.addOption(option);
            option  = Option.builder()
                    .longOpt("input")
                    .argName("inputPath")
                    .hasArg()
                    .desc("Top-level directory to search.")
                    .build();
            options.addOption(option);
            option  = Option.builder()
                    .longOpt("type")
                    .argName("dataType")
                    .hasArg()
                    .desc("Type of DTED data")
                    .build();
            options.addOption(option);
            option  = Option.builder()
                    .longOpt("search")
                    .argName("searchString")
                    .hasArg()
                    .desc("Search string to use to find DTED files")
                    .build();
            options.addOption(option);
            option  = Option.builder()
                    .longOpt("quality")
                    .argName("integer quality")
                    .hasArg()
                    .desc("Integer quality value (higher value is lower quality)")
                    .build();
            options.addOption(option);
            // Parse the input argments.
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
        
            // If the correct options are provided, invoke the application 
            // logic
            if ((cmd.hasOption("input")) && (cmd.hasOption("type")) && 
                    (cmd.hasOption("search")) && (cmd.hasOption("quality"))) {
                                
                DataSQLGenerator dataGen = new DataSQLGenerator(
                        cmd.getOptionValue("input"),
                        cmd.getOptionValue("search"),
                        cmd.getOptionValue("type"),
                        cmd.getOptionValue("quality"));
                dataGen.create();
                dataGen.close();

            }
            else {
                LOGGER.error("Required command line options not "
                        + "supplied.  Required options [ input, type, search, " 
                        + " quality ].");
            }
        }
        catch (ParseException pe) {
            LOGGER.error("ParseException encountered while processing the "
                    + "command line arguments.  Exception message => [ {} ].",
                    pe.getMessage());
        }
        catch (IOException ioe) {
            LOGGER.error("Unexpected IOException encountered.  "
                    + "Exception message => [ {} ].",
                    ioe.getMessage());
        }
    }

    /**
     * Close the output file.
     */
    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}
