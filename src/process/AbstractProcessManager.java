package process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractProcessManager {
	private static final ProcessBuilder PB = new ProcessBuilder();

	/* 外部プロセスを起動するコマンド */
	private Process process;

	
	/** 外部プロセス開始 */
	protected void startNewProcess(List<String> command) {
		PB.command(command);
		try {
			process = PB.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 外部プロセス終了 */
	protected void finishPresentProcess(WaitTime wait) {
		try {
			process.waitFor(wait.getTime(), wait.getUnit());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** 実行中，入力待ちの外部プロセスに文字列を入力する */
	protected void writeToProcess(String input) {
		try (OutputStream os = process.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);) {
			osw.write(input);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 実行中，入力待ちの外部プロセスに文字列を入力する */
	protected void writeAllToProcess(List<String> inputs) {
		try (OutputStream os = process.getOutputStream();
				PrintWriter pw = new PrintWriter(os);) {
			for (String input : inputs)
				pw.println(input);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 外部プロセスの標準出力をList<String>として読み込む */
	protected List<String> readFromProcess() {
		try (InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);) {
			return br.lines().collect(Collectors.toList());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	/** 外部プロセスのエラー出力をList<String>として読み込む */
	protected List<String> readErrorFromProcess() {
		try (InputStream is = process.getErrorStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);) {
			return br.lines().collect(Collectors.toList());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/** 外部プロセスの標準出力をファイルに書き出す */
	protected void writeOutput2File(Path path) {
		try (InputStream is = process.getInputStream()) {
			Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);	// ファイルが存在するなら上書き
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}