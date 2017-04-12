import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jfree.data.xy.XYSeries;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;

public class pingGraph {
	JTextField fieldInterval;
	
	private JFrame frame;
	private JTextField textField;
	private java.util.Timer timer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pingGraph window = new pingGraph();
					window.frame.setVisible(true);
					window.frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public pingGraph() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Ping graph");
		frame.setBounds(400, 200, 450, 300);
		frame.setSize(600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		//This panel houses the graph.
		JPanel graphPanel = new JPanel();
		GridBagConstraints gbc_graphPanel = new GridBagConstraints();
		gbc_graphPanel.gridwidth = 2;
		gbc_graphPanel.fill = GridBagConstraints.BOTH;
		gbc_graphPanel.insets = new Insets(0, 0, 5, 5);
		gbc_graphPanel.gridx = 0;
		gbc_graphPanel.gridy = 0;
		
		XYSeries xy = new XYSeries("");
		xy.add(0,20);
		graph newGraph = new graph(xy);
		graphPanel.add(newGraph.getPane());
		
		frame.getContentPane().add(graphPanel, gbc_graphPanel);
		graphPanel.setLayout(new MigLayout("", "[]", "[]"));
		graphPanel.setSize(new Dimension(400,400));
		
		JPanel priorityPanel = new JPanel();
		GridBagConstraints gbc_priorityPanel = new GridBagConstraints();
		gbc_priorityPanel.insets = new Insets(0, 0, 5, 0);
		gbc_priorityPanel.gridx = 1;
		gbc_priorityPanel.gridy = 1;
		frame.getContentPane().add(priorityPanel, gbc_priorityPanel);
		FlowLayout fl_priorityPanel = (FlowLayout) priorityPanel.getLayout();
		fl_priorityPanel.setAlignment(FlowLayout.RIGHT);
		
		JCheckBox chckbxAlwaysOnTop = new JCheckBox("Always on top?");
		chckbxAlwaysOnTop.setSelected(true);
		chckbxAlwaysOnTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AbstractButton absB = (AbstractButton) arg0.getSource();
				if (absB.getModel().isSelected()) {
					frame.setAlwaysOnTop(true);
				} else {
					frame.setAlwaysOnTop(false);
				}
			}
		});
		priorityPanel.add(chckbxAlwaysOnTop);
		chckbxAlwaysOnTop.setHorizontalAlignment(SwingConstants.CENTER);
		
		//This panel contains the address, "go" and "stop buttons.
		JPanel addressPanel = new JPanel();
		FlowLayout fl_addressPanel = (FlowLayout) addressPanel.getLayout();
		fl_addressPanel.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_addressPanel = new GridBagConstraints();
		gbc_addressPanel.gridwidth = 2;
		gbc_addressPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_addressPanel.insets = new Insets(0, 0, 5, 5);
		gbc_addressPanel.anchor = GridBagConstraints.NORTH;
		gbc_addressPanel.gridx = 0;
		gbc_addressPanel.gridy = 2;
		frame.getContentPane().add(addressPanel, gbc_addressPanel);
		
		JPanel listPanel = new JPanel();
		addressPanel.add(listPanel);

		textField = new JTextField();

		String[] array = {"Google", "Bing", "Localhost", "Custom"};
		JComboBox<Object> comboBox = new JComboBox<Object>(array);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((String) comboBox.getSelectedItem() == "Google") textField.setText("google.com");
				if((String) comboBox.getSelectedItem() == "Bing") textField.setText("bing.com");
				if((String) comboBox.getSelectedItem() == "Localhost") textField.setText("localhost");
				if((String) comboBox.getSelectedItem() == "Custom") {
					textField.setText("");
					textField.setEditable(true);
				} else {
					textField.setEditable(false);
				}
			}
		});
		listPanel.add(comboBox);
		
		JPanel ipPanel = new JPanel();
		addressPanel.add(ipPanel);
		
		textField.setText("google.com");
		ipPanel.add(textField);
		textField.setColumns(10);
		textField.setEditable(false);
		
		JPanel btnPanel = new JPanel();
		addressPanel.add(btnPanel);
		
		addGoButton(newGraph,btnPanel);
		
		JPanel pointPanel = new JPanel();
		FlowLayout fl_pointPanel = (FlowLayout) pointPanel.getLayout();
		fl_pointPanel.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_pointPanel = new GridBagConstraints();
		gbc_pointPanel.gridwidth = 2;
		gbc_pointPanel.insets = new Insets(0, 0, 5, 5);
		gbc_pointPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_pointPanel.anchor = GridBagConstraints.NORTH;
		gbc_pointPanel.gridx = 0;
		gbc_pointPanel.gridy = 3;
		frame.getContentPane().add(pointPanel, gbc_pointPanel);
		
		JLabel lblFastest = new JLabel("Fastest");
		pointPanel.add(lblFastest);
		
		JTextField fieldFastest = new JTextField();
		fieldFastest.setText("20");
		pointPanel.add(fieldFastest);
		fieldFastest.setColumns(3);
		
		JLabel lblFast = new JLabel("Fast");
		pointPanel.add(lblFast);
		
		JTextField fieldFast = new JTextField();
		fieldFast.setText("50");
		fieldFast.setColumns(3);
		pointPanel.add(fieldFast);
		
		JLabel lblHigh = new JLabel("Slow");
		pointPanel.add(lblHigh);
		
		JTextField fieldSlow = new JTextField();
		fieldSlow.setText("100");
		fieldSlow.setColumns(3);
		pointPanel.add(fieldSlow);
		
		JLabel lblSlowest = new JLabel("Slowest");
		pointPanel.add(lblSlowest);
		
		JTextField fieldSlowest = new JTextField();
		fieldSlowest.setText("200");
		fieldSlowest.setColumns(4);
		pointPanel.add(fieldSlowest);
		
		JButton btnEditBreakPoints = new JButton("Change break points");
		btnEditBreakPoints.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newGraph.pingArr[0] = Integer.parseInt(fieldFastest.getText());
				newGraph.pingArr[1] = Integer.parseInt(fieldFast.getText());
				newGraph.pingArr[2] = Integer.parseInt(fieldSlow.getText());
				newGraph.pingArr[3] = Integer.parseInt(fieldSlowest.getText());
			}
		});
		pointPanel.add(btnEditBreakPoints);
		pointPanel.setVisible(false);
		
		JPanel timingPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) timingPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 4;
		frame.getContentPane().add(timingPanel, gbc_panel_1);
		timingPanel.setVisible(false);
		
		JLabel lblPingingOnceEvery = new JLabel("Pinging once every:");
		timingPanel.add(lblPingingOnceEvery);
		
		fieldInterval = new JTextField();
		fieldInterval.setText(".1");
		fieldInterval.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.cancel();
				ping(newGraph);
			}
		});
		timingPanel.add(fieldInterval);
		fieldInterval.setColumns(3);
		
		JLabel lblSeconds = new JLabel("seconds");
		timingPanel.add(lblSeconds);
		
		JPanel savePanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) savePanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel1 = new GridBagConstraints();
		gbc_panel1.gridwidth = 2;
		gbc_panel1.insets = new Insets(0, 0, 0, 5);
		gbc_panel1.fill = GridBagConstraints.BOTH;
		gbc_panel1.gridx = 0;
		gbc_panel1.gridy = 5;
		frame.getContentPane().add(savePanel, gbc_panel1);
		savePanel.setVisible(false);
		
		JButton btnSaveLog = new JButton("Save Log");
		btnSaveLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("serial")
				JFileChooser chooser = new JFileChooser(){
				    @Override
				    public void approveSelection(){
				        File f = getSelectedFile();
				        if(f.exists() && getDialogType() == SAVE_DIALOG){
				            int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
				            switch(result){
				                case JOptionPane.YES_OPTION:
				                    super.approveSelection();
				                    return;
				                case JOptionPane.NO_OPTION:
				                    return;
				                case JOptionPane.CLOSED_OPTION:
				                    return;
				                case JOptionPane.CANCEL_OPTION:
				                    cancelSelection();
				                    return;
				            }
				        }
				        super.approveSelection();
				    }        
				};
			    chooser.setCurrentDirectory(new File("/home/me/Documents"));
			    chooser.setSelectedFile(new File("ping.txt"));
			    int retrival = chooser.showSaveDialog(null);
			    if (retrival == JFileChooser.APPROVE_OPTION) {
			        try {
			        	FileWriter fw;
			            if(chooser.getSelectedFile().getName().endsWith(".txt")) {
							fw = new FileWriter(chooser.getSelectedFile());
			            } else {
							fw = new FileWriter(chooser.getSelectedFile()+".txt");
			            }
			            String s = "Ping";
			            for(int i=0; i<newGraph.log.size(); i++) {
			            	s += "\n";
			            	s += newGraph.log.get(i).date + " -> " + newGraph.log.get(i).address + " = " + newGraph.log.get(i).ping + "ms;";
			            }
			            
			            fw.write(s.toString());
			            fw.close();
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
			    }
			}
		});
		savePanel.add(btnSaveLog);
		
		JPanel compPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) compPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_compPanel = new GridBagConstraints();
		gbc_compPanel.insets = new Insets(0, 0, 5, 5);
		gbc_compPanel.fill = GridBagConstraints.BOTH;
		gbc_compPanel.gridx = 0;
		gbc_compPanel.gridy = 1;
		frame.getContentPane().add(compPanel, gbc_compPanel);
		
		JLabel lblShowComponents = new JLabel("Show components:");
		compPanel.add(lblShowComponents);
		
		JCheckBox chckbxAddress = new JCheckBox("Address");
		chckbxAddress.setSelected(true);
		chckbxAddress.addActionListener(new toggleVisibility(addressPanel));
		compPanel.add(chckbxAddress);
		
		JCheckBox chckbxPingPts = new JCheckBox("Ping pts");
		chckbxPingPts.addActionListener(new toggleVisibility(pointPanel));
		compPanel.add(chckbxPingPts);
		
		JCheckBox chckbxTiming = new JCheckBox("Timing");
		chckbxTiming.addActionListener(new toggleVisibility(timingPanel));
		compPanel.add(chckbxTiming);
		
		JCheckBox chckbxSave = new JCheckBox("Save");
		chckbxSave.addActionListener(new toggleVisibility(savePanel));
		compPanel.add(chckbxSave);
		
		
	}
	
	/**
	 * Add "go" button, starting ping Graph
	 */
	public void addGoButton(graph graph, JPanel panel) {
		JButton btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ping(graph);
				
				panel.removeAll();
				panel.revalidate();
				addStopButton(graph, panel);
			} 
		} );
		panel.add(btnGo);
		panel.revalidate();
	}
	
	/**
	 * Begin ping task
	 */
	public void ping(graph graph) {
		timer = new java.util.Timer();
		java.util.TimerTask task = new java.util.TimerTask() {
	        @Override
	        public void run() {
	            graph.update(graph.ping(textField.getText()));
	        }
		};
		double timing = Double.parseDouble((fieldInterval.getText())) *1000;
		if(timing<1) timing = 1;
		timer.schedule(task, java.util.Calendar.getInstance().getTime(), (long) timing);
	}
	
	/**
	 * Add "stop" button, stopping ping Graph
	 */
	public void addStopButton(graph graph, JPanel panel) {
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				timer.cancel();
				
				panel.removeAll();
				panel.revalidate();
				addGoButton(graph, panel);
			} 
		} );
		panel.add(btnStop);
		panel.revalidate();
	}
	
	class toggleVisibility implements ActionListener {
		private JPanel panel;
		
		toggleVisibility(JPanel panel) {
			this.panel = panel;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AbstractButton absB = (AbstractButton) arg0.getSource();
			if (absB.getModel().isSelected()) {
				panel.setVisible(true);
				frame.setSize(frame.getSize().width, frame.getSize().height + 35);
			} else {
				panel.setVisible(false);
				frame.setSize(frame.getSize().width, frame.getSize().height - 35);
			}
		}
    }
}
