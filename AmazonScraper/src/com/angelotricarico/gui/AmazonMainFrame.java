package com.angelotricarico.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.angelotricarico.AmazonScraper;
import com.angelotricarico.bean.AmazonItem;
import com.angelotricarico.constants.Constants;
import com.angelotricarico.utils.AmazonUtility;
import com.angelotricarico.utils.Browser;

public class AmazonMainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JProgressBar percentProgressBar;
	private JProgressBar indeterminateProgressBar;
	private JPanel progressPanel;
	private JPanel tablePanel;

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

		setTitle(Constants.APP_TITLE);
		// Table model
		final DefaultTableModel model = new DefaultTableModel() {
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

		progressPanel = new JPanel();
		contentPane.add(progressPanel);
		progressPanel.setLayout(new GridLayout(2, 1, 0, 0));

		percentProgressBar = new JProgressBar();
		progressPanel.add(percentProgressBar);
		percentProgressBar.setForeground(new Color(255, 165, 0));
		percentProgressBar.setStringPainted(true);
		percentProgressBar.setValue(100);

		indeterminateProgressBar = new JProgressBar();
		indeterminateProgressBar.setValue(1);
		indeterminateProgressBar.setToolTipText("Loading Items...");
		indeterminateProgressBar.setForeground(new Color(0, 0, 102));
		progressPanel.add(indeterminateProgressBar);
		indeterminateProgressBar.setIndeterminate(true);

		tablePanel = new JPanel();
		contentPane.add(tablePanel);
		tablePanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane);

		table = new JTable(model);
		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					String itemUrl = (String) model.getValueAt(table.rowAtPoint(me.getPoint()), 4);
					try {
						Browser.openWebpage(new URL(itemUrl));
					} catch (MalformedURLException e) {
						AmazonUtility.log("ERROR: " + e);
					}
				}
			}
		});

		scrollPane.setViewportView(table);

		AmazonScraper as = new AmazonScraper();

		startLoadingItemsThread(as, model);

		startLoadingItemsProgressBarThread(as);

	}

	private void startLoadingItemsThread(final AmazonScraper as, final DefaultTableModel model) {
		// Loading items
		new Thread(new Runnable() {
			@Override
			public void run() {

				List<AmazonItem> amazonItemList = new ArrayList<AmazonItem>();
				while (true) {
					indeterminateProgressBar.setVisible(false);

					// Fetching item list
					as.doFillAmazonItemList(amazonItemList);

					// Removing all current items
					int rowCount = model.getRowCount();
					for (int i = rowCount - 1; i >= 0; i--) {
						model.removeRow(i);
					}

					// Filling table again
					for (AmazonItem amazonItem : amazonItemList) {
						Object[] amazonItemRow = { amazonItem.getHighestScore(), amazonItem.getScore(),
								amazonItem.getPrice(), amazonItem.getTitle(), amazonItem.getUrl() };
						model.addRow(amazonItemRow);
					}

					indeterminateProgressBar.setVisible(true);

					// Waiting some time
					try {
						Thread.sleep(AmazonScraper.MINUTES_PAUSE_FOR_HISTORY_BUILDING * 60 * 1000);
					} catch (InterruptedException e) {
						AmazonUtility.log("ERROR: " + e);
					}
				}
			}
		}).start();
	}

	private void startLoadingItemsProgressBarThread(final AmazonScraper as) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					getProgressBar().setValue(as.getPercent());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						AmazonUtility.log("ERROR: " + e);
					}
				}
			}
		}).start();
	}

	public JProgressBar getProgressBar() {
		return percentProgressBar;
	}

	public JProgressBar getIndeterminateProgressBar() {
		return indeterminateProgressBar;
	}

}
