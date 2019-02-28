import java.rmi.*; 
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.net.*;
import java.util.*;
import java.rmi.server.*; 
import java.rmi.registry.*;
import java.security.*;
interface Interface extends Remote 
{
  public void receiveFromClient(String c,String to) throws Exception; 
  public String connection(String x) throws Exception;
  public void sendToClient(Socket x,String y)throws Exception; 
  public void update(String n, String id, String c) throws Exception;
  public int checkIp(String ip) throws Exception;
  public String Chatc(String to,String c) throws Exception;
  public void setStatus(String ip,String x)throws Exception;
  public String getStatus(String x,String ip)throws Exception;
  public int getContacts(String x) throws Exception;
  public int checkID(String ID) throws Exception;
  public void getsock(int soc) throws Exception;
  public String sContact(int i) throws Exception;
  public String from() throws Exception;
  public int last() throws Exception;
  public void receiveImage(String c,String to,int len) throws Exception;
  public int getimglen() throws Exception;
}
public class Server extends UnicastRemoteObject implements Interface
{
	ServerSocket wSocket ;
	public static int sock,length;
	public static int ch=0;
	public static Socket client[]=new Socket[100];
	Connection con=DriverManager.getConnection("jdbc:odbc:Server","Nital","Nitalb95");
	Interface stub;
	PreparedStatement st;
	String db,clientSentence="\n";
	ResultSet Rs,Rs1;
	int j,n,k=1001;
	String id; int i=0;
	BufferedReader inFromClient;  
	public static String c[];
	public static String whom=" ";
	Server() throws Exception
	{
		super();  
	}
	public static void main(String args[]) 
	{
		try
		{
			System.setSecurityManager(new RMISecurityManager());
			Server ts=new Server();
			ts.stub=new Server();
			Naming.rebind("rmi://127.0.0.1:11000/MessengerServer",ts.stub);   
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			ts.con=DriverManager.getConnection("jdbc:odbc:Server","Nital","Nitalb95");
			ts.wSocket = new ServerSocket(6789);
			while(true)
			{	
				ts.callsock();	
			}	
		}
		catch(Exception e)
		{
			System.out.println("In Main:");
			e.printStackTrace();
		}
	}
	public int last() throws Exception
	{	
	int i=0,j=0;	
	try
	{
		db="select Cno from MessengerServer";
		st=con.prepareStatement(db);
		Rs=st.executeQuery();
		if(Rs.next())
			i=Rs.getInt(1);
		while(Rs.next())
		{
			j=Rs.getInt(1);
			if(i<j)
				i=j;
		}
		i++;
	}
	catch(Exception e){}
		return i;
	}
	public void callsock()
	{
		try
		{
			if(Server.ch==1)
			{
				socket();
			}
		}
		catch(Exception e)
		{
			System.out.println("In call:");
			e.printStackTrace();
		}
	}
	public void getsock(int soc) throws Exception
	{
		Server.ch=1;
		Server.sock=soc;
	}
	public void socket() throws Exception
	{	
		Server ts=new Server();
		client[Server.sock] = wSocket.accept();
		Server.ch=0;
	}
	public String connection(String ip) throws Exception
	{ 
		try
		{
			int i=last();
			k=1000+i;
			id='A'+String.valueOf(k);
			db="insert into MessengerServer values(?,?,?,?)";
			st=con.prepareStatement(db);
			st.setString(1,id);
			st.setString(2,ip);
			st.setInt(3,i);
			st.setString(4,"online");
			st.executeUpdate();
			db="create table client"+i+"(ID char(15) PRIMARY KEY,CName varchar(20))";
			st=con.prepareStatement(db);
			st.executeUpdate();
			con.commit();
		}
		catch(Exception e)
		{
			System.out.println("In Connection : ");
			e.printStackTrace();
		}
		return id;
	}
	public int checkID(String ID) throws Exception
	{
		int i=0;
		st=con.prepareStatement("select cno from Messengerserver where id='"+ID+"'");
		Rs=st.executeQuery();
		if(Rs.next()==true)
		{
			i=Rs.getInt(1);
			System.out.println("i:"+i);
		}
		if(i==0)
			return 0;
		else
			return 1;
	}
	public int checkIp(String ip) throws Exception
	{ 
		String ch="A1000";
		int cno=0;
		try
		{ 
			db="select ID from MessengerServer where IP='"+ip+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
				ch=Rs.getString(1);
		}
		catch(Exception e)
		{
			System.out.println("In CandG: ");
			e.printStackTrace();
		}
		if(ch!="A1000")
		{
			st=con.prepareStatement("select Cno from MessengerServer where IP='"+ip+"'");
			Rs=st.executeQuery();
			while(Rs.next())
				cno=Rs.getInt(1);
			return cno; 
		}
		else
			return 0;
	}
	public void receiveFromClient(String c,String to)throws RemoteException,Exception
	{
		try
		{
			int n=0,t=0;
			String id=" ",t1=" ";
			Date dt=new Date();
			db="select Cno,ID from MessengerServer where IP='"+c+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
			{
			n=Rs.getInt(1);
			id=Rs.getString(2);
			}
			inFromClient=new BufferedReader(new InputStreamReader(Server.client[n].getInputStream()));
			boolean i=inFromClient.ready();
			while(inFromClient.ready())
			clientSentence =inFromClient.readLine();
			db="select Id from client"+n+" where Cname='"+to+"'";
			st=con.prepareStatement(db);  
			Rs=st.executeQuery();
			while(Rs.next())
			{
			t1=Rs.getString(1);
			}
			db="select Cno from messengerserver where id='"+t1+"'";
			st=con.prepareStatement(db);  
			Rs=st.executeQuery();
			while(Rs.next())
			{
			t=Rs.getInt(1);
			}
			t1=t1+id;
			String dat=dt.toString();
			dat=dat.substring(4,20)+dat.substring(26,28);
			db="insert into chat"+t1+" values(?,?)";
			st=con.prepareStatement(db);
			st.setString(1,dat);
			st.setString(2,clientSentence);
			st.executeUpdate();
			sendToClient(Server.client[t],clientSentence+"\n"+dat+"\n");
			System.out.println("Sent");
			st=con.prepareStatement("select CName from client"+t+" where ID='"+id+"'");
			Rs=st.executeQuery();
			while(Rs.next())
				Server.whom=Rs.getString(1);
		}
		catch(Exception e)
		{
			System.out.println("In Receive from client : ");
			e.printStackTrace();
		}
	}    
	public void receiveImage(String c,String to,int len)
	{
		try{
		String t1="",id="";
		int t=0;
		db="select Cno,ID from MessengerServer where IP='"+c+"'";
		st=con.prepareStatement(db);
		Rs=st.executeQuery();
		while(Rs.next())
		{
			n=Rs.getInt(1);
			id=Rs.getString(2);
		}
		db="select Id from client"+n+" where Cname='"+to+"'";
		st=con.prepareStatement(db);  
		Rs=st.executeQuery();
		while(Rs.next())
		{
			t1=Rs.getString(1);
		}
		db="select Cno from messengerserver where id='"+t1+"'";
		st=con.prepareStatement(db);  
		Rs=st.executeQuery();
		while(Rs.next())
		{
			t=Rs.getInt(1);
		}
		Server.length=len;
		DataInputStream in = new DataInputStream(client[n].getInputStream());	
		System.out.println("in:"+in.available());
		System.out.println("len: "+len);
		byte b[]=new byte[65536];
		for( int i=0;i<len;i++)
		{
			b[i]=in.readByte();
		}
		String Img="Imageprocess";
		sendToClient(Server.client[t],Img+"\n");
		sendImage(Server.client[t],b);
		st=con.prepareStatement("select CName from client"+t+" where ID='"+id+"'");
		Rs=st.executeQuery();
		while(Rs.next())
			Server.whom=Rs.getString(1);
		
		}catch(Exception e)
		{
			System.out.println("IN IMAGE:");
			e.printStackTrace();
		}
		
	}
	public int getimglen()
	{
		return Server.length;
	}
	public void sendImage(Socket x,byte[] y) 
	{
		try
		{
			DataOutputStream outToClient = new DataOutputStream(x.getOutputStream());
			System.out.println("16");
			for(int i=0;i<getimglen();i++)
				outToClient.write(y[i]);
			System.out.println("Size:"+outToClient.size());
			outToClient.writeBytes("EndImage\n");
			outToClient.flush();
			inFromClient=null;
		}
		catch(Exception e)
		{
			System.out.println("Insend image:");
			e.printStackTrace();
		}
	}
	public String from()
	{
		return Server.whom;  
	}
	public int getContacts(String x) throws Exception
	{   
		int n=0,i=0;
		try
		{
			c=new String[100];
			db="select Cno from MessengerServer where IP='"+x+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
				n=Rs.getInt(1);
			db="select CName from client"+n+"";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
			{
				Server.c[i]=Rs.getString(1);
					i++;
			}
			Server.c[i]="end";
		}
		catch(Exception e)
		{
			System.out.println("In gC:");
			e.printStackTrace();
		}
		return i;
	}
	public String sContact(int i) throws Exception
	{
		return Server.c[i]; 
	}
	public String Chatc(String to,String c) throws Exception
	{
		int cn=0; 
		String n=" ",msg=" ",d=" ",d1=" ",msg1=" ",msg2="",t="",m="",m1="",n1="";
		db="select ID,cno from MessengerServer where IP='"+c+"'";
		st=con.prepareStatement(db);
		Rs=st.executeQuery();
		while(Rs.next()) 
		{
			n=Rs.getString(1);
			cn=Rs.getInt(2);
		}
		db="select ID from client"+cn+" where CName='"+to+"'";
		st=con.prepareStatement(db);
		Rs=st.executeQuery();
		if(Rs.next()==true)
		{
			m=Rs.getString(1);
		}
		m1=m+n;
		db="select * from chat"+m1;
		st=con.prepareStatement(db);
		Rs=st.executeQuery();
		n1=n+m;
		String db1="select * from chat"+n1;
		PreparedStatement st1=con.prepareStatement(db1);
		Rs1=st1.executeQuery();
		while(Rs.next())
		{
			d1=Rs.getString(1);
			msg1=Rs.getString(2);
			if(msg1==null)
			{
				msg1=" ";
				msg1=msg1+"\n"+"\t\t"+d1;
			}
			if(Rs1.next())
			{
				d=Rs1.getString(1);
				msg=Rs1.getString(2);
				msg=msg+"\n"+d+"\n";
				msg2=msg2+"\t\t"+msg1+"\n"+msg;
			}	
			else
			{
				msg="";
				msg2=msg2+"\t\t"+msg1+"\n"+msg+"\n";	
			}
								
		}
		while(Rs.next()==false&&Rs1.next()==true)
		{
			msg1="";
			d=Rs1.getString(1);
			msg=Rs1.getString(2);
			msg=msg+d;
			msg2=msg2+"\t\t"+msg1+"\n"+msg+"\n";
		}
		return msg2;	
	}
	public void sendToClient(Socket x,String y) throws Exception
	{
		try
		{
			DataOutputStream outToClient = new DataOutputStream(x.getOutputStream());
			outToClient.writeBytes(y);
			inFromClient=null;
			outToClient.flush();
		}
		catch(Exception e)
		{
			System.out.println("In Send to: "+e.getMessage());
		}
	}
	public void update(String n, String id, String c) throws Exception
	{ 
		try
		{ 	
			String p=" ",m=" "; 
			int j=0;
			db="select cno,ID from MessengerServer where IP='"+c+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
			{
				j=Rs.getInt(1);
				p=Rs.getString(2);
			}
			db="insert into client"+j+" values(?,?)";
			st=con.prepareStatement(db);
			st.setString(1,id);
			st.setString(2,n);
			st.executeUpdate();
			p=p+id;
			db="create table chat"+p+"(Dttm varchar(30),Message varchar(20))";
			st=con.prepareStatement(db);
			st.executeUpdate();
			con.commit();
		}
		catch(Exception e)
		{
			System.out.println("In Update ");
			e.printStackTrace();
		}
	}
	public void setStatus(String ip,String x) throws Exception
	{
			db="update MessengerServer set status='"+x+"' where IP='"+ip+"'";
			st=con.prepareStatement(db);
			st.executeUpdate();
			con.commit();
	}
	public String getStatus(String x,String ip) throws Exception
	{
		String n=" ";
		try
		{
			int i=0;
			db="select Cno from MessengerServer where IP='"+ip+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
				i=Rs.getInt(1);
			db="select ID from client"+i+" where Cname='"+x+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
				n=Rs.getString(1);
			db="select status from MessengerServer where ID='"+n+"'";
			st=con.prepareStatement(db);
			Rs=st.executeQuery();
			while(Rs.next())
				n=Rs.getString(1);	
		}
		catch(Exception e){}
		return n;
	}
}

