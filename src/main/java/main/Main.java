package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;

import libterminal.lib.terminal.Terminal;
import view.QSYFrame;

public final class Main {

	public static void main(final String[] args) throws Exception {
		final Terminal terminal = new Terminal((Inet4Address) Inet4Address.getByName(System.getenv("MY_IP")));

		terminal.start();
		QSYFrame view = new QSYFrame(terminal);
		terminal.addListener(view.getEventHandler());
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				view.close();
				terminal.close();
			}
		});
	}

}
