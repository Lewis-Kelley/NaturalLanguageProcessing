package nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

public class NLP {
	public static void main(String[] args) {
		StanfordCoreNLP pipeline = createCore();

		Annotation document = processFile(pipeline, new File("sample.txt"));

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values
		// with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			analyzeDependencies(sentence);
		}
	}

	private static StanfordCoreNLP createCore() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

		return new StanfordCoreNLP(props);
	}

	private static Annotation processFile(StanfordCoreNLP coreNLP, File file) {
		try {
			StringBuilder builder = fileToString(file);
			return processString(coreNLP, builder.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static StringBuilder fileToString(File file) throws IOException {
		BufferedReader reader = IOUtils.readerFromFile(file);
		StringBuilder builder = new StringBuilder();

		while (reader.ready())
			builder.append(reader.readLine());
		return builder;
	}

	private static Annotation processString(StanfordCoreNLP pipeline, String text) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		return document;
	}

	private static void analyzeDependencies(CoreMap sentence) {
		printHeader("Analysing Dependencies");
		DependencyAnalyzer dependencies = new DependencyAnalyzer(sentence);
		dependencies.analyseDependencies();
		dependencies.analyseRelations();
	}

	private static void printHeader(String text) {
		System.out.printf("===== %s =====\n", text);
	}
}