package nlp.analyzers;

import java.util.*;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;
import nlp.*;

public class SentenceAnalyzer implements Analyzer {
	private Tree sentence;
	private VerbPhraseAnalyzer verbPhrase;
	private NounPhraseAnalyzer nounPhrase;

	public SentenceAnalyzer(Tree tree) {
		sentence = tree;
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

		List<String> nounObjects = nounPhrase.getNouns();
		List<Pair<String, List<String>>> verbObjects = verbPhrase.getVerbs();

		for (String subject : nounObjects) {
			for (Pair<String, List<String>> verbObject : verbObjects) {
				String verb = verbObject.first();
				frames.add(new Frame(subject, verb));
				for (String object : verbObject.second()) {
					frames.add(new Frame(subject, verb, object));
				}
			}
		}

		return frames;
	}
}
