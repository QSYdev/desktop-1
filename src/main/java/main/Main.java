package main;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import libterminal.lib.network.MulticastReceiver;
import libterminal.lib.network.ReceiverSelector;
import libterminal.lib.network.SenderSelector;
import libterminal.lib.protocol.QSYPacket;
import libterminal.lib.terminal.Terminal;
import view.QSYFrame;

public final class Main {

	public static void main(final String[] args) throws Exception {

		final InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("192.168.0.16"), QSYPacket.MULTICAST_PORT);
		final MulticastReceiver multicastReceiver = new MulticastReceiver(addr, QSYPacket.MULTICAST_ADDRESS);
		final Terminal terminal = new Terminal();
		final ReceiverSelector receiverSelector = new ReceiverSelector();
		final SenderSelector senderSelector = new SenderSelector(terminal.getNodes());
		final QSYFrame view = new QSYFrame(terminal);

		multicastReceiver.addListener(terminal);
		receiverSelector.addListener(terminal);
		terminal.addListener(receiverSelector);
		terminal.addListener(senderSelector);
		terminal.addListener(view);

		final Thread threadReceiveSelector = new Thread(receiverSelector, "Receive Selector");
		threadReceiveSelector.start();

		final Thread threadTerminal = new Thread(terminal, "Terminal");
		threadTerminal.start();

		final Thread threadSenderSelector = new Thread(senderSelector, "Sender Selector");
		threadSenderSelector.start();

		final Thread threadMulticastReceiver = new Thread(multicastReceiver, "Multicast Receiver");
		threadMulticastReceiver.start();

	}

}
