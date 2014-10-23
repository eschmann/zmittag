package io.eschmann.zmittag.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class ShellCommandTests {
	
	@Test
	public void testShouldExecuteMavenClean() throws IOException {

		ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "install");
		final Process process = pb.start();

		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		while ((line = br.readLine()) != null) {
		    System.out.println(line);       
		}
		
		System.out.println("Program terminated!");
	}

}
