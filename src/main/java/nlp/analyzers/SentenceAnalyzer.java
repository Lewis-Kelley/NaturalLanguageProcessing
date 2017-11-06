package nlp.analyzers;

import java.util.*;

import edu.stanford.nlp.trees.Tree;
import nlp.*;

public class SentenceAnalyzer implements Analyzer {
	private Tree sentence;
	private VerbPhraseAnalyzer verbPhrase;
	private NounPhraseAnalyzer nounPhrase;

	public SentenceAnalyzer(Tree tree) {
		sentence = tree;
		System.out.println(sentence);
		sentence.getChildrenAsList().forEach(this::findVerbPhrase);
		sentence.getChildrenAsList().forEach(this::findNounPhrase);
	}

	private void findVerbPhrase(Tree child) {
		if (child.value().equals(Phrases.VERB_PHRASE.toString()))
			verbPhrase = new VerbPhraseAnalyzer(child);
	}

	private void findNounPhrase(Tree child) {
		if (child.value().equals(Phrases.NOUN_PHRASE.toString()))
			nounPhrase = new NounPhraseAnalyzer(child);
	}

	@Override
	public Collection<Frame> analyze() {
		List<Frame> frames = new LinkedList<>();

		frames.add(new Frame(nounPhrase.getNoun(), verbPhrase.getVerb()));

		return frames;
	}
}
