package nlp;

import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.*;

@SuppressWarnings("deprecation")
public class DependencyAnalyzer {
	private SemanticGraph dependencies;
	private RelationAnalyzer relationAnalyzer;

	public DependencyAnalyzer(CoreMap map) {
		dependencies = map.get(CollapsedCCProcessedDependenciesAnnotation.class);
		relationAnalyzer = new RelationAnalyzer(dependencies);
	}

	public void analyseDependencies() {
		System.out.println(dependencies);

		parseType(dependencies.getFirstRoot());
	}

	public void analyseRelations() {
		relationAnalyzer.detailedAnalysis();
	}

	private void parseType(IndexedWord root) {
		String type = root.tag();
		switch (type) {
		case "DT":
			processDeterminer(root);
			break;
		case "NN":
			processNounPhrase(root);
			break;
		case "VB":
			processVerbPhrase(root);
			break;
		default:
			System.out.println("Cannot identify sentence structure.");
		}
	}

	// Processes: {This, that} one?
	public void processDeterminer(IndexedWord root) {
		System.out.println("Identity of object: " + root.originalText().toLowerCase());
	}

	// Processes: {That, this, the} {block, sphere}
	public void processNounPhrase(IndexedWord root) {
		List<Pair<GrammaticalRelation, IndexedWord>> s = dependencies.childPairs(root);

		System.out.println("Identity of object: " + root.originalText().toLowerCase());
		System.out.println("Type of object: " + s.get(0).second().originalText().toLowerCase());
	}

	// Processes: {Pick up, put down} {that, this} {block, sphere}
	public void processVerbPhrase(IndexedWord root) {
		List<Pair<GrammaticalRelation, IndexedWord>> s = dependencies.childPairs(root);
		Pair<GrammaticalRelation, IndexedWord> prt = s.get(0);
		Pair<GrammaticalRelation, IndexedWord> dobj = s.get(1);

		//List<Pair<GrammaticalRelation, IndexedWord>> newS = dependencies.childPairs(dobj.second());

		System.out.println("Action: " + root.originalText().toLowerCase()
				            + " " + prt.second().originalText().toLowerCase());
		System.out.println("Type of object: " + dobj.second().originalText().toLowerCase());
		//System.out.println("Identity of object: " + newS.get(0).second().originalText().toLowerCase());
	}
}
