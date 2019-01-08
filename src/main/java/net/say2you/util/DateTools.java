package net.say2you.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTools {
	public static Date longTime2Date(long time) throws ParseException {
		
		SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	       Long formatTime=new Long(time);
	       String d = format.format(formatTime);
	       Date date=format.parse(d);
	       return date;
	}

}
