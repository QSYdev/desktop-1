package view;

import libterminal.lib.terminal.Terminal;
import libterminal.patterns.observer.Event;
import libterminal.patterns.observer.EventListener;
import libterminal.patterns.visitor.event.ExternalEventHandler;

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

public final class QSYFrame extends JFrame implements AutoCloseable {

	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 550;
	private static final int HEIGHT = 600;

	private final EventHandler eventHandler;

	private final SearchPanel searchPanel;
	private final CommandPanel commandPanel;
	private final RoutinePanel routinePanel;
	private final StressPanel stressPanel;

	private final Terminal libterminal;

	public QSYFrame(Terminal terminal) {
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

		searchPanel = new SearchPanel(this);
		commandPanel = new CommandPanel(this);
		routinePanel = new RoutinePanel(this);
		stressPanel = new StressPanel(this);

		final Container rightPane = new Container();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(commandPanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		rightPane.add(routinePanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		rightPane.add(stressPanel);
		rightPane.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE), new Dimension(0, Integer.MAX_VALUE)));

		final JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(searchPanel, BorderLayout.CENTER);
		contentPane.add(rightPane, BorderLayout.EAST);
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		commandPanel.setEnabled(false);

		this.eventHandler = new EventHandler();
		setVisible(true);
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public CommandPanel getCommandPanel() {
		return commandPanel;
	}

	public Terminal getLibterminal() {
		return libterminal;
	}

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    @Override
	public void close() {
		searchPanel.close();
		commandPanel.close();
		eventHandler.close();
	}

    private final class EventHandler extends EventListener<Event.ExternalEvent> implements Runnable, ExternalEventHandler, AutoCloseable {

	    private final Thread thread;
	    private boolean running;

	    public EventHandler() {
	        this.running = true;

	        this.thread = new Thread(this, "View Task");
	        thread.start();
        }

        @Override
        public void run() {
            while(running) {
                try {
                    final Event.ExternalEvent event = getEvent();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            event.acceptHandler(EventHandler.this);
                        }
                    });
                } catch (final InterruptedException e) {
                    running = false;
                }
            }
        }

        @Override
        public void handle(Event.Touche event) {
            System.out.println("El nodo " + event.getToucheArgs().getPhysicalId() + " ha sido tocado");
        }

        @Override
        public void handle(Event.ConnectedNode event) {
            searchPanel.addNewNode(event.getPhysicalId(), event.getNodeAddress());
        }

        @Override
        public void handle(Event.DisconnectedNode event) {
	        searchPanel.removeNode(event.getPhysicalId(), event.getNodeAddress());
        }

        @Override
        public void close() {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
