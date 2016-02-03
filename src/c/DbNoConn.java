package c;

import java.io.IOException;

public class DbNoConn extends IOException {
	public DbNoConn()
	{
		super("Data base have not connected");
	}

}
