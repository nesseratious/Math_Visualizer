package com.esie;
import com.esie.math.*;

public class Main {

	public static void main(String... args) {

		InitialisationInterface initialisationInterface = new MathInit();

		Thread formsThread = new Thread(() -> {
			try {
				initialisationInterface.initForms();
			} catch (Exception ignored) { }
		});

		Thread logicThread = new Thread(() -> {
			try {
				formsThread.join();
				initialisationInterface.initThreads();
			} catch (Exception ignored) { }
		});

		formsThread.start();
		logicThread.start();
	}
}
