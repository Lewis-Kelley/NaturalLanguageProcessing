package nlp.analyzers;

import edu.stanford.nlp.trees.Tree;
import nlp.*;

public class VerbPhraseAnalyzer {
	private NounPhraseAnalyzer object;
	private Tree verb;

	public VerbPhraseAnalyzer(Tree tree) {
		tree.getChildrenAsList().forEach(this::findVerb);
		tree.getChildrenAsList().forEach(this::findObject);
	}

	private void findVerb(Tree child) {
		if (TreeAnalyzer.treeIs(child, PartOfSpeech.VERB_BASE_FORM,
				                       PartOfSpeech.VERB_PRESENT))
			verb = child;
	}

	private void findObject(Tree child) {
		if (child.value().equals(Phrases.NOUN_PHRASE.toString()))
			object = new NounPhraseAnalyzer(child);
	}

	public String getVerb() {
		return verb.getChild(0).toString();
	}

	public String getObject() {
		return object == null ? "" : object.getNoun();
	}
}
