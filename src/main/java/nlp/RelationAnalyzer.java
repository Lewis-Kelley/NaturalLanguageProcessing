package nlp;

import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.Pair;

public class RelationAnalyzer {
	private SemanticGraph dependencies;
	private IndexedWord subject;
	private String quantifier;
	private Boolean topLevelNegation ;
	private Boolean predicateLevelNegation;

	public RelationAnalyzer(SemanticGraph dependencies) {
		this.dependencies = dependencies;
		subject = null;
		quantifier = null;
		topLevelNegation = false;
		predicateLevelNegation = false;
	}

	public void detailedAnalysis() {
		boolean status = parseDeclaratives();

		if (status) {
			parseModifiers();
			System.out.println(buildOutput());
		} else {
			System.out.println("Could not translate sentence.");
		}
	}

	private boolean parseDeclaratives() {
		boolean status = false;
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
				status = true;
			}
			if (relation.toString().equals("neg")) {
				predicateLevelNegation = true;
			}
		}

		return status;
	}

	private void parseModifiers() {
		List<Pair<GrammaticalRelation, IndexedWord>> subjectRelationWords = dependencies.childPairs(subject);
		for (Pair<GrammaticalRelation, IndexedWord> relationWord : subjectRelationWords) {
			GrammaticalRelation relation = relationWord.first();
			IndexedWord word = relationWord.second();

			if (relation.toString().equals("det")) {
				quantifier = word.lemma().toString();
			}
			if (relation.toString().equals("neg")) {
				topLevelNegation = true;
			}
		}
	}

	private String buildOutput() {
		StringBuilder output = new StringBuilder();

		if (topLevelNegation)
			output.append("not ");
		if (quantifier != null)
			output.append(quantifier + "(x): " + subject.lemma().toString() + "(x) ^ ");
		if (predicateLevelNegation)
			output.append("not(");
		if (quantifier == null) {
			output.append(dependencies.getFirstRoot().originalText()
					+ "(" + subject.lemma().toString() + ")");
		} else {
			output.append(dependencies.getFirstRoot().originalText() + "(x)");
		}
		if (predicateLevelNegation)
			output.append(")");

		return output.toString();
	}
}
