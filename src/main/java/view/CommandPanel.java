package view;

import libterminal.lib.protocol.CommandParameters;
import libterminal.lib.protocol.QSYPacket;
import libterminal.lib.routine.Color;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

public final class CommandPanel extends JPanel implements AutoCloseable {

	private static final long serialVersionUID = 1L;
	private static final String[] comboBoxPosibilites = { "No Color", "Red", "Green", "Blue", "Cyan", "Magenta", "Yellow", "White" };
	private static final Color[] colors = { Color.NO_COLOR, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.WHITE };

	private final JComboBox<String> comboBoxColor;
	private final JTextField textDelay;
	private final JTextField textStepId;
	private final JButton btnSendCommand;

	public CommandPanel(final QSYFrame parent) {
		this.setLayout(new GridLayout(0, 1, 2, 2));

		this.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Command"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		final JLabel lblColor = new JLabel("Color:");
		this.add(lblColor);

		comboBoxColor = new JComboBox<>();
		comboBoxColor.setModel(new DefaultComboBoxModel<>(comboBoxPosibilites));
		this.add(comboBoxColor);

		final JLabel lblDelay = new JLabel("Delay: (ms)");
		this.add(lblDelay);

		textDelay = new JTextField();
		textDelay.setHorizontalAlignment(SwingConstants.LEFT);
		textDelay.setText("0");
		textDelay.setMaximumSize(new Dimension(Integer.MAX_VALUE, textDelay.getPreferredSize().width));
		this.add(textDelay);

		final JLabel lblStepId = new JLabel("StepId :");
        this.add(lblStepId);

        textStepId = new JTextField();
        textStepId.setHorizontalAlignment(SwingConstants.LEFT);
        textStepId.setText("0");
        textStepId.setMaximumSize(new Dimension(Integer.MAX_VALUE, textStepId.getPreferredSize().width));
        this.add(textStepId);

		btnSendCommand = new JButton("Send Command");
		this.add(btnSendCommand);

		btnSendCommand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					final JTable table = parent.getSearchPanel().getTable();

					final Color color = getSelectedColorFromComboBox();
					final long delay = Long.parseLong(textDelay.getText());
					final int nodeId = (Integer) table.getValueAt(table.getSelectedRow(), 0);
					final int stepId = Integer.parseInt(textStepId.getText());
					CommandParameters commandParameters = new CommandParameters(nodeId, delay, color, stepId);
					parent.getLibterminal().sendPacket(nodeId, commandParameters, false, false);

				} catch (final NullPointerException exception) {
					JOptionPane.showMessageDialog(null, "Se debe seleccionar un color", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (final NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Se debe colocar un n�mero entero de 4 Bytes sin signo.", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (final IllegalArgumentException exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (final Exception exception) {
					exception.printStackTrace();
				}
			}
		});

	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		comboBoxColor.setEnabled(enabled);
		textDelay.setEnabled(enabled);
		textStepId.setEnabled(enabled);
		btnSendCommand.setEnabled(enabled);
		if (!enabled) {
			comboBoxColor.setSelectedIndex(0);
			textDelay.setText("0");
			textStepId.setText("0");
		}
	}

	private Color getSelectedColorFromComboBox() throws NullPointerException {
		final int index = comboBoxColor.getSelectedIndex();
		if (index < 0 || index >= colors.length) {
			throw new NullPointerException();
		}
		return colors[index];
	}

	@Override
	public void close() throws Exception {
		return;
	}
}
