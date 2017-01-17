package com.angelotricarico.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.angelotricarico.utils.SettingsPreference;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmailAlertDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField emailTextField;
	private JLabel lbEmailAddress;

	/**
	 * Create the dialog.
	 */
	public EmailAlertDialog() {
		setTitle("Email Alert - Input your email address");
		setBounds(100, 100, 350, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		contentPanel.add(panel);

		lbEmailAddress = new JLabel("Email Address");
		panel.add(lbEmailAddress);

		emailTextField = new JTextField();
		lbEmailAddress.setLabelFor(emailTextField);
		panel.add(emailTextField);
		emailTextField.setText(SettingsPreference.loadEmailAddressAlert());
		emailTextField.setColumns(20);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SettingsPreference.saveEmailAddressAlert(getEmailTextField().getText());
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

	public JTextField getEmailTextField() {
		return emailTextField;
	}
}
