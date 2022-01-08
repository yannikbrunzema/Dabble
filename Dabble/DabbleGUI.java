package dabble;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * The view for the Boggle app.
 *
 */
public class DabbleGUI extends JFrame implements ActionListener {

	private static final String NEW_GAME = "NEW_GAME";
	private static final String GIVE_UP = "GIVE_UP";
	private static final String EXIT = "EXIT";

	private Map<String, JButton> buttons;
	private int buttonsPressed;
	private String lastKey;
	private Dabble dab;

	/**
	 * Create the Boggle user interface. Please see the lab for a detailed
	 * description of the user interface.
	 * 
	 * @param controller the controller that listens for submit and roll events
	 */
	public DabbleGUI(Dabble dab) {
		super("Dabble");
		this.buttons = new HashMap<String, JButton>();
		this.buttonsPressed = 0;
		this.lastKey = "";
		this.dab = dab;

		this.setJMenuBar(this.makeMenu());
		JPanel contentPanel = new JPanel();
		contentPanel.add(makeButtonPanels());
		this.setWords(this.dab.getScrambledWords());
		this.setContentPane(contentPanel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private JMenuBar makeMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Dabble");
		bar.add(menu);

		this.addMenuItem(menu, "New Game", NEW_GAME);
		this.addMenuItem(menu, "Give up", GIVE_UP);
		menu.addSeparator();
		this.addMenuItem(menu, "Exit", EXIT);
		return bar;
	}

	private void addMenuItem(JMenu menu, String label, String action) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(action);
		item.addActionListener(this);
		menu.add(item);
	}

	private JPanel makeButtonPanels() {
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 32);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(5, 1));
		for (int len = 2; len <= 6; len++) {
			JPanel plen = new JPanel();
			for (int i = 0; i < len; i++) {
				JButton b = new JButton("" + i);
				b.setPreferredSize(new Dimension(100, 100));
				b.setMaximumSize(b.getSize());
				b.setFont(font);
				b.setBackground(Color.WHITE);
				String key = key(len, i);
				this.buttons.put(key, b);
				b.setActionCommand(key);
				b.addActionListener(this);
				plen.add(b);
			}
			p.add(plen);
		}
		return p;
	}

	private static String key(int length, int index) {
		return length + ":" + index;
	}

	private void setWords(Map<Integer, String> words) {
		for (int wordLen = 2; wordLen <= 6; wordLen++) {
			String word = words.get(wordLen);
			if (word == null) {
				throw new IllegalArgumentException("map missing key = " + wordLen);
			}
			if (word.length() != wordLen) {
				String err = String.format("map has word with wrong length, key = %d, word = %s", wordLen, word);
				throw new IllegalArgumentException(err);
			}
			for (int j = 0; j < wordLen; j++) {
				char c = word.charAt(j);
				String k = key(wordLen, j);
				JButton b = this.buttons.get(k);
				b.setText("" + c);
			}
		}
		if (this.dab.isSolved()) {
			JOptionPane.showMessageDialog(this, "You won!");
			this.dab = new Dabble();
			this.setWords(this.dab.getScrambledWords());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String key = e.getActionCommand();
		if (key.equals(NEW_GAME)) {
			this.dab = new Dabble();
			this.setWords(this.dab.getScrambledWords());
		} else if (key.equals(GIVE_UP)) {
			this.setWords(this.dab.getSolutionWords());
		} else if (key.equals(EXIT)) {
			this.dispose();
		} else {
			int wordLen = Integer.valueOf(key.substring(0, 1));
			int charIndex = Integer.valueOf(key.substring(2, 3));

			// change button color
			JButton b = this.buttons.get(key);
			b.setBackground(Color.CYAN);

			this.buttonsPressed++;
			if (this.buttonsPressed == 2) {
				JButton prev = this.buttons.get(this.lastKey);
				prev.setBackground(Color.WHITE);
				b.setBackground(Color.WHITE);

				int prevLen = Integer.valueOf(this.lastKey.substring(0, 1));
				int prevIndex = Integer.valueOf(this.lastKey.substring(2, 3));
				// switch letters
				this.dab.exchange(prevLen, prevIndex, wordLen, charIndex);
				this.setWords(this.dab.getScrambledWords());

				this.lastKey = "";
				this.buttonsPressed = 0;
			} else {
				this.lastKey = key;
			}
		}
	}

	public static void main(String[] args) {
		Dabble dab = new Dabble(0);
		System.out.println(dab);
		DabbleGUI gui = new DabbleGUI(dab);
		gui.setVisible(true);
	}

}
