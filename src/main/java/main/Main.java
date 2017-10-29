package main;

import java.net.Inet4Address;

import libterminal.lib.terminal.Terminal;
import view.QSYFrame;

public final class Main {

	public static void main(final String[] args) throws Exception {
		final Terminal libterminal = new Terminal((Inet4Address) Inet4Address.getByName(System.getenv("MY_IP")));

		libterminal.start();
		QSYFrame view = new QSYFrame(libterminal);
		libterminal.addListener(view);
	}

}
