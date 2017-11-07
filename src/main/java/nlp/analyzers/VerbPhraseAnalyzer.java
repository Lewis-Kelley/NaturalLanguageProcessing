package nlp.analyzers;

import edu.stanford.nlp.trees.Tree;
import nlp.PartOfSpeech;

public class VerbPhraseAnalyzer {
	private Tree verb;

	public VerbPhraseAnalyzer(Tree tree) {
		tree.getChildrenAsList().forEach(this::findVerb);
	}

	private void findVerb(Tree child) {
		if (TreeAnalyzer.treeIs(child, PartOfSpeech.VERB_BASE_FORM,
				                       PartOfSpeech.VERB_PRESENT))
			verb = child;
	}

	public String getVerb() {
		return verb.getChild(0).toString();
	}
}
