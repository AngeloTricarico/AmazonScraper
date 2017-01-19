package com.angelotricarico.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.angelotricarico.bean.AmazonItem;
import com.angelotricarico.constants.Constants;
import com.angelotricarico.scraper.AmazonScraper;
import com.angelotricarico.utils.AmazonUtility;
import com.angelotricarico.utils.Browser;
import com.angelotricarico.utils.SettingsPreference;

public class AmazonMainFrame extends JFrame {

	private static final long serialVersionUID = -2536333424450897613L;

	private JPanel contentPane;
	private JTable tResultsTable;
	private JProgressBar pbPercent;
	private JPanel progressPanel;
	private JPanel tablePanel;
	private JLabel lbTimerLabel;
	private JMenuBar menuBar;
	private JMenu mLanguage;
	private JMenu mEmailAlert;
	private JPanel menuBarPanel;
	private JRadioButtonMenuItem rdbtnLanguage;

	AmazonScraper as;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AmazonMainFrame frame = new AmazonMainFrame();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					AmazonUtility.log("ERROR: " + e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AmazonMainFrame() {

		as = new AmazonScraper(SettingsPreference.loadNation());

		setTitle(Constants.APP_TITLE + " - Loading from www.amazon." + SettingsPreference.loadNation());

		// Table model
		final DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = -5742396885506787986L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// All cells are not editable
				return false;
			}
		};

		model.addColumn(Constants.TABLE_HEADER_HIGHEST_SCORE);
		model.addColumn(Constants.TABLE_HEADER_CURRENT_SCORE);
		model.addColumn(Constants.TABLE_HEADER_PRICE);
		model.addColumn(Constants.TABLE_HEADER_TITLE);
		model.addColumn(Constants.TABLE_HEADER_URL);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 444);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		menuBarPanel = new JPanel();
		contentPane.add(menuBarPanel);
		menuBarPanel.setLayout(new BorderLayout(0, 0));
		menuBarPanel.setMaximumSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 16));
		menuBarPanel.setMinimumSize(new Dimension(200, 16));

		menuBar = new JMenuBar();
		menuBarPanel.add(menuBar);

		mLanguage = new JMenu("Select domain");
		menuBar.add(mLanguage);

		final ButtonGroup buttonGroup = new ButtonGroup();
		for (String nation : AmazonScraper.NATION_ARRAY) {
			rdbtnLanguage = new JRadioButtonMenuItem(nation);
			addListenerToRadioButtonItem(rdbtnLanguage, buttonGroup);
			if (nation.equals(SettingsPreference.loadNation())) {
				rdbtnLanguage.setSelected(true);
			}
			buttonGroup.add(rdbtnLanguage);
			mLanguage.add(rdbtnLanguage);
		}

		mEmailAlert = new JMenu("Email alert");
		mEmailAlert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EmailAlertDialog dialog = new EmailAlertDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		menuBar.add(mEmailAlert);

		progressPanel = new JPanel();
		contentPane.add(progressPanel);
		progressPanel.setLayout(new BorderLayout(5, 0));
		progressPanel.setMaximumSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 16));
		menuBarPanel.setMinimumSize(new Dimension(200, 16));

		pbPercent = new JProgressBar();
		pbPercent.setForeground(new Color(255, 165, 0));
		pbPercent.setStringPainted(true);
		pbPercent.setValue(100);
		progressPanel.add(pbPercent, BorderLayout.CENTER);

		lbTimerLabel = new JLabel("00");
		lbTimerLabel.setLabelFor(pbPercent);
		progressPanel.add(lbTimerLabel, BorderLayout.EAST);

		tablePanel = new JPanel();
		contentPane.add(tablePanel);
		tablePanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane);

		tResultsTable = new JTable(model);
		// tResultsTable.setAutoCreateRowSorter(true);

		tResultsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					String itemUrl = (String) model.getValueAt(tResultsTable.rowAtPoint(me.getPoint()), 4);
					try {
						Browser.openWebpage(new URL(itemUrl));
					} catch (MalformedURLException e) {
						AmazonUtility.log("ERROR: " + e);
					}
				}
			}
		});

		scrollPane.setViewportView(tResultsTable);

		startLoadingItemsThread(as, model);

		startLoadingItemsProgressBarThread(as);

	}

	private void doColorTableRows(JTable table, final AmazonScraper as) {
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
				double currentItemHighestScore = (double) table.getModel().getValueAt(row, 0);
				double globalHighestScore = as.getHighestScoreAmongAllProducts();
				setBackground(AmazonUtility.getColorForScore(currentItemHighestScore, globalHighestScore));
				setForeground(Color.BLACK);
				return this;
			}
		});
	}

	private void startLoadingItemsThread(final AmazonScraper as, final DefaultTableModel model) {
		// Loading items
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {

			public void run() {

				getLbTimerLabel().setVisible(false);
				getProgressBar().setIndeterminate(false);

				// Fetching item list
				List<AmazonItem> amazonItemList = as.downloadAmazonItemList();

				// Removing all current items
				for (int i = model.getRowCount() - 1; i >= 0; i--) {
					model.removeRow(i);
				}

				// Filling table again
				for (AmazonItem amazonItem : amazonItemList) {
					Object[] amazonItemRow = { amazonItem.getHighestScore(), amazonItem.getScore(), amazonItem.getPrice(), amazonItem.getTitle(), amazonItem.getUrl() };
					model.addRow(amazonItemRow);
				}

				doColorTableRows(tResultsTable, as);

				getLbTimerLabel().setVisible(true);
				getProgressBar().setIndeterminate(true);
				startCountdownTimer();

				AmazonUtility.sendEmailIfNewExcellentProductWasFound(as);

			}

		}, 0, AmazonScraper.MINUTES_PAUSE_FOR_HISTORY_BUILDING, TimeUnit.MINUTES);

	}

	private void startLoadingItemsProgressBarThread(final AmazonScraper as) {
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {

			public void run() {
				getProgressBar().setValue(as.getPercent());
			}

		}, 0, 1, TimeUnit.SECONDS);
	}

	private void startCountdownTimer() {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int countdownValue = AmazonScraper.MINUTES_PAUSE_FOR_HISTORY_BUILDING * 60;

			public void run() {
				getLbTimerLabel().setText("" + countdownValue--);
				if (countdownValue < 0)
					timer.cancel();
			}
		}, 0, 1000);
	}

	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}

		return null;
	}

	public void addListenerToRadioButtonItem(JRadioButtonMenuItem rbmi, final ButtonGroup buttonGroup) {
		rbmi.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String nation = getSelectedButtonText(buttonGroup);
				if (nation != null) {
					as.setNation(nation);
					SettingsPreference.saveNation(nation);
					setTitle(Constants.APP_TITLE + " - Loading from www.amazon." + SettingsPreference.loadNation());
					// Every time we switch domain we reset highest score
					SettingsPreference.saveHighestScoreEver(0.0);
				}
			}
		});
	}

	public JProgressBar getProgressBar() {
		return pbPercent;
	}

	public JLabel getLbTimerLabel() {
		return lbTimerLabel;
	}
}
