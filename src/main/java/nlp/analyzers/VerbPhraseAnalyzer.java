package nlp.analyzers;

import java.util.*;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;
import nlp.*;

public class VerbPhraseAnalyzer {
	private Tree verb;
	private NounPhraseAnalyzer object;
	private List<VerbPhraseAnalyzer> verbPhrases;

	public VerbPhraseAnalyzer(Tree tree) {
		verbPhrases = new LinkedList<>();
		tree.getChildrenAsList().forEach(this::findVerbs);
		tree.getChildrenAsList().forEach(this::findObject);
	}

	private void findVerbs(Tree child) {
		if (TreeAnalyzer.treeIs(child, PartOfSpeech.VERB_BASE_FORM,
				                       PartOfSpeech.VERB_PRESENT,
				                       PartOfSpeech.VERB_PAST_TENSE))
			verb = child;
		else if (child.value().equals(Phrases.VERB_PHRASE.toString()))
			verbPhrases.add(new VerbPhraseAnalyzer(child));
	}

	private void findObject(Tree child) {
		if (child.value().equals(Phrases.NOUN_PHRASE.toString()))
			object = new NounPhraseAnalyzer(child);
	}

	public List<Pair<String, List<String>>> getVerbs() {
		List<Pair<String, List<String>>> verbs = new LinkedList<>();

		if (verb != null)
			addVerbObject(verbs);

		for (VerbPhraseAnalyzer verbPhrase : verbPhrases)
			verbs.addAll(verbPhrase.getVerbs());

		return verbs;
	}

	private void addVerbObject(List<Pair<String, List<String>>> verbs) {
		List<String> nouns;
		if (object != null)
			nouns = object.getNouns();
		else
			nouns = new LinkedList<>();

		verbs.add(new Pair<String, List<String>>(verb.getChild(0).toString(), nouns));
	}
}
