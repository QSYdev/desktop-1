package main;

import java.net.Inet4Address;

import libterminal.api.TerminalAPI;
import view.QSYFrame;

public final class Main {

	public static void main(final String[] args) throws Exception {
		String ip = System.getenv("MY_IP");
		TerminalAPI libterminal = new TerminalAPI(
			(Inet4Address) Inet4Address.getByName(System.getenv("MY_IP")));

		libterminal.start();
		QSYFrame view = new QSYFrame(libterminal);
		libterminal.addListener(view);
	}

}
