package org.pujolitoo.CModpackDownloader.ui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Toolkit;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
/*
 * Created by JFormDesigner on Wed Aug 25 22:58:57 CEST 2021
 */



/**
 * @author aaa
 */
public class Output extends JDialog {
	public Output(Window owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - aaa
		button1 = new JButton();
		textField1 = new JTextField();
		label1 = new JLabel();
		button2 = new JButton();

		//======== this ========
		setModal(true);
		setResizable(false);
		Container contentPane = getContentPane();

		this.addWindowListener(new java.awt.event.WindowAdapter(){
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent){
				path = null;
			}
		});

		//---- button1 ----
		button1.setText("Continue");

		//---- label1 ----
		label1.setText("Please enter an output file path (.zip):");

		//---- button2 ----
		button2.setText("...");

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(34, 34, 34)
					.addGroup(contentPaneLayout.createParallelGroup()
						.addComponent(label1, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(textField1, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(button2, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addComponent(button1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(label1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button2))
					.addGap(12, 12, 12)
					.addComponent(button1)
					.addGap(25, 25, 25))
		);
		pack();
		setLocationRelativeTo(getOwner());
		button2.addActionListener((ActionListener) new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				chooseFile();
			}

		});

		button1.addActionListener((ActionListener) new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				continueDialog();
			}

		});
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void continueDialog(){
		if(!textField1.getText().equals("")){
			if(textField1.getText().endsWith(".zip")){
				path = new File(this.textField1.getText());
				if(path.exists()){
					int reply = JOptionPane.showConfirmDialog(this, "This file already exists. Do you want to override the file?", "Override", JOptionPane.YES_NO_OPTION);
					if(reply==JOptionPane.YES_OPTION){
						this.overrides = true;
					}else{
						return;
					}
				}
				this.dispose();
			}else{
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "Not a valid type.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}else{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Please enter an output path.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void chooseFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Select where to save the file.");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip files", "zip");
		fileChooser.setFileFilter(filter);
		int returnval = fileChooser.showSaveDialog(this);
		if(returnval == JFileChooser.APPROVE_OPTION){
			this.textField1.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	public File getPath(){
		return path;
	}

	public boolean getOverride(){
		return overrides;
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - aaa
	private boolean overrides;
	private JButton button1;
	private JTextField textField1;
	private JLabel label1;
	private JButton button2;
	private File path = null;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
