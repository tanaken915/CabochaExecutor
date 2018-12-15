package dic;

import java.util.Arrays;
import java.util.List;

import language.pos.CabochaTags;

public class DictionaryEntry extends CabochaTags {
	/** 表層形 */
	private final String surface;
	/** 左文脈ID */
	private final String left_id;
	/** 右文脈ID */
	private final String right_id;
	/** コスト */
	private String cost;

	/* ================================================== */
	/* ================== Constructor =================== */
	/* ================================================== */
	protected DictionaryEntry(
			String surface, String left_id, String right_id, String cost, 
			String mainPoS, String subPoS1, String subPoS2, String subPoS3,
			String conjugation, String inflection, String infinitive, String yomi, String pronunciation) {
		super(mainPoS, subPoS1, subPoS2, subPoS3, conjugation, inflection, infinitive, yomi, pronunciation);
		this.surface		= surface;
		this.left_id		= left_id;
		this.right_id		= right_id;
		this.cost			= cost;
	}
	
	public static DictionaryEntry newSimpleNounizeInstance(String surface) {
		return new DictionaryEntry(surface, "", "", "100", "名詞", "一般", "*", "*", "*", "*", surface, "*", "*");
	}

	
	/* ================================================== */
	/* ================== Member Method ================= */
	/* ================================================== */	
	public String toCSV() {
		return String.join(",", toList());
	}

	/* ================================================== */
	/* ================ Interface Method ================ */
	/* ================================================== */
	@Override
	public Object[] initArgs() {
		return new Object[] {surface, left_id, right_id, cost, mainPoS, subPoS1, subPoS2, subPoS3, conjugation, inflection, infinitive, yomi, pronunciation};
	}
	@Override
	public List<String> toList() {
		return Arrays.asList(surface, left_id, right_id, cost,
				mainPoS, subPoS1, subPoS2, subPoS3, 
				conjugation, inflection, infinitive, yomi, pronunciation);
	}

	/* ================================================== */
	/* ===================== Setter ===================== */
	/* ================================================== */	
	public void setCost(String cost) {
		this.cost = cost;
	}

}
