package trowl.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import eu.trowl.owlapi3.rel.reasoner.el.RELReasonerFactory;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class TBoxClassification {

	static String TSV_HEADER = "Ontology" + "\t" + "OntologyLoading" + "\t" + "Classification";;

	public static void main(String[] args) {

		// String strFile = "C:/Users/Isa/Desktop/3858/00b86bdc-e16b-46bf-93fb-9cde6849dcc8_onto.rdf_functional.owl";  
		String strFile = args[0];

		File dFile = new File(strFile);
		
		long start = 0;
		long loadingTime = 0;
		long classificationTime = 0;

		// Logs of Execution
		File outFile = new File("TrOWL_Classification_NanoSecs.tsv"); // dFilePath + File.separator + 
		PrintWriter out = null;

		// Logs of Errors
		File errorFile = new File("TrOWL_Exceptions.tsv"); // dFilePath + File.separator + 
		PrintWriter errorOut = null;
		OWLReasoner reasoner = null;
		
		try {

			if (outFile.exists()) {
				out = new PrintWriter(new FileWriter(outFile, true));
			} else {
				out = new PrintWriter(new FileWriter(outFile));
				out.println(TSV_HEADER);
			}

			if (errorFile.exists()) {
				errorOut = new PrintWriter(new FileWriter(errorFile, true));
			} else {
				errorOut = new PrintWriter(new FileWriter(errorFile));
			}

			// Staring of REASONING
			start = System.nanoTime();

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			// Load your ontology
			OWLOntology ont = manager.loadOntologyFromOntologyDocument(dFile);

			loadingTime = System.nanoTime();
			
			// Create an TrOWL reasoner.
			RELReasonerFactory relfactory = new RELReasonerFactory();
			reasoner = relfactory.createReasoner(ont);

			// Classify the ontology.
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

			classificationTime = System.nanoTime();
			
			// Terminate the worker threads used by the reasoner.
			reasoner.dispose();

		} catch (Exception e) {
			errorOut.println(strFile + "\t" + e.getClass().getName() + "\t" + e.getMessage());
			System.err.println(strFile + " could not be processed");
			e.printStackTrace();
		} finally {
			out.println(strFile + "\t" + (loadingTime - start) + "\t" + (classificationTime - loadingTime));
			out.flush();
			out.close();
			errorOut.flush();
			errorOut.close();
		}
	}

}
