package nlp.analyzers;

import edu.stanford.nlp.trees.Tree;
import nlp.PartOfSpeech;

public class NounPhraseAnalyzer {
	private Tree subject;

	public NounPhraseAnalyzer(Tree tree) {
		tree.getChildrenAsList().forEach(this::findSubject);
	}

	private void findSubject(Tree child) {
		if (TreeAnalyzer.treeIs(child, PartOfSpeech.PLURAL_NOUN,
				                       PartOfSpeech.PLURAL_PROPER_NOUN,
				                       PartOfSpeech.SINGULAR_NOUN,
				                       PartOfSpeech.PROPER_NOUN,
				                       PartOfSpeech.PERSONAL_PRONOUN))
			subject = child;
	}

	public String getNoun() {
		return subject.getChild(0).toString();
	}
}
