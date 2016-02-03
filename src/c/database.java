package c;
	import java.io.IOException;
import java.sql.*;



/**
 * 
 * @author Administrator
 * @see 1.使用前收件建立一个对象 
 * @see 2.调用getConnection方法连接数据库 
 * @see 3.接下来可调用其他相关的方法
 */
public class database
{
	static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String URL = "jdbc:sqlserver://localhost;integratedSecurity=true;Database=mydb";
	static final String USER = "sa";
	static final String PWD = "bigpig";
	private static int count=0;
	private Connection conn;
	private boolean isTabExist=false;
	public int GetCount()
	{
		return count;
	}

	protected void finalize()
	{
		count--;
	}
	private boolean searchtab(String tablename)
	{
		try
		{
			ResultSet rs  = conn.getMetaData().getTables(null, null,  "userpwd", null );
			if (rs.next()) 
			{
				//yourTable exist
				isTabExist=true;
				return true;
			}
		
		else {
			 	//yourTable not exist
				isTabExist=false;
				return false;
			 } 
		}
		catch(SQLException e)
		{
			//e.printStackTrace();
			isTabExist=false;
			return false;
		}
			
	}
	/**
	 * 像已建立连接的的数据库添加一个用户
	 * @param aname 用户名
	 * @param apwd  密码
	 */
	public void adduser(String aname,String apwd) //增加用户
	{
		PreparedStatement ast;
		try {
			ast=conn.prepareStatement("insert into userpwd values (?,?)");
			ast.setString(1, aname);
			ast.setString(2, apwd);
			ast.executeUpdate();
			System.out.println("插入成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}
	/**
	 * 建立与数据库的连接
	 * @throws SQLException
	 */
	public void getConnection() throws SQLException //连接数据库
	{
		Connection acon = null;
		try {
		Class.forName(DRIVER);//java反射建立对象
		acon = DriverManager.getConnection(URL);
		} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (SQLException e) {
		// TODO Auto-generated catch block
			System.out.println("数据库连接失败"+e.toString());
			throw e;
		}
		System.out.println("连接成功");
		conn=acon;
		count++;
		searchtab("userpwd");
	}
	
	public void InitTable() throws DbNoConn,SQLException //初始化数据库相关表格
	{
			Statement ast;
			if (conn==null)
			{
				throw new DbNoConn();
			}
			else
			{
				if (isTabExist)  
				{

					//yourTable exist
					;
				}
				else {						 	//yourTable not exist
						ast=conn.createStatement();
						ast.execute("USE mydb");
						ast.execute("create table userpwd(name varchar(20) not null primary key,password varchar(20) not null)");
					 } 
			}

	}
	/**
	 * 验证用户名密码
	 * @param name 用户输入的用户名
	 * @param password 用户输入的密码
	 * @return 返回值：0.用户不存在1.密码错误2.密码正确
	 */
	public short UserValidation(String name,String password)
	{
		if(isUserExist(name))
		{
			try 
			{
				PreparedStatement ast=conn.prepareStatement("select password from userpwd where name=?");
				ast.setString(1, name); //将上方的SQL语句中的第一个问号填充为name
				ResultSet rs=ast.executeQuery();
				//rs.getString(0);
				rs.next();//最初rs集的指针指向空，next可以移动至第一行
				if(rs.getString(1).equals(password))
				{
					return 2; //密码正确
				}
				else
				{
					//System.out.println("库密码："+rs.getString(1)+"  "+"输入密码："+password);					
					return 1; //密码错误
				}
			}
				
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;//用户不存在
			}
		}
		else
			return 0;//用户不存在
	}
	
	private boolean isUserExist(String name)
	{
		try {
			PreparedStatement ast=conn.prepareStatement("select * from userpwd where name=?");
			ast.setString(1, name);
			ResultSet rs=ast.executeQuery();
			if (rs.next()) 
			{
				//yourTable exist
			
				return true;
			}
		
			else {
			 	//yourTable not exist
				return false;
			 } 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
			
}
		

	


