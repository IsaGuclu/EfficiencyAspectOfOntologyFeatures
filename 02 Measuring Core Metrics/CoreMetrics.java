package uk.abdn.cs.semanticweb.metrics;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.metrics.AxiomCount;
import org.semanticweb.owlapi.metrics.AxiomTypeMetric;
import org.semanticweb.owlapi.metrics.DLExpressivity;
import org.semanticweb.owlapi.metrics.GCICount;
import org.semanticweb.owlapi.metrics.HiddenGCICount;
import org.semanticweb.owlapi.metrics.LogicalAxiomCount;
import org.semanticweb.owlapi.metrics.OWLMetric;
import org.semanticweb.owlapi.metrics.ReferencedClassCount;
import org.semanticweb.owlapi.metrics.ReferencedDataPropertyCount;
import org.semanticweb.owlapi.metrics.ReferencedIndividualCount;
import org.semanticweb.owlapi.metrics.ReferencedObjectPropertyCount;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Isa Guclu
 * @version 1.0
 * @since 2018-01-12
 */

public class CoreMetrics {

	public static void main(String[] args) {
		
		String inputDir;
		inputDir = args[0];
		// inputDir = "C:/Users/Isa/Desktop/tmp/";

		File dFolder = new File(inputDir);
		if (!dFolder.isDirectory()) {
			System.out.println("!!! Process terminated. The specified folder does not exist !!!");
		}
		File[] dOntologyList = dFolder.listFiles(new OWLExtensionFilter());
		
		String dCSVFileName = "CoreMetrics.csv";
		File dMetricCSV = new File(dFolder.toString() + "/" + dCSVFileName);
		PrintWriter outResults = null;

		try {
			boolean dFileExists = false;
			if (dMetricCSV.exists())
				dFileExists = true;
			outResults = new PrintWriter(new FileOutputStream(dMetricCSV, true));
			if (dFileExists != true)
				printHeader(outResults);
			int counter = 0;

			for (File dOntoFile : dOntologyList) {
				if (counter % 10 == 0) {
					System.out.println(counter + " of " + dOntologyList.length + " ontologies are processed.");
				}
				counter++;
				CoreMetrics dMP = new CoreMetrics();
				dMP.measureMetrics(dOntoFile, outResults);
			}
			System.out.println(dOntologyList.length + " ontologies are processed.");
			outResults.close();
		} catch (Exception e) {
			System.out.println("!!! EXCEPTION !!! : " + e.toString());
		}
	}

	public static class OWLExtensionFilter implements FileFilter {
		public boolean accept(File pFileName) {
			return pFileName.toString().toUpperCase().endsWith(".OWL");
		}
	}

	public static void printHeader(PrintWriter out) {

		out.println("Ontology," + "\"OntologyLoadingTime_ns\"," + "\"MetricMeasuringTime_ns\","
				+ "\"TotalTime_ns\","

				+ "\"Axiom\"," + "\"LogicalAxiomCount\"," 
				+ "\"ClassCount\"," + "\"ObjectPropertyCount\"," + "\"DataPropertyCount\"," 
				+ "\"IndividualCount\"," + "\"DLExpressivity\"," + "\"DeclarationAxiomsCount\"," 

				+ "\"SubClassOf\"," + "\"EquivalentClasses\"," + "\"DisjointClasses\"," 
				+ "\"GCICount\"," + "\"HiddenGCICount\"," 

				+ "\"SubObjectPropertyOf\"," + "\"EquivalentObjectProperties\"," 
				// + "\"InverseObjectProperties\"," + "\"DisjointObjectProperties\"," 
				// + "\"FunctionalObjectProperty\"," + "\"InverseFunctionalObjectProperty\"," 
				+ "\"TransitiveObjectProperty\","
				// + "\"SymmetricObjectProperty\"," + "\"AsymmetricObjectProperty\"," 
				+ "\"ReflexiveObjectProperty\","
				// + "\"IrrefexiveObjectProperty\"," 
				+ "\"ObjectPropertyDomain\"," + "\"ObjectPropertyRange\"," + "\"SubPropertyChainOf\"," 
				+ "\"SubDataPropertyOf\"," + "\"EquivalentDataProperties\"," 
				// + "\"DisjointDataProperties\","
				+ "\"FunctionalDataProperty\"," + "\"DataPropertyDomain\"," + "\"DataPropertyRange\"," +

				"\"ClassAssertion\"," + "\"ObjectPropertyAssertion\"," + "\"DataPropertyAssertion\","
				+ "\"NegativeObjectPropertyAssertion\"," + "\"NegativeDataPropertyAssertion\"," + "\"SameIndividual\","
				+ "\"DifferentIndividuals\","

				+ "\"AnnotationAssertion\"," + "\"AnnotationPropertyDomain\"," + "\"AnnotationPropertyRangeOf\","

				+ "\"ABoxAxiomCount\"," + "\"TBoxAxiomCount\"," + "\"RBoxAxiomCount\"");

	}

	public void measureMetrics(File pOntoFile, PrintWriter pResults) {

		long dBeforeLoadingOntology, dAfterLoadingOntology,	dAfterMeasuringMetrics;
		int dABox = 0, dTBox = 0, dRBox = 0;

		OWLOntologyManager dOm = null;
		OWLOntology dOnto = null;

		try {

			dOm = OWLManager.createOWLOntologyManager();

			String dFilePath = pOntoFile.getAbsolutePath().replace("\\", "/");
			if (!(dFilePath.startsWith("http://") || dFilePath.startsWith("file://"))) {
				dFilePath = "file:///" + dFilePath;
			}
			IRI physicalURI = IRI.create(dFilePath);

			// *** TIME JUST BEFORE LOADING THE ONTOLOGY
			dBeforeLoadingOntology = System.nanoTime();
			dOnto = dOm.loadOntology(physicalURI);
			// *** TIME JUST AFTER LOADING THE ONTOLOGY
			dAfterLoadingOntology = System.nanoTime();

			List<OWLMetric> metrics = new ArrayList<OWLMetric>();
			// dMP2.createBasicMetrics(dOm, metricManagerMap);
			metrics.add(new AxiomCount(dOm));
			metrics.add(new LogicalAxiomCount(dOm));			
			metrics.add(new ReferencedClassCount(dOm));
			metrics.add(new ReferencedObjectPropertyCount(dOm));
			metrics.add(new ReferencedDataPropertyCount(dOm));
			metrics.add(new ReferencedIndividualCount(dOm));
			// metrics.add(new ReferencedAnnotationPropertyCount(getOntology())); // OWLAPI (v.3.4.2) doesn't contain that metric.
			metrics.add(new DLExpressivity(dOm));

			metrics.add(new AxiomTypeMetric(dOm, AxiomType.DECLARATION));
			
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.SUBCLASS_OF));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.EQUIVALENT_CLASSES));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.DISJOINT_CLASSES));
			metrics.add(new GCICount(dOm));
			metrics.add(new HiddenGCICount(dOm));

			metrics.add(new AxiomTypeMetric(dOm, AxiomType.SUB_OBJECT_PROPERTY));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.EQUIVALENT_OBJECT_PROPERTIES));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.TRANSITIVE_OBJECT_PROPERTY));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.REFLEXIVE_OBJECT_PROPERTY));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.OBJECT_PROPERTY_DOMAIN));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.OBJECT_PROPERTY_RANGE));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.SUB_PROPERTY_CHAIN_OF));

			metrics.add(new AxiomTypeMetric(dOm, AxiomType.SUB_DATA_PROPERTY));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.EQUIVALENT_DATA_PROPERTIES));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.FUNCTIONAL_DATA_PROPERTY));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.DATA_PROPERTY_DOMAIN));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.DATA_PROPERTY_RANGE));

			metrics.add(new AxiomTypeMetric(dOm, AxiomType.CLASS_ASSERTION));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.OBJECT_PROPERTY_ASSERTION));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.DATA_PROPERTY_ASSERTION));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.SAME_INDIVIDUAL));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.DIFFERENT_INDIVIDUALS));

			metrics.add(new AxiomTypeMetric(dOm, AxiomType.ANNOTATION_ASSERTION));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.ANNOTATION_PROPERTY_DOMAIN));
			metrics.add(new AxiomTypeMetric(dOm, AxiomType.ANNOTATION_PROPERTY_RANGE));
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.INVERSE_OBJECT_PROPERTIES));  // *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.DISJOINT_OBJECT_PROPERTIES)); // *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.FUNCTIONAL_OBJECT_PROPERTY)); // *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY));// *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.SYMMETRIC_OBJECT_PROPERTY));  // *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.ASYMMETRIC_OBJECT_PROPERTY)); // *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.IRREFLEXIVE_OBJECT_PROPERTY));// *** NOT IN EL++
			// metrics.add(new AxiomTypeMetric(dOm, AxiomType.DISJOINT_DATA_PROPERTIES));   // *** NOT IN EL++
						
			// SECTION 7 : ATR AXIOM METRICS		
			dABox = getAxiomsCount(dOnto, ABoxAxiomTypes_v4);
			dTBox = getAxiomsCount(dOnto, TBoxAxiomTypes_v4);
			dRBox = getAxiomsCount(dOnto, RBoxAxiomTypes_v4);			
		
			dAfterMeasuringMetrics = System.nanoTime();

			pResults.print("\"" + pOntoFile.getName() + "\"" + ",");

			pResults.print("\"" + (dAfterLoadingOntology - dBeforeLoadingOntology) + "\"" + ",");
			pResults.print("\"" + (dAfterMeasuringMetrics - dAfterLoadingOntology) + "\"" + ",");
			pResults.print("\"" + (dAfterMeasuringMetrics - dBeforeLoadingOntology) + "\"" + ",");

			for (OWLMetric m : metrics) {
				m.setOntology(dOnto);
				m.setImportsClosureUsed(true);

				String[] dPairs = m.toString().split(":");
				// String dTemp = dPairs[1].trim();
				pResults.print("\"" + dPairs[1].trim() + "\"" + ",");
			}

			pResults.print("\"" + dABox + "\"" + ",");
			pResults.print("\"" + dTBox + "\"" + ",");
			pResults.print("\"" + dRBox + "\"" + ",");
			pResults.print("\n");

			pResults.flush();

		} catch (Exception e) {
			System.out.println("!!! EXCEPTION !!! : " + e.toString());
		}

	}

	/*********************************** ABox-TBox-RBox catergorization in OWLAPI 4 ***********************************/
	
	public static final AxiomType[] TBoxAxiomTypes_v4 = new AxiomType[] { AxiomType.SUBCLASS_OF,
			AxiomType.EQUIVALENT_CLASSES, AxiomType.DISJOINT_CLASSES, AxiomType.OBJECT_PROPERTY_DOMAIN,
			AxiomType.OBJECT_PROPERTY_RANGE, AxiomType.FUNCTIONAL_OBJECT_PROPERTY,
			AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, AxiomType.DATA_PROPERTY_DOMAIN, AxiomType.DATA_PROPERTY_RANGE,
			AxiomType.FUNCTIONAL_DATA_PROPERTY, AxiomType.DATATYPE_DEFINITION, AxiomType.DISJOINT_UNION,
			AxiomType.HAS_KEY };

	public static final AxiomType[] ABoxAxiomTypes_v4 = new AxiomType[] { AxiomType.CLASS_ASSERTION,
			AxiomType.SAME_INDIVIDUAL, AxiomType.DIFFERENT_INDIVIDUALS, AxiomType.OBJECT_PROPERTY_ASSERTION,
			AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION, AxiomType.DATA_PROPERTY_ASSERTION,
			AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION };

	public static final AxiomType[] RBoxAxiomTypes_v4 = new AxiomType[] { AxiomType.TRANSITIVE_OBJECT_PROPERTY,
			AxiomType.DISJOINT_DATA_PROPERTIES, AxiomType.SUB_DATA_PROPERTY, AxiomType.EQUIVALENT_DATA_PROPERTIES,
			AxiomType.DISJOINT_OBJECT_PROPERTIES, AxiomType.SUB_OBJECT_PROPERTY, AxiomType.EQUIVALENT_OBJECT_PROPERTIES,
			AxiomType.SUB_PROPERTY_CHAIN_OF, AxiomType.INVERSE_OBJECT_PROPERTIES, AxiomType.SYMMETRIC_OBJECT_PROPERTY,
			AxiomType.ASYMMETRIC_OBJECT_PROPERTY, AxiomType.REFLEXIVE_OBJECT_PROPERTY,
			AxiomType.IRREFLEXIVE_OBJECT_PROPERTY };
 	
	/*********************************** ABox-TBox-RBox catergorization in OWLAPI 4 ***********************************/
	
	public int getAxiomsCount(OWLOntology pOntology, AxiomType[] pAxiomTypes) {
		int dResult = 0;
		for (int i = 0; i < pAxiomTypes.length; i++) {
			dResult += pOntology.getAxioms(pAxiomTypes[i]).size();
		}
		return dResult;
	}

}
