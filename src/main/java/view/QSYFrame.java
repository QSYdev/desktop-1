package view;

import libterminal.api.TerminalAPI;
import libterminal.lib.node.Node;
import libterminal.lib.terminal.Terminal;
import libterminal.patterns.observer.Event;
import libterminal.patterns.observer.EventListener;
import libterminal.patterns.visitor.EventHandler;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class QSYFrame extends JFrame implements AutoCloseable, EventListener {

	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 550;
	private static final int HEIGHT = 600;

	private final EventHandler eventHandler;

	private final SearchPanel searchPanel;
	private final CommandPanel commandPanel;
	private final RoutinePanel routinePanel;

	private final TerminalAPI libterminal;

	public QSYFrame(TerminalAPI terminal) {
		super("QSY Packet Sender");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, WIDTH, HEIGHT);
		setLocationRelativeTo(null);

		this.libterminal = terminal;
		this.eventHandler = new InternalEventHandler();

		searchPanel = new SearchPanel(this);
		commandPanel = new CommandPanel(this);
		routinePanel = new RoutinePanel(this);

		final Container rightPane = new Container();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(commandPanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		rightPane.add(routinePanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		final JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(searchPanel, BorderLayout.CENTER);
		contentPane.add(rightPane, BorderLayout.EAST);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		commandPanel.setEnabled(false);

		setVisible(true);
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public CommandPanel getCommandPanel() {
		return commandPanel;
	}

	public TerminalAPI getLibterminal() {
		return libterminal;
	}

	private void newNodeCreated(final Node node) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				searchPanel.addNewNode(node);
			}
		});
	}

	private void removeDisconectedNode(final Node node) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				searchPanel.removeNode(node);
			}
		});
	}

	@Override
	public void close() throws Exception {
		searchPanel.close();
		commandPanel.close();
	}

	@Override
	public void receiveEvent(final Event event) {
        event.acceptHandler(eventHandler);
	}

    private final class InternalEventHandler extends EventHandler {

        @Override
        public void handle(final Event.NewNodeEvent event) {
            super.handle(event);
            newNodeCreated(event.getNode());
        }

        @Override
        public void handle(final Event.DisconnectedNodeEvent event) {
            super.handle(event);
            removeDisconectedNode(event.getNode());
        }
    }
}
