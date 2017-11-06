package nlp;

// Taken from http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
// Please keep alphabetical
public enum PartOfSpeech {
	COORDINATING_CONJUNCTION {
		@Override
		public String toString() {
			return "CC";
		}
	},
	CARDINAL_NUMBER {
		@Override
		public String toString() {
			return "CD";
		}
	},
	DETERMINER {
		@Override
		public String toString() {
			return "DT";
		}
	},
	ADJECTIVE {
		@Override
		public String toString() {
			return "JJ";
		}
	},
	SINGULAR_NOUN {
		@Override
		public String toString() {
			return "NN";
		}
	},
	PLURAL_NOUN {
		@Override
		public String toString() {
			return "NNS";
		}
	},
	PROPER_NOUN {
		@Override
		public String toString() {
			return "NNP";
		}
	},
	PLURAL_PROPER_NOUN {
		@Override
		public String toString() {
			return "NNPS";
		}
	},
	VERB_BASE_FORM {
		@Override
		public String toString() {
			return "VB";
		}
	},
	VERB_PRESENT {
		@Override
		public String toString() {
			return "VBP";
		}
	}
}
