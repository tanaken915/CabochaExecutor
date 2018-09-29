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
		this.cost 			= "100";	// 適当
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

	
	/* ================================================== */
	/* ===================== Setter ===================== */
	/* ================================================== */
	public void setSurface(String surface) {
		this.surface = surface;
	}
	public void setLeft_id(String left_id) {
		this.left_id = left_id;
	}
	public void setRight_id(String right_id) {
		this.right_id = right_id;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public void setPoS(String poS) {
		PoS = poS;
	}
	public void setSubPoS1(String subPoS1) {
		this.subPoS1 = subPoS1;
	}
	public void setSubPoS2(String subPoS2) {
		this.subPoS2 = subPoS2;
	}
	public void setSubPoS3(String subPoS3) {
		this.subPoS3 = subPoS3;
	}
	public void setConjugation(String conjugation) {
		this.conjugation = conjugation;
	}
	public void setInflection(String inflection) {
		this.inflection = inflection;
	}
	public void setInfinitive(String infinitive) {
		this.infinitive = infinitive;
	}
	public void setYomi(String yomi) {
		this.yomi = yomi;
	}
	public void setPronounciation(String pronounciation) {
		this.pronounciation = pronounciation;
	}

}