package game;


import java.util.Scanner;


public class KeyboardCapture implements Runnable {
	private boolean running = true;
	public KeyboardCapture() {}

	@Override
	public void run() {
		this.running = true;
		System.out.print("Press Enter to end close: ");
		Scanner kbd = new Scanner(System.in);
		String readString = kbd.nextLine();
		while (readString != null) {
			System.out.println(readString);
			if (readString.equals("")) {
				this.running = false;
			}
			if (kbd.hasNextLine())
				readString = kbd.nextLine();
			else
				readString = null;
		}
		kbd.close();

	}

	public boolean isRunning() {
		return running;
	}
}
