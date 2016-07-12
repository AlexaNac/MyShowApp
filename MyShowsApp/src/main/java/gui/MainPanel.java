package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import connection.User;
import database.DatabaseHandler;
import database.DatabaseOperations;
import gui.MainFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class MainPanel extends JPanel {

	private final MainFrame rootFrame;
	private JTextArea textArea = new JTextArea();
	private DefaultListModel listModel = new DefaultListModel();
	private JList list = new JList();


	public MainPanel(MainFrame rootFrame) {
		this.rootFrame = rootFrame;
		setLayout(null);
		initComponents();
		
	}
	private void initComponents(){
		
		this.rootFrame.setSize(630,350);
		this.rootFrame.setTitle("MyShowsApp");
		this.rootFrame.setResizable(false);
		setBounds(100, 100, 625, 340);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(22, 24, 210, 280);
		add(scrollPane);
		
		JScrollPane scrollPaneDetails = new JScrollPane();
		scrollPaneDetails.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneDetails.setBounds(397, 21, 202, 283);
		add(scrollPaneDetails);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				textArea.setText(null);
			}
		});
		
		list.setModel(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		SwingWorker<Void,String> worker = new SwingWorker<Void, String>()
		{
			@Override
			protected Void doInBackground() throws Exception 
			{
				ArrayList<String> result = DatabaseOperations.getUserShows(User.getInstance());
				for(int i=0; i<result.size(); i++){
					listModel.addElement(result.get(i));
				}
				return null;
			}
		};
		worker.execute();
		scrollPane.setViewportView(list);
		
		textArea.setBounds(397, 21, 202, 283);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPaneDetails.setViewportView(textArea);

		
		JButton addButton = new JButton("Add Show");
		addButton.setBounds(253, 58, 124, 23);
		add(addButton);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootFrame.changePanel(new AddPanel(rootFrame));
			}
		});
		
		JButton deleteButton = new JButton("Delete Show");
		deleteButton.setBounds(252, 92, 125, 23);
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SwingWorker<Void,String> worker = new SwingWorker<Void, String>()
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						
						DatabaseOperations.deleteShow(User.getInstance(),list.getSelectedValue().toString());
						listModel.remove(list.getSelectedIndex());
						return null;
					}
				};
				worker.execute();
			}
		});
		add(deleteButton);

		JButton signoutButton = new JButton("Sign Out");
		signoutButton.setBounds(252, 24, 123, 23);
		signoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rootFrame.changePanel(new LoginPanel(rootFrame));
			}
		});
		add(signoutButton);
		
		JButton detailsButton = new JButton("Show Details");
		detailsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SwingWorker<Void,String> worker = new SwingWorker<Void, String>()
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						String details = DatabaseOperations.getDetails(list.getSelectedValue().toString());
						textArea.setText(null);
						textArea.append(details);
						//list.clearSelection();
						return null;
					}
				};
				worker.execute();
			}
		});
		detailsButton.setBounds(253, 126, 124, 23);
		add(detailsButton);
		
		JButton tinderButton = new JButton("TINDER Show");
		tinderButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingWorker<Void,String> worker = new SwingWorker<Void, String>()
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						ArrayList<String> Tinder = DatabaseOperations.getTinder(User.getInstance(),list.getSelectedValue().toString());
						textArea.setText(null);
						if(Tinder.size()==0){
							textArea.append("There are no other users with the same interests as you for this show. We are sorry :(");
						}
						else {
							for(int i=0; i<Tinder.size(); i++){
								textArea.append(Tinder.get(i)+ "\n");
							}
						}
						
						//list.clearSelection();
						return null;
					}
				};
				worker.execute();
			}
		});
		tinderButton.setBounds(253, 160, 124, 23);
		add(tinderButton);
		
		
	}
}
