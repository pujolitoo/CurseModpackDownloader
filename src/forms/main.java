import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Wed Aug 25 13:07:11 CEST 2021
 */



/**
 * @author aaa
 */
public class main extends JFrame {
	public main() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - aaa
		panel1 = new JPanel();
		panel2 = new JPanel();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		textField1 = new JTextField();
		label1 = new JLabel();
		button1 = new JButton();
		progressBar1 = new JProgressBar();

		//======== this ========
		setResizable(false);
		Container contentPane = getContentPane();

		//======== panel1 ========
		{
			panel1.setBorder(new TitledBorder("Modpack Downloader"));
			panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
			. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border. TitledBorder. CENTER, javax
			. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
			12 ), java. awt. Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans
			. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .
			getPropertyName () )) throw new RuntimeException( ); }} );

			//======== panel2 ========
			{
				panel2.setBorder(new TitledBorder("Log"));
				panel2.setLayout(new GridLayout(1, 1));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(textArea1);
				}
				panel2.add(scrollPane1);
			}

			//---- label1 ----
			label1.setText("Project Id:");

			//---- button1 ----
			button1.setText("Download");

			//---- progressBar1 ----
			progressBar1.setValue(50);
			progressBar1.setForeground(new Color(51, 204, 0));

			GroupLayout panel1Layout = new GroupLayout(panel1);
			panel1.setLayout(panel1Layout);
			panel1Layout.setHorizontalGroup(
				panel1Layout.createParallelGroup()
					.addGroup(panel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(panel1Layout.createParallelGroup()
							.addComponent(panel2, GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
							.addComponent(button1, GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
							.addGroup(panel1Layout.createSequentialGroup()
								.addComponent(label1, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 314, Short.MAX_VALUE))
							.addComponent(progressBar1, GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
						.addContainerGap())
			);
			panel1Layout.setVerticalGroup(
				panel1Layout.createParallelGroup()
					.addGroup(panel1Layout.createSequentialGroup()
						.addGap(21, 21, 21)
						.addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label1))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(panel2, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(button1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(progressBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(34, Short.MAX_VALUE))
			);
		}

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addComponent(panel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - aaa
	private JPanel panel1;
	private JPanel panel2;
	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JTextField textField1;
	private JLabel label1;
	private JButton button1;
	private JProgressBar progressBar1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
