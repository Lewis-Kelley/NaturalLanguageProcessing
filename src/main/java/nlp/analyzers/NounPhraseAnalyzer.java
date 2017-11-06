package nlp.analyzers;

import edu.stanford.nlp.trees.Tree;

public class NounPhraseAnalyzer {
	private String noun;

	public NounPhraseAnalyzer(Tree child) {
		noun = "A NOUN";
	}

	public String getNoun() {
		return noun;
	}
}
