package base.swing;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener {
	public static Dimension s;
	
	private JPanel drawingPane;

	MainFrame(String title, JPanel drawingPane, Dimension size, Image icon) {
		super(title);
		setFocusable(true);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(Color.white);
		this.drawingPane = drawingPane;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		//Create and set up the content pane.
		setContentPane(this.drawingPane);
		this.drawingPane.repaint();
		
		s = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Display the window.
		if(size != null) {setSize(size);} else {setSize((int)(s.width / 1),(int)(s.height / 1));}
		
		setLocation((int)((s.width - this.getWidth())/2), (int)((s.height - this.getHeight())/2));
		setFocusable(true);
		setAutoRequestFocus(true);
		
		if (icon != null) {
        	setIconImage(icon);
		}
		
		UIManager.put("swing.boldMetal", Boolean.TRUE);
		//UIManager.put("ToolTip.background", Color.WHITE);
	}
	
	public synchronized JPanel getDrawingPane() {
		return drawingPane;
	}
	
	public static void main(String argz[]) {
		//test
		System.exit(0);
		MainFrame mf = new MainFrame("Test", new JPanel(), new Dimension(500, 500), null);
		mf.setVisible(true);
		
	}
	
	/*
	public static void main(String argz[]) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					
					//Create and set up the window
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 
					//Create and set up the content pane.
					frame.setContentPane(drawingPane);
					drawingPane.repaint();
					
					//Display the window.
					frame.setSize(480, 480);
					
					Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setSize((int)(s.height / 1.5),(int)(s.height / 1.5));
					frame.setLocation((int)(s.width / 3), (int)(s.height / 6));
					frame.setFocusable(true);
					frame.setAutoRequestFocus(true);
					
					java.net.URL imgURL1 = MainFrame.class.getResource("Lava!.png");
			        if (imgURL1 != null) {
			        	try {frame.setIconImage(ImageIO.read(imgURL1));} catch (IOException e) {util.DebugConsole.appendErrToConsole("Couldn't find file: " + "Lava!.png" + ".");}
			        } else {util.DebugConsole.appendErrToConsole("Couldn't find icon file: " + "Lava!.png" + ".");}
					
			        frame.setVisible(true);
			        
					UIManager.put("swing.boldMetal", Boolean.TRUE);
					UIManager.put("ToolTip.background", Main.BorderUnselectedC);
					UIManager.put("ToolTip.foreground", Main.UnselectedTextC);
			}});
	}
	*/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
