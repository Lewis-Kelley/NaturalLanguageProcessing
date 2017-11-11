package nlp.analyzers;

import java.util.*;

import edu.stanford.nlp.trees.Tree;
import nlp.*;

public class NounPhraseAnalyzer {
	private List<Tree> nouns;
	private NounPhraseAnalyzer nestedNoun;

	public NounPhraseAnalyzer(Tree tree) {
		nouns = new LinkedList<>();
		tree.getChildrenAsList().forEach(this::findSubject);
	}

	private void findSubject(Tree child) {
		if (TreeAnalyzer.treeIs(child, PartOfSpeech.DETERMINER,
									   PartOfSpeech.THERE,
									   PartOfSpeech.ADJECTIVE,
									   PartOfSpeech.PLURAL_NOUN,
				                       PartOfSpeech.PLURAL_PROPER_NOUN,
				                       PartOfSpeech.SINGULAR_NOUN,
				                       PartOfSpeech.PROPER_NOUN,
				                       PartOfSpeech.PERSONAL_PRONOUN,
				                       PartOfSpeech.WH_PRONOUN))
			nouns.add(child);
		if (child.value().equals(Phrases.NOUN_PHRASE.toString()))
			nestedNoun = new NounPhraseAnalyzer(child);
	}

	public List<String> getNouns() {
		List<String> nounResults = new LinkedList<>();

		for (Tree noun : nouns)
			nounResults.add(noun.getChild(0).toString());

		if (nestedNoun != null)
			nounResults.addAll(nestedNoun.getNouns());

		return nounResults;
	}
}
