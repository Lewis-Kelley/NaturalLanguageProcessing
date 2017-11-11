package nlp.analyzers;

import java.util.*;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;
import nlp.*;

public class SentenceAnalyzer implements Analyzer {
	private Tree sentence;
	private List<SentenceAnalyzer> subSentences;
	private VerbPhraseAnalyzer verbPhrase;
	private NounPhraseAnalyzer nounPhrase;

	public SentenceAnalyzer(Tree tree) {
		sentence = tree;
		List<Tree> children = sentence.getChildrenAsList();
		children.forEach(this::findVerbPhrase);
		children.forEach(this::findNounPhrase);

		subSentences = new LinkedList<>();
		children.forEach(this::findSubSentences);
	}

	private void findVerbPhrase(Tree child) {
		if (child.value().equals(Phrases.VERB_PHRASE.toString()))
			verbPhrase = new VerbPhraseAnalyzer(child);
	}

	private void findNounPhrase(Tree child) {
		if (child.value().equals(Phrases.NOUN_PHRASE.toString()))
			nounPhrase = new NounPhraseAnalyzer(child);
	}

	private void findSubSentences(Tree child) {
		if (child.value().equals(Phrases.SENTENCE.toString()))
			subSentences.add(new SentenceAnalyzer(child));
	}

	@Override
	public Collection<Frame> analyze() {
		List<Frame> frames = new LinkedList<>();

		for (SentenceAnalyzer subSentence : subSentences)
			frames.addAll(subSentence.analyze());

		if (nounPhrase == null || verbPhrase == null)
			return frames;

		List<String> nounObjects = nounPhrase.getNouns();
		List<Pair<String, List<String>>> verbObjects = verbPhrase.getVerbs();

		for (String subject : nounObjects) {
			for (Pair<String, List<String>> verbObject : verbObjects) {
				String verb = verbObject.first();
				List<String> objects = verbObject.second();
				if (objects.isEmpty()) {
					frames.add(new Frame(subject, verb));
				} else {
					for (String object : objects) {
						frames.add(new Frame(subject, verb, object));
					}
				}
			}
		}

		return frames;
	}
}
