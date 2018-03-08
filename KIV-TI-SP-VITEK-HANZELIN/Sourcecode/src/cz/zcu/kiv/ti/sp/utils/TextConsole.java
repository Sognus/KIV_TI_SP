package cz.zcu.kiv.ti.sp.utils;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class TextConsole extends OutputStream {

	private TextArea output;

	public TextConsole(TextArea ta) {
		this.output = ta;
	}

	@Override
	public void write(int i) throws IOException {
		Platform.runLater(() -> output.appendText(String.valueOf((char) i)));
	}
}