package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import cabocha.Cabocha;

public class Main {
	public static void main(String[] args) {
		/*
		List<String> texts = Arrays.asList(
				"クジラは哺乳類である。", 
				"クジラは海に住む。" ,
				"クジラは小魚を食べる。",
				"クジラは大きくて可愛い。",
				"クジラは尾びれが横向き。"
				);
		//*/

		Path p = Paths.get("../OntologyGenerator/resource/input/goo/text/gooText生物-動物名-All.txt");
		Path p2 = Paths.get("");
		
		Cabocha cabocha = new Cabocha();

		List<String> result = cabocha.parse(p);

		System.out.println(result.size());
		//result.forEach(System.out::println);
	}
}