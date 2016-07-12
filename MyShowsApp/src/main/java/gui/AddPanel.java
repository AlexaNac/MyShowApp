package gui;

import javax.swing.JPanel;
import gui.MainFrame;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import connection.User;
import database.DatabaseOperations;

import java.awt.CheckboxGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;


public class AddPanel extends JPanel {

	private final MainFrame rootFrame;
	private JTextField ratingField;
	JComboBox showsBox = new JComboBox();
	ButtonGroup group = new ButtonGroup();
	JLabel nullRatingLabel = new JLabel("");


	public AddPanel(MainFrame rootFrame) {
		setForeground(Color.BLACK);
		this.rootFrame = rootFrame;
		setLayout(null);
		initComponents();
	}
		
	private void initComponents(){
			
			this.rootFrame.setSize(530,350);
			this.rootFrame.setResizable(false);
			setBounds(100, 100, 520, 340);
			setLayout(null);

			JButton backButton = new JButton("<<Back");
			backButton.setBounds(10, 11, 88, 23);
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rootFrame.changePanel(new MainPanel(rootFrame));
				}
			});
			setLayout(null);
			add(backButton);
			
			showsBox.setBounds(194, 65, 272, 20);
			add(showsBox);
			SwingWorker<Void,String> worker = new SwingWorker<Void, String>()
			{
				@Override
				protected Void doInBackground() throws Exception 
				{
					ArrayList<String> result = DatabaseOperations.getAddShows(User.getInstance());
					for(int i=0; i<result.size(); i++){
						showsBox.addItem(result.get(i));
					}
					return null;
				}
			};
			worker.execute();
			JRadioButton finnishedRButton = new JRadioButton("Finished");
			finnishedRButton.setBounds(194, 132, 79, 23);
			add(finnishedRButton);
			
			JRadioButton watchingRButton = new JRadioButton("Still watching it");
			watchingRButton.setBounds(293, 132, 114, 23);
			add(watchingRButton);
			
			JRadioButton toseeButton = new JRadioButton("To see");
			toseeButton.setBounds(409, 132, 88, 23);
			add(toseeButton);
			
		    group.add(finnishedRButton);
		    group.add(watchingRButton);
		    group.add(toseeButton);
			    
			ratingField = new JTextField();
			ratingField.setBounds(200, 196, 185, 20);
			add(ratingField);
			ratingField.setColumns(10);
			
			nullRatingLabel.setForeground(Color.RED);
			nullRatingLabel.setBounds(34, 173, 351, 14);
			add(nullRatingLabel);
			
			JButton addButton = new JButton("ADD");
			addButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					
					SwingWorker<Void,String> worker = new SwingWorker<Void, String>()
					{
						@Override
						protected Void doInBackground() throws Exception 
						{
							String showname = showsBox.getSelectedItem().toString();
							String status = new String();
							String rank = new String();
							Enumeration<AbstractButton> buttons = group.getElements();
							while(buttons.hasMoreElements()){
								AbstractButton button = buttons.nextElement();
								 if (button.isSelected()) {
						                status = button.getText();
						                break;    
						            }
							}
							if(ratingField.getText().equals(""))
								if(status.equals("To see")){
									DatabaseOperations.insertShowHistory(User.getInstance(),showname,status,0);
								}
								else{
									nullRatingLabel.setText("Add a rating for this show");
								}
							
							else{
								rank=ratingField.getText();
								int ranking = Integer.parseInt(rank);
								DatabaseOperations.insertShowHistory(User.getInstance(),showname,status,ranking);
								}
							return null;
						}
					};
					worker.execute();
					nullRatingLabel.setText(null);
					//ratingField.setText(null);
					//rootFrame.changePanel(new MainPanel(rootFrame));
					/*
					Enumeration<AbstractButton> buttons = group.getElements();
					while(buttons.hasMoreElements()){
						AbstractButton button = buttons.nextElement();
						 if (button.isSelected()) {
				                button.setSelected(false);
				                break;    
				            }
					}
					
					showsBox.setSelectedIndex(0);
					*/
				}
			});
			addButton.setBounds(132, 284, 253, 23);
			add(addButton);
			
			JLabel shownameLabel = new JLabel("Select show's name:");
			shownameLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			shownameLabel.setBounds(35, 67, 149, 17);
			add(shownameLabel);
			
			JLabel statusLabel = new JLabel("Select show's status:");
			statusLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			statusLabel.setBounds(35, 135, 149, 17);
			add(statusLabel);
			
			JLabel ratingLabel = new JLabel("Rating:");
			ratingLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			ratingLabel.setBounds(35, 198, 149, 17);
			add(ratingLabel);
			
			JLabel ratinginfoLabel = new JLabel("Give this show a rating from 1 to 5, show it how much you love it!");
			ratinginfoLabel.setBounds(35, 226, 431, 14);
			add(ratinginfoLabel);
			
			JLabel lblIfYouDidnt = new JLabel("If you didn't see it but you want to, you can leave this field blank ");
			lblIfYouDidnt.setBounds(34, 248, 432, 14);
			add(lblIfYouDidnt);
			
			
	}
}
