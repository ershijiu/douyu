package c;
import java.sql.SQLException;

import c.database;
public class testmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		database db=new database();
				
		try{
			db.getConnection();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("数据库连接成功");
		try{
			db.InitTable();
		}
		catch(DbNoConn|SQLException e)
		{
			e.toString();
		}
		System.out.println("表创建成功");
		
		//db.adduser("xiha2", "bigpig");
		System.out.print(db.UserValidation("xiha", "bigpig"));
		System.out.print(db.UserValidation("xiha3", "bigpig"));
		System.out.print(db.UserValidation("xiha", "big"));
		
	}

}
