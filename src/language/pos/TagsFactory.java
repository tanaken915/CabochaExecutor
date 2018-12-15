package language.pos;

import java.util.function.Supplier;

import util.Factory;

public final class TagsFactory extends Factory<CabochaTags> {
	private static final TagsFactory FACTORY = new TagsFactory();
	public static TagsFactory getInstance() {
		return FACTORY;
	}
	
	public CabochaTags getCabochaTags(String mainPoS, String subPoS1, String subPoS2, String subPoS3,
			String conjugation, String inflection, String infinitive, String yomi, String pronunciation) {
		Supplier<CabochaTags> construcrtion = 
				() -> new CabochaTags(mainPoS, subPoS1, subPoS2, subPoS3, 
						conjugation, inflection, infinitive, yomi, pronunciation);
		return TagsFactory.getInstance().intern(
				mainPoS, construcrtion, 
				mainPoS, subPoS1, subPoS2, subPoS3, 
				conjugation, inflection, infinitive, yomi, pronunciation);
	}
	

	/**
	 * プールにすでに同等のインスタンスがあればそれを，無ければ{@code Optional#empty}を返す. 
	 * {@code String#intern}と同じような実装
	 */
	/*
	public CabochaTags intern(String mainPoS, String subPoS1, String subPoS2, String subPoS3,
			String conjugation, String inflection, String infinitive, String yomi, String pronunciation) {
  		String key = mainPoS;
    	Set<CabochaTags> tagsSet = constantPool.computeIfAbsent(key, s -> new HashSet<>());
    	Optional<CabochaTags> tagsOpt =
    			getIfPresentSet(tagsSet, mainPoS, subPoS1, subPoS2, subPoS3,
    					conjugation, inflection, infinitive, yomi, pronunciation);
    	
    	return tagsOpt.orElseGet(() -> {
    		CabochaTags newTags = new CabochaTags(mainPoS, subPoS1, subPoS2, subPoS3,
    				conjugation, inflection, infinitive, yomi, pronunciation);
    		tagsSet.add(newTags);
    		return newTags;
    	});
  	}
  	*/

}
