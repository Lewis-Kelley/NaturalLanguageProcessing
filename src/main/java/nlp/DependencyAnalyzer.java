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

	public DependencyAnalyzer(CoreMap map) {
		dependencies = map.get(CollapsedCCProcessedDependenciesAnnotation.class);
	}

	public void analyseDependencies() {
		System.out.println(dependencies);

		parseType(dependencies.getFirstRoot());
		detailedAnalysis();
	}

	private void detailedAnalysis() {
		IndexedWord subject = null;
		String quantifier = null;
		Boolean goodToGo = false;
		Boolean topLevelNegation = false;
		Boolean predicateLevelNegation = false;

		List<Pair<GrammaticalRelation, IndexedWord>> relationWords = dependencies.childPairs(dependencies.getFirstRoot());
		for (Pair<GrammaticalRelation, IndexedWord> relationWord : relationWords) {
			GrammaticalRelation relation = relationWord.first();
			IndexedWord word = relationWord.second();

			if (relation.toString().equals("nsubj")) {
				subject = word;
			}
			if (relation.toString().equals("cop")
					&& (relationWord.second.originalText().toLowerCase().equals("are")
					|| relationWord.second.originalText().toLowerCase().equals("is"))) {
				goodToGo = true;
			}
			if (relation.toString().equals("neg")) {
				predicateLevelNegation = true;
			}
		}

		List<Pair<GrammaticalRelation, IndexedWord>> t = dependencies.childPairs(subject);
		for (Pair<GrammaticalRelation, IndexedWord> item : t) {
			if (item.first.toString().equals("det")) {
				quantifier = item.second.lemma().toString();
			}
			if (item.first.toString().equals("neg")) {
				topLevelNegation = true;
			}
		}

		if (goodToGo) {
			String output = "";
			if (topLevelNegation)
				output += "not ";
			if (quantifier != null)
				output += quantifier + "(x): " + subject.lemma().toString() + "(x) ^ ";
			if (predicateLevelNegation)
				output += "not(";
			if (quantifier == null) {
				output += dependencies.getFirstRoot().originalText() + "(" + subject.lemma().toString() + ")";
			} else {
				output += dependencies.getFirstRoot().originalText() + "(x)";
			}
			if (predicateLevelNegation)
				output += ")";

			System.out.println(output);
		} else {
			System.out.println("Could not translate sentence.");
		}
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

		List<Pair<GrammaticalRelation, IndexedWord>> newS = dependencies.childPairs(dobj.second());

		System.out.println(
				"Action: " + root.originalText().toLowerCase() + " " + prt.second().originalText().toLowerCase());
		System.out.println("Type of object: " + dobj.second().originalText().toLowerCase());
		System.out.println("Identity of object: " + newS.get(0).second().originalText().toLowerCase());
	}
}
