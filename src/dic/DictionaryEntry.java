package dic;

import java.util.Arrays;
import java.util.List;

public class DictionaryEntry {
	/** 表層形 */
	private String surface;
	/** 左文脈ID */
	private String left_id;
	/** 右文脈ID */
	private String right_id;
	/** コスト */
	private String cost;
	/** 品詞 */
	private String PoS;
	/** 品詞細分類1 */
	private String subPoS1;
	/** 品詞細分類2 */
	private String subPoS2;
	/** 品詞細分類3 */
	private String subPoS3;
	/** 活用型 */
	private String conjugation;
	/** 活用形 */
	private String inflection;
	/** 原形 */
	private String infinitive;
	/** 読み */
	private String yomi;
	/** 発音 */
	private String pronounciation;
	
	/* ================================================== */
	/* ================== Constructor =================== */
	/* ================================================== */
	public DictionaryEntry(String surface) {
		this.surface 	= surface;
	}
	
	public void setAsNoun() {
		this.left_id 		= "";
		this.right_id 		= "";
		this.cost 			= "";
		this.PoS			= "名詞";
		this.subPoS1 		= "一般";
		this.subPoS2 		= "*";
		this.subPoS3 		= "*";
		this.conjugation 	= "*";
		this.inflection 	= "*";
		this.infinitive 	= surface;
		this.yomi 			= "*";
		this.pronounciation = "*";
	}
	
	public List<String> toList() {
		return Arrays.asList(surface, left_id, right_id, cost,
				PoS, subPoS1, subPoS2, subPoS3, 
				conjugation, inflection, infinitive, yomi, pronounciation);
	}
	
	public String toCSV() {
		return String.join(",", toList());
	}
}