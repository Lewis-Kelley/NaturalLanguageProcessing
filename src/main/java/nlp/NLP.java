package nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import nlp.analyzers.TreeAnalyzer;

public class NLP {
	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		FileOutputStream fileOut = new FileOutputStream(new File("resources/abraham_lincoln.dat"));

		Annotation document = parser.processFile(new File("resources/abraham_lincoln.txt"));

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values
		// with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		Collection<Frame> frames = new LinkedList<>();

		for (CoreMap sentence : sentences) {
			//analyzeDependencies(sentence);
			frames.addAll(new TreeAnalyzer(sentence.get(TreeAnnotation.class)).analyze());
		}

		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(frames);
		objectOut.close();
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