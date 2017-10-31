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

        Annotation document = processString(pipeline, "Pick up that block.");

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for(CoreMap sentence : sentences) {
        	sentence.get(TokensAnnotation.class).forEach(NLP::analyseTokens);

        	analyseTree(sentence);

        	analyzeDependencies(sentence);

          // next step, need to identify further components of sentence


//          IndexedWord subject = null;
//          String quantifier = null;
//          Boolean goodToGo = false;
//          Boolean TopLevelNegation = false;
//          Boolean PredicateLevelNegation = false;
//          List<Pair<GrammaticalRelation,IndexedWord>> s = dependencies.childPairs(predicate);
//          for (Pair<GrammaticalRelation,IndexedWord> item : s) {
//        	  if (item.first.toString().equals("nsubj")) {
//        		  subject = item.second;
//        	  }
//        	  if (item.first.toString().equals("cop") && (item.second.originalText().toLowerCase().equals("are") || item.second.originalText().toLowerCase().equals("is"))) {
//        		  goodToGo = true;
//        	  }
//        	  if (item.first.toString().equals("neg")) {
//        		  PredicateLevelNegation = true;
//        	  }
//          }
//
//          List<Pair<GrammaticalRelation,IndexedWord>> t = dependencies.childPairs(subject);
//          for (Pair<GrammaticalRelation,IndexedWord> item : t) {
//        	  if (item.first.toString().equals("det")) {
//        		  quantifier = item.second.lemma().toString();
//        	  }
//        	  if (item.first.toString().equals("neg")) {
//        		  TopLevelNegation = true;
//        	  }
//          }
//
//          if (goodToGo) {
//          String output = "";
//          if (TopLevelNegation) output += "not ";
//          if (quantifier != null) output += quantifier + "(x): " + subject.lemma().toString() + "(x) ^ ";
//          if (PredicateLevelNegation) output += "not(";
//          if (quantifier == null) {
//        	  output += predicate.originalText() + "(" + subject.lemma().toString() + ")";
//          } else {
//        	  output += predicate.originalText() + "(x)";
//          }
//          if (PredicateLevelNegation) output += ")";
//
//          System.out.println(output);
//          } else {
//        	  System.out.println("Could not translate sentence.");
//          }

        }

        analyseCorefChain(document);

    }

	private static void analyzeDependencies(CoreMap sentence) {
		printHeader("Analysing Dependencies");
		DependencyAnalyzer dependencies = new DependencyAnalyzer(sentence);
		dependencies.analyseDependencies();
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

	private static void analyseTokens(CoreLabel token) {
		printHeader("Analysing Tokens");
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