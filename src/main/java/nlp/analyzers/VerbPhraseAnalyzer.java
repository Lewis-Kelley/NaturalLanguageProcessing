package nlp.analyzers;

import edu.stanford.nlp.trees.Tree;
import nlp.PartOfSpeech;

public class VerbPhraseAnalyzer {
	private Tree verb;

	public VerbPhraseAnalyzer(Tree tree) {
		tree.getChildrenAsList().forEach(this::findVerb);
	}

	private void findVerb(Tree child) {
		if (child.value().equals(PartOfSpeech.VERB_BASE_FORM.toString())
				|| child.value().equals(PartOfSpeech.VERB_PRESENT.toString()))
			verb = child;
	}

	public String getVerb() {
		return verb.getChild(0).toString();
	}
}
