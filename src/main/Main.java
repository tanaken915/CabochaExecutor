package main;

import java.util.Arrays;
import java.util.List;

import parser.cabocha.Cabocha;

public class Main {


	public static void main(String[] args) {
		Cabocha cabocha = new Cabocha();

		List<String> texts = Arrays.asList(
				"クジラは哺乳類である。", 
				"クジラは海に住む。", 
				"クジラは小魚を食べる。",
				"クジラは大きくて可愛い。",
				"クジラは尾びれが横向き。"
				);
		
		List<String> result = cabocha.parse(texts);

		result.forEach(System.out::println);
	}
}