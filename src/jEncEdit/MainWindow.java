package jEncEdit;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;

public class MainWindow {

	private JFrame frmMainWindow;
	private JLabel lblEncodeTable;
	private JTextPane txtpnHex;
	private JTextPane txtpnIn;
	private JTextPane txtpnOut;
	private JList<ListElement> lstFileList;
	//private DefaultListModel<ListElement> lstFiles;
	private JLabel lblPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmMainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMainWindow = new JFrame();
		frmMainWindow.setBounds(100, 100, 450, 300);
		frmMainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMainWindow.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane spltpLvl1 = new JSplitPane();
		spltpLvl1.setResizeWeight(0.5);
		spltpLvl1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmMainWindow.getContentPane().add(spltpLvl1, BorderLayout.CENTER);
		
		JSplitPane spltpLvl2 = new JSplitPane();
		spltpLvl2.setResizeWeight(0.25);
		spltpLvl1.setLeftComponent(spltpLvl2);
		
		JScrollPane scrlpFileList = new JScrollPane();
		scrlpFileList.setPreferredSize(new Dimension(80, 3));
		spltpLvl2.setLeftComponent(scrlpFileList);
		
		lstFileList = new JList<ListElement>();
		lstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstFileList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
					EncEdit.readFile(lstFileList.getSelectedValue().path);
					if (EncEdit.inputBuffer.length() > 0) {
						txtpnIn.setText(EncEdit.inputBuffer.toString());
						txtpnOut.setText(EncEdit.outputBuffer.toString());
						txtpnHex.setText(EncEdit.hexBuffer.toString());
					}
			}
		});
		scrlpFileList.setViewportView(lstFileList);
		
		lblPath = new JLabel("Path");
		scrlpFileList.setColumnHeaderView(lblPath);
		
		JSplitPane spltpLvl3 = new JSplitPane();
		spltpLvl2.setRightComponent(spltpLvl3);
		spltpLvl3.setResizeWeight(0.5);
		spltpLvl3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		JScrollPane scrlpIn = new JScrollPane();
		spltpLvl3.setLeftComponent(scrlpIn);
		
		txtpnIn = new JTextPane();
		scrlpIn.setViewportView(txtpnIn);
		
		JLabel lblInput = new JLabel("Input");
		scrlpIn.setColumnHeaderView(lblInput);
		
		JScrollPane scrlpOut = new JScrollPane();
		spltpLvl3.setRightComponent(scrlpOut);
		
		txtpnOut = new JTextPane();
		scrlpOut.setViewportView(txtpnOut);
		
		JLabel lblOutput = new JLabel("Output");
		scrlpOut.setColumnHeaderView(lblOutput);
		
		JScrollPane scrlpHex = new JScrollPane();
		spltpLvl1.setRightComponent(scrlpHex);
		
		txtpnHex = new JTextPane();
		scrlpHex.setViewportView(txtpnHex);
		
		JLabel lblNumeric = new JLabel("Numeric");
		scrlpHex.setColumnHeaderView(lblNumeric);
		
		lblEncodeTable = new JLabel("Encode Table");
		frmMainWindow.getContentPane().add(lblEncodeTable, BorderLayout.NORTH);
		
		JMenuBar menuBarMain = new JMenuBar();
		frmMainWindow.setJMenuBar(menuBarMain);
		
		JMenu mnFile = new JMenu("File");
		menuBarMain.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open Directory");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Open File
				JFileChooser dialogOpen = new JFileChooser();
				dialogOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (dialogOpen.showOpenDialog(frmMainWindow) == 0) {
					lblPath.setText("Path: " + dialogOpen.getSelectedFile().getPath());
					final File dirList = new File(dialogOpen.getSelectedFile().getPath());
					DefaultListModel<ListElement> lstFiles = new DefaultListModel<ListElement>();
					for (final File dirItem : dirList.listFiles()) {
						if (dirItem.isFile()) lstFiles.addElement(new ListElement(dirItem));
					}
					lstFileList.setModel(lstFiles);
				}
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Quit the bust above my door
				System.exit(0);
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save to File");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
				StringBuffer tempBuffer = new StringBuffer();
				int lstCount = lstFileList.getModel().getSize();
				for (int i = 0; i < lstCount; i++) {
					EncEdit.readFile(lstFileList.getModel().getElementAt(i).path);
					tempBuffer.append(EncEdit.outputBuffer);
					tempBuffer.append("\n\n");
				}
				EncEdit.outputBuffer = tempBuffer;
				txtpnOut.setText(EncEdit.outputBuffer.toString());
				JFileChooser dialogSave = new JFileChooser();
				dialogSave.setSelectedFile(new File("Recoded Text.txt"));
				if (dialogSave.showSaveDialog(frmMainWindow) == 0) {
					EncEdit.writeFile(dialogSave.getSelectedFile().getPath());
				}
			}
		});
		mnFile.add(mntmSave);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		mnFile.add(mntmExit);
		frmMainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// Start-up
				lblEncodeTable.setText("Character Table: " + EncEdit.encTable);
			}
		});
	}
	protected JLabel getLblEncodeTable() {
		return lblEncodeTable;
	}
	protected JTextPane getTxtpnHex() {
		return txtpnHex;
	}
	protected JTextPane getTxtpnIn() {
		return txtpnIn;
	}
	protected JTextPane getTxtpnOut() {
		return txtpnOut;
	}
	protected JList<ListElement> getLstFileList() {
		return lstFileList;
	}
	protected JLabel getLblPath() {
		return lblPath;
	}
}

class ListElement {
	public String caption;
	public String path;
	public ListElement () {
		caption = "";
		path = "";
	}
	public ListElement (String caption) {
		this.caption = caption;
		path = "";
	}
	public ListElement (File file) {
		caption = file.getName();
		path = file.getPath();
	}
	public String toString() {
		return caption;
	}
}
