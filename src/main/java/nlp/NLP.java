package nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class NLP {
	public static void main(String[] args) {
		StanfordCoreNLP pipeline = createCore();

		Annotation document = processString(pipeline, "Those people are so mean. I hate them.");

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values
		// with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			analyseTokens(sentence);

			analyseTree(sentence);

			analyzeDependencies(sentence);
		}

		analyseCorefChain(document);
	}

	private static StanfordCoreNLP createCore() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

		return new StanfordCoreNLP(props);
	}

	private static Annotation processString(StanfordCoreNLP pipeline, String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		return document;
	}

	private static void processFile(StanfordCoreNLP coreNLP, File file) {
		Collection<File> fileCollection = new ArrayList<File>();
		fileCollection.add(file);

		try {
			coreNLP.processFiles(fileCollection, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void analyseTokens(CoreMap sentence) {
		printHeader("Analysing Tokens");
		sentence.get(TokensAnnotation.class).forEach(NLP::analyseToken);
	}

	private static void analyseToken(CoreLabel token) {
		String word = token.get(TextAnnotation.class);
		String pos = token.get(PartOfSpeechAnnotation.class);
		String ne = token.get(NamedEntityTagAnnotation.class);
		System.out.println("word: " + word + ", pos: " + pos + ", ne: " + ne);
	}

	private static void analyseTree(CoreMap sentence) {
		printHeader("Analysing Tree");
		Tree tree = sentence.get(TreeAnnotation.class);
		System.out.println();
		System.out.println(tree);
	}

	private static void analyzeDependencies(CoreMap sentence) {
		printHeader("Analysing Dependencies");
		DependencyAnalyzer dependencies = new DependencyAnalyzer(sentence);
		dependencies.analyseDependencies();
		dependencies.analyseRelations();
	}

	private static void analyseCorefChain(Annotation document) {
		printHeader("Analysing Co-ref Chain");

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
		System.out.println();
		System.out.println(graph);
	}

	private static void printHeader(String text) {
		System.out.printf("===== %s =====\n", text);
	}
}