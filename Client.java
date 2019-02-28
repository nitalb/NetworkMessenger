import java.io.*;
import java.net.*;
import java.rmi.*;  
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.*;
import javax.swing.text.Utilities;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer; 
public class Client extends JFrame implements ActionListener
{
	public static String name;
	public static int no=0;
	String status=" ",sendmsg=" ",rcvmsg=" ",menu1=" ",ipAddress=" ",sname="";
	DataOutputStream outToServer;
	InetAddress addr;
	int mnu=0,pno;
	Socket clientSocket;
	BufferedReader inFromServer;
	JPanel jpanel;
	JButton send=new JButton("Send Image");
	JTextField status1[]=new JTextField[100],tc,tc1,t1;
	JTextField t[]=new JTextField[100];
	JButton b[]=new JButton[100],bc,b1,b2;
	JButton b3[]=new JButton[100];
	JTextArea area[]=new JTextArea[100];
	Interface stub;
	JTabbedPane tb;
	JMenu Contact;
	JLabel wel=new JLabel("Welcome");
	JPanel pn[]=new JPanel[100];
	JMenuItem create,menu[]=new JMenuItem[100];
	JMenuBar menuBar;
	JFrame image1=new JFrame();
	JLabel ima1=new JLabel(); 
	JScrollPane sp2=new JScrollPane(ima1);
	DefaultMutableTreeNode emoji[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode smiling[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode crying[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode shocking[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode s1[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode s2[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode s3[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode s4[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode s5[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode c1[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode c2[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode c3[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode c4[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode c5[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode x1[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode x2[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode x3[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode x4[]=new DefaultMutableTreeNode[100];
	DefaultMutableTreeNode x5[]=new DefaultMutableTreeNode[100];
	JScrollPane scrollPane[]=new JScrollPane[100];
	JScrollPane sp[]=new JScrollPane[100];
	JButton sendi[]=new JButton[100];
	JTree tree[]=new JTree[100];
	JComboBox cb=new JComboBox();
	JComboBox cb1=new JComboBox();
	JComboBox cb2=new JComboBox();
	JRadioButton r1,r2;
	JLabel ima= new JLabel("YOUR IMAGE:");
	JScrollPane sp1=new JScrollPane(ima);
	JFrame f,f1,image=new JFrame("Image chooser");
	public static File file;
	JFileChooser fc1;
	public void GUI() 
	{	
		try{
		setTitle("Messenger");
		tb=new JTabbedPane();
		tb.setPreferredSize(new Dimension(355,610));
		getContentPane().setBackground(Color.pink); 
		add(wel);
		menuBar = new JMenuBar();		
		Contact=new JMenu("Contacts");
		create=new JMenuItem("Create contact");
		pn=new JPanel[100];
		setLayout(new FlowLayout());
		setSize(375,700);
		setResizable(false);
		Contact.add(create);
		menuBar.add(Contact);
		setJMenuBar(menuBar);
		create.addActionListener(this);
		addWindowListener(new WindowAdapter()
		{
		public void windowClosing(WindowEvent we) 
		{
			try
			{
				stub.setStatus(ipAddress,"offline");
				clientSocket.close();
				System.exit(0);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		});
		setVisible(true);
		}
		catch(Exception e)
		{
			System.out.println("GUI:");
			e.printStackTrace();
		}
	}
	public static void main(String args[]) 
	{ 
		try
		{	
			Client tc=new Client();
			System.setSecurityManager(new RMISecurityManager());
			tc.stub=(Interface)Naming.lookup("rmi://192.168.43.68:11000/MessengerServer");
			tc.addr = InetAddress.getLocalHost();
			tc.ipAddress = tc.addr.getHostAddress();
			int i=tc.stub.checkIp(tc.ipAddress);
			if(i!=0)
			{	
				tc.stub.getsock(i);
				tc.clientSocket = new Socket("192.168.43.68", 6789);
				tc.GUI();
				tc.stub.setStatus(tc.ipAddress,"online");
				tc.addContact();
			}
			else
			{
				tc.RGUI();
				JOptionPane optionPane1 = new JOptionPane("Please register yourself",JOptionPane.WARNING_MESSAGE);
				JDialog dialog1 = optionPane1.createDialog("Not Registered");
				dialog1.setAlwaysOnTop(true); 
				dialog1.setVisible(true); 
			}
			
			while(true)
			{
				tc.receive();
			}
			
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	public void addContact()
	{
		try
		{	
			int i=0,j=0;
			i=stub.getContacts(ipAddress);
			while(j<=i-1)
			{	
				Client.name=stub.sContact(j);
				menu[mnu]=new JMenuItem(Client.name);
				Contact.add(menu[mnu]);
				menu[mnu].addActionListener(new Tab(Client.name));
				j++;
			}
		}
		catch(Exception e)
		{
			System.out.println("In addC:");
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent a) 
	{
		try
		{
			if(a.getSource()==create)
			{
				create();
			}
			if(a.getSource()==bc)
			{
				int i=0;
				Client.name=tc.getText();
				String ID=tc1.getText();
				i=stub.checkID(ID);
				if(i==1)
				{
					mnu++;
					stub.update(Client.name,ID,ipAddress);
					menu[mnu]=new JMenuItem(Client.name);
					Contact.add(menu[mnu]);
					menu[mnu].addActionListener(new Tab(Client.name));
					JOptionPane optionPane = new JOptionPane("Created Contact Successfully",JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog = optionPane.createDialog("Confirmation");
					dialog.setAlwaysOnTop(true); 
					dialog.setVisible(true); 
					f1.setVisible(false);
					f1.dispose();
				}
				else
				{
					JOptionPane optionPane2 = new JOptionPane("No UID found",JOptionPane.WARNING_MESSAGE);
					JDialog dialog = optionPane2.createDialog("Err..");
					dialog.setAlwaysOnTop(true); 
					dialog.setVisible(true); 
				}
			}	
			if(a.getSource()==b1)
			{	String c=" ";
				int i=0;
				Client tc=new Client();
				i=stub.last();
				stub.getsock(i);
				clientSocket = new Socket("192.168.43.68", 6789);
				System.out.println("IP:"+ipAddress); 
				c=stub.connection(ipAddress);
				f.setVisible(false);
				f.dispose();
				JOptionPane optionPane1= new JOptionPane("Registered Successfully.Your unique id is "+c+"\nPlease store it",JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog1 = optionPane1.createDialog("Confirmation");
				dialog1.setAlwaysOnTop(true); 
				dialog1.setVisible(true);
				GUI(); 
				stub.setStatus("online",ipAddress);
			}
			if(a.getSource()==b2)
			{
				t1.setText("");
			}
			if(a.getSource()==send)
			{
				FileInputStream fis=new FileInputStream(Client.file);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] byte1 = new byte[5000000];
				for (int readNum; (readNum = fis.read(byte1)) != -1;) 
                baos.write(byte1, 0, readNum);
				System.out.println("baos:"+baos.size());
				outToServer = new DataOutputStream(clientSocket.getOutputStream());	
				System.out.println("Size:"+outToServer.size());
				byte b[]=baos.toByteArray();
				for(int i=0;i<baos.size();i++)
				outToServer.write(b[i]);
				System.out.println("Size:"+outToServer.size());
				stub.receiveImage(ipAddress,tb.getTitleAt(tb.getSelectedIndex()),baos.size());
				System.out.println("Sent\n");
				image.setVisible(false);
				image.dispose();
				
			}
		}
		catch(Exception e)
		{
			System.out.println("3");
			e.printStackTrace();
		}
	}
	public void receive() 
	{
		try
		{
			Date dt= new Date();
			String whom;
			int in=0;
			inFromServer= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while(inFromServer.ready())
			{		
				whom=stub.from();
				in=tb.indexOfTab(whom);
				rcvmsg=inFromServer.readLine();
				if((rcvmsg.equals("Imageprocess")==false))
				{
					area[in].append(rcvmsg+"\n");
				}
				else
				{
					Client.no++;
					JOptionPane optionPane1 = new JOptionPane("Downloaded Image to img"+Client.no+".jpg",JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog1 = optionPane1.createDialog("Image received!!");
					dialog1.setAlwaysOnTop(true); 
					dialog1.setVisible(true); 
					int len=stub.getimglen();
					System.out.println("len:"+len);
					DataInputStream ini = new DataInputStream(clientSocket.getInputStream());
					byte[] b=new byte[5000000];
					System.out.println("CI:"+clientSocket.getInputStream());
					FileOutputStream fos=new FileOutputStream("img"+Client.no+".png");
					BufferedOutputStream out=new BufferedOutputStream(fos);
					System.out.println("DIN:"+ini.available());
					for(int i=0;i<=ini.available();i++)
					{
						b[i]=ini.readByte();
						out.write(b[i]);
						i++;
					}
					fos.close();
					ImageIcon rimg=new ImageIcon("img"+Client.no+".png");
					image1.setTitle("Downloaded image from "+whom);
					ima1.setText(whom+" has sent this image");
					ima1.setIcon(rimg);
					ima1.setHorizontalTextPosition(JLabel.CENTER); 
					ima1.setVerticalTextPosition(JLabel.TOP);
					sp2.setBackground(Color.pink);
					sp2.setPreferredSize(new Dimension(1000,500));
					image1.getContentPane().setBackground(Color.gray);
					image1.add(sp2);
					image1.setSize(1200,600);
					image1.invalidate();
					image1.validate();
					image1.repaint();
					image1.setVisible(true);
					image1.setResizable(false);	
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("IN RECV:");
			e.printStackTrace();
		}
	} 
	public void create()
	{	
		f1=new JFrame("Add Contact");
		f1.setSize(350,120);
		f1.setLayout(new FlowLayout());
		JLabel l= new JLabel("Name:");
		f1.add(l);
		tc=new JTextField("",25);
		f1.add(tc);
		System.out.println("LAbel:"+l);
		JLabel l1= new JLabel("Unique Id:");
		f1.add(l1);
		tc1=new JTextField("",23);
		f1.add(tc1);
		bc=new JButton("Create");
		bc.setPreferredSize(new Dimension(100,35));
		f1.add(bc);
		bc.addActionListener(this);
		f1.getContentPane().setBackground(Color.pink); 
		f1.setResizable(false);
		f1.setVisible(true);
	}
	public void RGUI()
	{
		f=new JFrame("Registration");
		f.setLayout(new FlowLayout());
		f.setSize(250,145);
		JLabel l1=new JLabel("Name:"); 
		t1=new JTextField("",15);
		JLabel l2=new JLabel("Birth:"); 
		cb.addItem(1985);
		cb.addItem(1986);
		cb.addItem(1987);
		cb.addItem(1988);
		cb.addItem(1989);
		cb.addItem(1990);
		cb.addItem(1991);
		cb.addItem(1992);
		cb.addItem(1993);
		cb.addItem(1994);
		cb.addItem(1995);
		cb.addItem(1996);
		cb.addItem(1997);
		cb.addItem(1998);
		cb.addItem(1999);
		cb.addItem(2000);
		cb.addItem(2001);
		cb.addItem(2002);
		cb.addItem(2003);
		cb.addItem(2004);
		cb.addItem(2005);
		cb1.addItem("Jan");
		cb1.addItem("Feb");cb1.addItem("Mar");cb1.addItem("Apr");cb1.addItem("May");cb1.addItem("June");cb1.addItem("July");cb1.addItem("Aug");cb1.addItem("Sept");cb1.addItem("Oct");cb1.addItem("Nov");cb1.addItem("Dec");
		cb2.addItem(1);cb2.addItem(2);cb2.addItem(3);cb2.addItem(4);cb2.addItem(5);cb2.addItem(6);cb2.addItem(7);cb2.addItem(8);cb2.addItem(9);cb2.addItem(10);cb2.addItem(11);cb2.addItem(12);cb2.addItem(13);cb2.addItem(14);cb2.addItem(15);cb2.addItem(16);cb2.addItem(17);cb2.addItem(18);cb2.addItem(19);cb2.addItem(20);cb2.addItem(21);cb2.addItem(22);cb2.addItem(23);cb2.addItem(24);cb2.addItem(26);cb2.addItem(27);cb2.addItem(28);cb2.addItem(29);cb2.addItem(30);cb2.addItem(31);
		JLabel l3=new JLabel("GENDER:"); 
		r1=new JRadioButton("Male",true);
		r2=new JRadioButton("Female",true);
		b1=new JButton("Register");
		b2=new JButton("Reset");
		b1.addActionListener(this);
		b2.addActionListener(this);
		ButtonGroup bg=new ButtonGroup();
		f.add(l1);
		f.add(t1);
		bg.add(r1);
		bg.add(r2);
		f.add(l3);
		f.add(r1);
		f.add(r2);
		f.add(l2);
		f.add(cb1);
		f.add(cb2);
		f.add(cb);
		f.add(b1);
		f.add(b2);
		f.getContentPane().setBackground(Color.pink);
		f.setVisible(true);
		f.setResizable(false);
	}
	public void imageChooser()
	{
		image.setLayout(new FlowLayout());
		image.getContentPane().setBackground(Color.gray);
		send.setPreferredSize(new Dimension(150,100));
		send.addActionListener(this);
		image.add(send);
		fc1 = new JFileChooser();
		int returnVal = fc1.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			Client.file = fc1.getSelectedFile();
			sname = Client.file.getAbsolutePath();
			ImageIcon image1=new ImageIcon(sname);
			ima.setIcon(image1);
		}
		ima.setHorizontalTextPosition(JLabel.CENTER); 
		ima.setVerticalTextPosition(JLabel.TOP);
		sp1.setBackground(Color.pink);
		sp1.setPreferredSize(new Dimension(1000,500));
		image.add(sp1);			
		image.setSize(1200,600);
		image.invalidate();
		image.validate();
		image.repaint();
		image.setVisible(true);
	}
	public class Tab extends AbstractAction
	{
		public Tab(String name)
		{
			super(Client.name);
		}
		public void actionPerformed(ActionEvent a)
		{	
			try
			{
				pno=tb.getTabCount();
				String n=a.getActionCommand(),status;
				area[pno]=new JTextArea(25,25);
				area[pno].setEditable(false);
				sp[pno]=new JScrollPane(area[pno]);
				area[pno].setBounds(25,25,25,25);
				t[pno]=new JTextField("",20);
				status1[pno]=new JTextField("",10);
				sendi[pno]=new JButton("Send Image");
				sendi[pno].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a)
				{
					imageChooser();
				}
				});
				b[pno]=new JButton("Send");
				b[pno].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a)
				{
					try
					{
						Date dt=new Date();
						sendmsg = t[tb.getSelectedIndex()].getText();
						outToServer = new DataOutputStream(clientSocket.getOutputStream());	
						outToServer.writeBytes(sendmsg +"\n");
						String dat=dt.toString();
						dat=dat.substring(4,20)+dat.substring(26,28);
						stub.receiveFromClient(ipAddress,tb.getTitleAt(tb.getSelectedIndex()));
						area[tb.getSelectedIndex()].append("\t\t"+sendmsg+"\n"+"\t\t"+dat+"\n");
						t[tb.getSelectedIndex()].setText("");
						outToServer.flush();
					}
					catch(Exception e)
					{
						System.out.println("In sending:");
						e.printStackTrace();
					}
				}
				});	
				b3[pno]=new JButton("Delete");
				b3[pno].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent a)
				{
					try 
					{
						int offset = area[pno].getCaretPosition();
						int rowStart = Utilities.getRowStart(area[pno], offset);
						int rowEnd = Utilities.getRowEnd(area[pno], offset);
						Document document = area[pno].getDocument();
						int len = rowEnd - rowStart + 1;
						if (rowStart + len > document.getLength()) 
						{
							len--;
						}
						document.remove(rowStart, len);
						}
					catch (Exception ex) 
					{
                            ex.printStackTrace();
                    }
		
				}	
				});
				emoji[pno] = new DefaultMutableTreeNode("EMOJIS");
				smiling[pno] = new DefaultMutableTreeNode("SMILING");
				crying [pno]= new DefaultMutableTreeNode("CRYING");
				s1[pno]= new DefaultMutableTreeNode("~(^.^)~");
				s2[pno]= new DefaultMutableTreeNode(":)");
				s3[pno]= new DefaultMutableTreeNode(";)");
				s4[pno]= new DefaultMutableTreeNode("(^V^)");
				s5[pno]= new DefaultMutableTreeNode("<(^,^)>");
				c1[pno]= new DefaultMutableTreeNode(":(");
				shocking[pno]= new DefaultMutableTreeNode("SHOCKING");
				c2[pno]= new DefaultMutableTreeNode(";(");
				c3[pno]= new DefaultMutableTreeNode("(+.+)");
				c4[pno]= new DefaultMutableTreeNode("('_')");
				c5[pno]= new DefaultMutableTreeNode("('-')");
				x1[pno]= new DefaultMutableTreeNode("~(!.!)~");
				x2[pno]= new DefaultMutableTreeNode("~(#.#)~");
				x3[pno]= new DefaultMutableTreeNode("~(@,@)~");
				x4[pno]= new DefaultMutableTreeNode("(*.*)");
				x5[pno]= new DefaultMutableTreeNode("($.$)");
				emoji[pno].add(smiling[pno]);
				emoji[pno].add(crying[pno]);
				emoji[pno].add(shocking[pno]);
				smiling[pno].add(s1[pno]);
				smiling[pno].add(s2[pno]);
				smiling[pno].add(s3[pno]);
				smiling[pno].add(s4[pno]);
				smiling[pno].add(s5[pno]);
				crying[pno].add(c1[pno]);    
				crying[pno].add(c2[pno]);
				crying[pno].add(c3[pno]);
				crying[pno].add(c4[pno]);    
				crying[pno].add(c5[pno]);   
				shocking[pno].add(x1[pno]);    
				shocking[pno].add(x2[pno]);   
				shocking[pno].add(x3[pno]);    
				shocking[pno].add(x4[pno]);    
				shocking[pno].add(x5[pno]);
				tree[pno] = new JTree(emoji[pno]);
				scrollPane[pno]= new JScrollPane(tree[pno]);
				scrollPane[pno].setPreferredSize(new Dimension(150,75));
				status1[pno].setEditable(false);
				pn[pno]=new JPanel();
				pn[pno].add(status1[pno]);
				pn[pno].add(sp[pno]);
				pn[pno].add(t[pno]);
				pn[pno].add(b[pno]);
				pn[pno].add(sendi[pno]);
				pn[pno].add(b3[pno]);
				pn[pno].add(scrollPane[pno]);
				pn[pno].setBackground(Color.pink);
				pn[pno].setSize(330,700);
				tb.addTab(n,pn[pno]);
				add(tb);
				setVisible(true);
				status=stub.getStatus(tb.getTitleAt(tb.getSelectedIndex()),ipAddress);
				status1[tb.getSelectedIndex()].setText(status);
				area[tb.getSelectedIndex()].append(stub.Chatc(Client.name,ipAddress));
				tree[pno].getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
				{	
				String e=" ";
				public void valueChanged(TreeSelectionEvent tse){
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree[pno].getLastSelectedPathComponent();
					e=selectedNode.getUserObject().toString();
					if(e.equals("EMOJIS") || e.equals("SMILING") || e.equals("SHOCKING") || e.equals("CRYING"))
					e="";
					t[pno].setText(t[pno].getText()+e);
				}
				});
				tb.addChangeListener(changeListener);
			}
			catch(Exception e)
			{
				System.out.println("In tab:");
				e.printStackTrace();
			}
	}
	ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent changeEvent) 
		{
			try
			{
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				status=stub.getStatus(sourceTabbedPane.getTitleAt(sourceTabbedPane.getSelectedIndex()),ipAddress);
				status1[sourceTabbedPane.getSelectedIndex()].setText(status);
		}
		catch(Exception e)
		{
			System.out.println("Change:");
			e.printStackTrace();
		}
		}
	};
	}
}