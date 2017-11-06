package nlp;

public enum Phrases {
	NOUN_PHRASE {
		@Override
		public String toString() {
			return "NP";
		}
	},
	ROOT {
		@Override
		public String toString() {
			return "ROOT";
		}
	},
	SENTENCE {
		@Override
		public String toString() {
			return "S";
		}
	},
	VERB_PHRASE {
		@Override
		public String toString() {
			return "VP";
		}
	}
}
