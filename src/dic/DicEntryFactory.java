package dic;

import java.util.function.Supplier;

import util.Factory;

public final class DicEntryFactory extends Factory<DictionaryEntry> {
	private static final DicEntryFactory FACTORY = new DicEntryFactory();
	private DicEntryFactory() {}
	public static DicEntryFactory get() { return FACTORY; }

	
	public static DictionaryEntry getDictionaryEntry(
			String surface, String left_id, String right_id, String cost, 
			String mainPoS, String subPoS1, String subPoS2, String subPoS3,
			String conjugation, String inflection, String infinitive, String yomi, String pronunciation) {
		Supplier<DictionaryEntry> construcrtion = 
				() -> new DictionaryEntry(surface, left_id, right_id, cost, 
						mainPoS, subPoS1, subPoS2, subPoS3, 
						conjugation, inflection, infinitive, yomi, pronunciation);

		return DicEntryFactory.get().intern(mainPoS, construcrtion, 
				surface, left_id, right_id, cost, 
				mainPoS, subPoS1, subPoS2, subPoS3, 
				conjugation, inflection, infinitive, yomi, pronunciation);
	}
	

	/**
	 * プールにすでに同等のインスタンスがあればそれを，無ければ{@code Optional#empty}を返す. 
	 * {@code String#intern}と同じような実装
	 */
	/*
	public DictionaryEntry intern(
			String surface, String left_id, String right_id, String cost, 
			String mainPoS, String subPoS1, String subPoS2, String subPoS3,
			String conjugation, String inflection, String infinitive, String yomi, String pronunciation
			) {
  		String key = mainPoS;		// Mapへの振り分けに使うキーを決定
    	Set<DictionaryEntry> tagsSet = constantPool.computeIfAbsent(key, s -> new HashSet<>());
    	Optional<DictionaryEntry> tagsOpt =
    			getIfPresentSet(tagsSet, 
    					surface, left_id, right_id, cost, 
    					mainPoS, subPoS1, subPoS2, subPoS3,
    					conjugation, inflection, infinitive, yomi, pronunciation);
    	
    	DictionaryEntry dicEtry = tagsOpt.orElseGet(() -> {
    		DictionaryEntry newEntry = new DictionaryEntry(
    				surface, left_id, right_id, cost, 
    				mainPoS, subPoS1, subPoS2, subPoS3,
    				conjugation, inflection, infinitive, yomi, pronunciation);
    		tagsSet.add(newEntry);
    		return newEntry;
    	});
		return dicEtry;
  	}
	*/
}
