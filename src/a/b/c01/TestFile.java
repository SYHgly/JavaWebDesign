package a.b.c01;

import java.io.File;
import java.io.IOException;

public class TestFile {
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 10000; i++) {
			File f=new File("c:\\1111\\"+i+".txt");
			f.createNewFile();
		}
		System.out.println("OK");
	}
}
