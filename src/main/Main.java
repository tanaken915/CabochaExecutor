package main;

import java.util.List;

import parser.cabocha.Cabocha;

public class Main {


	public static void main(String[] args) {
		Cabocha cabocha = new Cabocha();

		String text = "クジラは哺乳類である。";

		List<String> result = cabocha.parse(text);

		result.forEach(System.out::println);
	}
}