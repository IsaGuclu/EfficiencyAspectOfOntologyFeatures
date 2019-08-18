package experiments;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.IRI;

import edu.monash.infotech.owl2metrics.metrics.writer.MetricsWriter;
import edu.monash.infotech.owl2metrics.metrics.jgrapht.MetricsCollector;
import edu.monash.infotech.owl2metrics.model.OntMetrics;
import edu.monash.infotech.owl2metrics.translate.OWL2Graph;
import edu.monash.infotech.owl2metrics.translate.jgrapht.OWL2GraphJGraphTImpl;
import edu.monash.infotech.owl2metrics.translate.jgrapht.graph.NamedNode;
import edu.monash.infotech.owl2metrics.translate.jgrapht.graph.NamedParamEdge;

import org.jgrapht.DirectedGraph;

/**
 * @author Isa Guclu: This code is implemented by re-using/modifying the class "DirectoryProcessor" of Yuan-Fang Li 
 * according to the need of measuring the time consumption of metric generation. 
 */

public class Metrics92 {

	private static MetricsWriter metricWriter = new MetricsWriter();
	private static OWL2Graph<DirectedGraph<NamedNode, NamedParamEdge>, NamedNode, NamedParamEdge> owl2Graph = new OWL2GraphJGraphTImpl();
	static boolean measureExpressivity = false;	
	static String inputDir = "";
	static float fileSize;
	static String fileName = "";
	static String csvMetrics = "";	

	OWLOntologyManager ontManager;
	OWLOntology ont;

	public static void main(String[] args) throws FileNotFoundException {

		inputDir = args[0];
		// inputDir = "C:/Users/Isa/Desktop/tmp/";

		File folder = new File(inputDir);
		File[] listOfFiles = folder.listFiles(new OWLExtensionFilter());
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileName = listOfFiles[i].getName();
				PrintStream console = System.out;
				System.setOut(console);
				System.out.println(fileName);
				PrintStream out = null;

				try {

					String sConsoleOutput = inputDir + "ConsoleOutput/";
					File dNewFolder = new File(sConsoleOutput);
					if (!dNewFolder.exists()) {
						dNewFolder.mkdir();
						System.out.println("Folder created : " + sConsoleOutput);
					}

					out = new PrintStream(new FileOutputStream(sConsoleOutput + getCurrentTimeStamp() + "_" + fileName + ".txt"));
					System.setOut(out);

					File file = new File(inputDir + fileName);
					fileSize = file.length() / 1024; //  1 KB. = 1014 bytes
					
					measureOntology("file:///" + inputDir + fileName);
					
				} catch (Exception e) {
					System.out.println("!!! FAIL !!! : " + fileName + " : " + e.toString());
				} finally {
					out.close();
					System.setOut(console);
				}
			}
		}
	}

	public static void measureOntology(String pURL) {
		try {
			
			System.out.println("*** Ontology (" + pURL + ") started.");
			
			IRI iri = IRI.create(pURL);
			OWLOntologyDocumentSource source = new IRIDocumentSource(iri);

			long dBeforeLoadingOntology, dAfterLoadingOntology,dAfterMeasuringMetrics;

			File dCSVTimeMeasurement = new File(inputDir + "/" + "TimeConsumption_92Metrics.csv");
			boolean dFileExists = false;
			if (dCSVTimeMeasurement.exists())
				dFileExists = true;
			PrintWriter outResults = new PrintWriter(new FileOutputStream(dCSVTimeMeasurement, true));
			if (dFileExists != true)
				outResults.println("Ontology," + "\"OntologyLoadingTime_ns\"," + "\"MetricMeasuringTime_ns\"," + "\"TotalTime_ns\",");
			
			// *** TIME JUST BEFORE LOADING ONTOLOGY
			dBeforeLoadingOntology = System.nanoTime();
			
			DirectedGraph<NamedNode, NamedParamEdge> graph = owl2Graph.loadOWLOntology(source, true);

			// *** TIME JUST AFTER LOADING ONTOLOGY
			dAfterLoadingOntology = System.nanoTime();

			MetricsCollector collector = new MetricsCollector(graph, owl2Graph.getOntology(), measureExpressivity);
			OntMetrics metrics = collector.collectMetrics(pURL);
			metrics.setSze(fileSize);

			// *** TIME JUST AFTER MEASURING METRICS
			dAfterMeasuringMetrics = System.nanoTime();

			// Writing measurement logs
			outResults.print("\"" + fileName + "\"" + ",");
			outResults.print("\"" + (dAfterLoadingOntology - dBeforeLoadingOntology) + "\",");
			outResults.print("\"" + (dAfterMeasuringMetrics - dAfterLoadingOntology) + "\",");
			outResults.print("\"" + (dAfterMeasuringMetrics - dBeforeLoadingOntology) + "\"" + "\n");
			outResults.close();
			
			csvMetrics = inputDir + "/" + "Owl2Metrics.csv";
			File dCSVMetrics = new File(csvMetrics);
			if (!dCSVMetrics.exists()) {
				metricWriter.writeMetrics(csvMetrics, ',', fileName, metrics, true, true);
			} else {
				metricWriter.writeMetrics(csvMetrics, ',', fileName, metrics, false, true);
			}
			
			System.out.println("*** Ontology (" + pURL + ") processed.");
		} catch (Exception e) {
			System.out.println("!!! FAIL !!! : " + e.toString());
		} finally {
			owl2Graph.shutdown();
		}

	}	
	
	public static class OWLExtensionFilter implements FileFilter {
		public boolean accept(File pFileName) {
			return pFileName.toString().toUpperCase().endsWith(".OWL");
		}
	}
	
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

}
