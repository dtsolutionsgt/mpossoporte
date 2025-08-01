package com.dts.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateUtils {

	public DateUtils() {}

	public String sfecha(long f) {
		long vy,vm,vd;
		String s;

		vy=(long) f/100000000;f=f % 100000000;
		vm=(long) f/1000000;f=f % 1000000;
		vd=(long) f/10000;f=f % 10000;

		s="";
		if (vd>9) s=s+ String.valueOf(vd)+"/"; else s=s+"0"+ String.valueOf(vd)+"/";
		if (vm>9) s=s+ String.valueOf(vm)+"/20"; else s=s+"0"+ String.valueOf(vm)+"/20";
		if (vy>9) s=s+ String.valueOf(vy); else s=s+"0"+ String.valueOf(vy);

		return s;
	}
	
	public String sfechas(long f) {
		int vy,vm,vd;
		String s;

		vy=(int) f/100000000;f=f % 100000000;
		vm=(int) f/1000000;f=f % 1000000;
		vd=(int) f/10000;f=f % 10000;

		s="";
		if (vd>9) { s=s+ String.valueOf(vd)+"/";} else {s=s+"0"+ String.valueOf(vd)+"/";}
		if (vm>9) { s=s+ String.valueOf(vm)+"/";} else {s=s+"0"+ String.valueOf(vm)+"/";}
		if (vy>9) { s=s+ String.valueOf(vy);} else {s=s+"0"+ String.valueOf(vy);}

		return s;
	}

	public String sfechash(long f) {
		int vy,vm,vd;
		String s;

		vy=(int) f/100000000;f=f % 100000000;
		vm=(int) f/1000000;f=f % 1000000;
		vd=(int) f/10000;f=f % 10000;

		s="";
		if (vd>9) { s=s+ String.valueOf(vd)+"/";} else {s=s+"0"+ String.valueOf(vd)+"/";}
		if (vm>9) { s=s+ String.valueOf(vm);} else {s=s+"0"+ String.valueOf(vm);}

		return s;
	}

	public String shora(long vValue) {
		long h,m;
		String sh,sm;

		if (vValue==0) return "";

		h=vValue % 10000;
		m=h % 100;if (m>9) {sm= String.valueOf(m);} else {sm="0"+ String.valueOf(m);}
		h=(int) h/100;if (h>9) {sh= String.valueOf(h);} else {sh="0"+ String.valueOf(h);}

		return sh+":"+sm;
	}

    public String shorasp(long vValue) {
        long h,m;
        String sh,sm;

        if (vValue==0) return "     ";

        h=vValue % 10000;
        m=h % 100;if (m>9) {sm= String.valueOf(m);} else {sm="0"+ String.valueOf(m);}
        h=(int) h/100;if (h>9) {sh= String.valueOf(h);} else {sh="0"+ String.valueOf(h);}

        return sh+":"+sm;
    }

	public String sfechalocal(long f) {

		if (f==0) return "";

		String s=sfecha(f);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		Date date = null;
		try {
			date = sdf.parse(s);
			s = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
		} catch (Exception e) {
			s="";
		}

		return s;
	}

    public String sfechalocalmonth(long f) {
        if (f==0) return "";

        String s=monthname(f);
        f=getyear(f);

        return s+" "+f;
    }

    public String sfechalocalshort(long f) {

        if (f==0) return "";

        String s=sfecha(f);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(s);
            s = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
            s=s.substring(0,s.length()-3);
        } catch (Exception e) {
            s="";
        }

        return s;
    }

    public String sfecham(long f) {
        long vy,vm,vd;
        String s,nm;

        if (f==0) return "";

        nm=monthnamesp(f);
        vy=(long) f/100000000;f=f % 100000000;
        vm=(int) f/1000000;f=f % 1000000;
        vd=(int) f/10000;f=f % 10000;

        s="";
        if (vd>9)  s=s+ String.valueOf(vd)+"/"; else s=s+"0"+ String.valueOf(vd)+"/";
        s=s+nm+"/20";
        if (vy>9)  s=s+ String.valueOf(vy); else s=s+"0"+ String.valueOf(vy);

        return s;
    }

    public int dateDiff(long f1,long f2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date1=null,date2=null;
        long fd;

        if (f1<=0) return -1;if (f2<=0) return -1;
        if (f1<f2) {
            fd=f1;f1=f2;f2=fd;
        }

        try {
            date1 = sdf.parse(sfecha(f1));
        } catch (Exception e) {
            return -1;
        }

        try {
            date2 = sdf.parse(sfecha(f2));
        } catch (Exception e) {
            return -1;
        }

        long diffInMillisec = date1.getTime() - date2.getTime();

        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillisec);
    }

	public String univfecha(long f) {
		long vy,vm,vd,m,h;
		String s;

		//yyyyMMdd hh:mm:ss

		vy=(int) f/100000000;f=f % 100000000;
		vm=(int) f/1000000;f=f % 1000000;
		vd=(int) f/10000;f=f % 10000;
		h= (int) f/100;
		m= f % 100;

		s="20";
		if (vy>9) s=s+vy; else s=s+"0"+vy; 
		if (vm>9) s=s+vm; else s=s+"0"+vm;
		if (vd>9) s=s+vd; else s=s+"0"+vd;  
		s=s+" ";  
		if (h>9)  s=s+h;  else s=s+"0"+h;
		s=s+":";
		if (m>9)  s=s+m;  else s=s+"0"+m;
		s=s+":00";

		return s;
	}

	public String univfechash(long f) {
		long vy,vm,vd,m,h;
		String s;

		//yyyyMMdd

		f=f+0;

		vy=(long) f/100000000;
		f=f % 100000000;
		vm=(long) f/1000000;
		f=f % 1000000;
		vd=(long) f/10000;f=f % 10000;

		s="20";
		if (vy>9) s=s+vy; else s=s+"0"+vy;
		if (vm>9) s=s+vm; else s=s+"0"+vm;
		if (vd>9) s=s+vd; else s=s+"0"+vd;

		return s;
	}

	public String univfechaext(int f) {
		int vy,vm,vd;
		String s;

		//yyyyMMdd hh:mm:ss

		vy=(int) f/10000;f=f % 10000;
		vm=(int) f/100;f=f % 100;
		vd=(int) f;

		s=""+vy;
		if (vm>9) s=s+vm; else s=s+"0"+vm;
		if (vd>9) s=s+vd; else s=s+"0"+vd;
		s=vy+" "+vm+":"+vd+":00"; //#HS_20181128_1102 Agregue " "+vm+":"+vd+":00" para que devolviera la hora.

		return s;
	}

	public long fechames(long f) {
		f=(int) f/1000000;
		f=f*1000000;
		return f;
	}

	public long ffecha00(long f) {
		f=f/10000;
		f=f*10000;
		return f;
	}

	public long ffecha24(long f) {
		f=f/10000;
		f=f*10000+2359;
		return f;
	}

	public long cfecha(int year,int month, int day) {
		long c;
		c=year % 100;
		c=c*10000+month*100+day;
		return c*10000;
	}

	public int cfechaint(int year,int month, int day) {
		long c;
		int cc;
		c=year % 100;
		c=c*10000+month*100+day;
		cc=(int) c;
		return cc;
	}

	public long parsedate(long date,int hour,int min) {
		long f;
		f=date+100*hour+min;
		return f;
	}

	public int getyear(long f) {
        long vy;

		vy=(long) f/100000000;f=f % 100000000;
		vy=vy+2000;

		return (int) vy;
	}

	public int getmonth(long f) {
        long vy,vm;

		vy=(long) f/100000000;f=f % 100000000;
		vm=(long) f/1000000;f=f % 1000000;

		return (int) vm;
	}

	public int getday(long f) {
        long vy,vm,vd;

		vy=(long) f/100000000;f=f % 100000000;
		vm=(long) f/1000000;f=f % 1000000;
		vd=(long) f/10000;f=f % 10000;

		return (int) vd;
	}

    public int gethour(long f) {
		long vy,vm,vd,vh;

        vy=(long) f/100000000;f=f % 100000000;
        vm=(long) f/1000000;f=f % 1000000;
        vd=(long) f/10000;f=f % 10000;
        vh=(long) f/100;

        return (int) vh;
    }

    public int getmin(long f) {
		long vy,vm,vd,vh,vmi;

        vy=(long) f/100000000;f=f % 100000000;
        vm=(long) f/1000000;f=f % 1000000;
        vd=(long) f/10000;f=f % 10000;
        vh=(long) f/100;vmi=(int) f % 100;

        return (int) vmi;
    }

    public int getmindif(long f1,long f2) {
        int ff,t1,t2,vh,vmi;

        ff=(int) f1 % 10000;
        vh=(int) ff/100;vmi=(int) ff % 100;t1=60*vh+vmi;

        ff=(int) f2 % 10000;
        vh=(int) ff/100;vmi=(int) ff % 100;t2=60*vh+vmi;

        ff=t1-t2;if (ff<0) ff=-ff;
        return ff;
    }

	public int LastDay(int year,int month) {
		int m,y,ld;

		m=month % 2;
		if (m==1) {
			ld=31;
		} else {
			ld=30;
		}

		if (month==2) {
			ld=28;
			if (year % 4==0) {ld=29;}
		}

		return ld;

	}

	public long addDays(long f,int days){
        long cyear,cmonth,cday;

		final Calendar c = Calendar.getInstance();

		c.set(getyear(f), getmonth(f)-1, getday(f));
		c.add(Calendar.DATE, days);

		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);

		f=cfecha((int) cyear,(int) cmonth,(int) cday);

		return f;
	}

	public String dayweek(long f) {
		int y,m,d;

		y=getyear(f);
		m=getmonth(f)-1;
		d=getday(f);

		Calendar c = new GregorianCalendar(y,m-1,d,1,0,0);

		String dn = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

		dn = dn.substring(0,1).toUpperCase() + dn.substring(1).toLowerCase();

		return dn;
	}

	public int dayofweek(long f) {
		int y,m,d,dw;

		final Calendar c = Calendar.getInstance();

		c.set(getyear(f), getmonth(f)-1, getday(f));

		dw=c.get(Calendar.DAY_OF_WEEK);

		if (dw==1) dw=7;else dw=dw-1;

		return dw;
	}

	public String monthname(long f) {
		int y,m,d;

		y=getyear(f);
		m=getmonth(f)-1;
		d=getday(f);

		Calendar c = new GregorianCalendar(y,m,d,1,0,0);

		String dn = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

		dn = dn.substring(0,1).toUpperCase() + dn.substring(1).toLowerCase();

		return dn;
	}

    public String monthnamesp(long f) {
        int y,m,d;

        Locale spanish = new Locale("es", "ES");

        y=getyear(f);
        m=getmonth(f)-1;
        d=getday(f);

        Calendar c = new GregorianCalendar(y,m,d,1,0,0);

        String dn = c.getDisplayName(Calendar.MONTH, Calendar.LONG, spanish);

        dn = dn.substring(0,1).toUpperCase() + dn.substring(1,3).toLowerCase();

        return dn;
    }

    public String dayweekshort(long f) {
		int y,m,d;
	     
		if (f==0) return "";
		
		y=getyear(f);
		m=getmonth(f);
		d=getday(f);
		
		Calendar c = new GregorianCalendar(y,m-1,d,1,0,0);
			
	    String dn = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
	    
	    dn = dn.substring(0,1).toUpperCase() + dn.substring(1).toLowerCase();
	    
	    return dn;
	}
	
	public long getActDate(){
        long cyear,cmonth,cday;
		long f;		

		final Calendar c = Calendar.getInstance();
		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);

		f=cfecha((int) cyear,(int) cmonth,(int) cday);

		return f;
	}

	public long getActDateTime(){
		int cyear,cmonth,cday,ch,cm; 
		long f;

		final Calendar c = Calendar.getInstance();
		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);
		ch=c.get(Calendar.HOUR_OF_DAY);
		cm=c.get(Calendar.MINUTE);

		f=cfecha(cyear,cmonth,cday);
		f=f+ch*100+cm;

		return f;
	}

	public int getActDateInt(){
		long cyear,cmonth,cday;
		int ff;

		final Calendar c = Calendar.getInstance();
		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);

		ff=cfechaint((int) cyear,(int) cmonth,(int) cday);

		return ff;
	}

	public int getActWeek(){
	    int cweek;

        final Calendar c = Calendar.getInstance();
        cweek = c.get(Calendar.WEEK_OF_YEAR);
        return cweek;
    }

    public int getWeek(long f){
        int cweek;

        final Calendar c = Calendar.getInstance();

        c.set(getyear(f), getmonth(f)-1, getday(f));
        cweek = c.get(Calendar.WEEK_OF_YEAR);
        return cweek;
    }

    public long getZuluTime(){
		int cyear,cmonth,cday,ch,cm;
		long f;

		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);
		ch=c.get(Calendar.HOUR_OF_DAY);
		cm=c.get(Calendar.MINUTE);

		f=cfecha(cyear,cmonth,cday);
		f=f+ch*100+cm;

		return f;
	}

	public String getActDateStr(){
		int cyear,cmonth,cday;
		long f;

		final Calendar c = Calendar.getInstance();
		cyear = c.get(Calendar.YEAR);
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);

		f=cfecha(cyear,cmonth,cday);

		return sfecha(f);
	}

	public long getCorelBase(){
		int cyear,cmonth,cday,ch,cm,cs,vd,vh;
		long f;

		final Calendar c = Calendar.getInstance();

		cyear = c.get(Calendar.YEAR);cyear=cyear % 10;
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);
		ch=c.get(Calendar.HOUR_OF_DAY);
		cm=c.get(Calendar.MINUTE);
		cs=c.get(Calendar.SECOND);

		vd=cyear*384+cmonth*32+cday;
		vh=ch*3600+cm*60+cs;

		f=vd*100000+vh;
		f=f*100;

		return f;
	}

	public String getCorelTimeStr(){
		int cyear,cmonth,cday,ch,cm,cs,vd,vh;
		long f;
		
		Calendar c = Calendar.getInstance();

		cyear = c.get(Calendar.YEAR);cyear=cyear % 10;
		cmonth = c.get(Calendar.MONTH)+1;
		cday = c.get(Calendar.DAY_OF_MONTH);
		ch=c.get(Calendar.HOUR_OF_DAY);
		cm=c.get(Calendar.MINUTE);
		cs=c.get(Calendar.SECOND);

		vd=cyear*384+cmonth*32+cday;
		vh=ch*3600+cm*60+cs;

		f=vd*100000+vh;

		return ""+f;
	}

    //region Fecha larga

    public long fechalarga(int year,int month, int day) {
        long c;

        c=year % 10000;
        c=c*10000+month*100+day;
        return c;
    }

    public String sfechaLarga(long f) {
              long vy,vm,vd;
        String sy,sm,sd;

        if (f==0) return "--/--/--";

        vy=(long) f/10000;f=f % 10000;
        vm=(long) f/100;vd=f % 100;

        sy=""+vy;
        if (vm>9) sm=""+vm; else sm="0"+vm;
        if (vd>9) sd=""+vd; else sd="0"+vd;

        return sd+"/"+sm+"/"+sy;
    }

    public String sfechaLargalocal(long f) {
        int vy,vm,vd;
        String sy,sm,sd;

        if (f==0) return "";

		f=f/10000;
        vy=(int) (f/10000);
		vy=2000+vy;
		f=f % 10000;
        vm=(int) (f/100);vd=(int) (f % 100);
        f=cfecha(vy,vm,vd);

        String s=sfecha(f);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(s);
            s = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        } catch (Exception e) {
            s="";
        }

        return s;
    }

    public int fechalargaYear(long f) {
        long vy=(long) f/10000;
        return (int) vy;
    }

    public int fechalargaMonth(long f) {

        f=f % 10000;
        long vm=(long) f/100;

        return (int) vm;
    }

    public int fechalargaDia(long f) {
        long vm,vd;

        f=f % 10000;
        vm=(long) f/100;
        vd=f % 100;

        return (int) vd;
    }

    public long fechalargaDate(){
        int cyear,cmonth,cday;
        long f;

        final Calendar c = Calendar.getInstance();
        cyear = c.get(Calendar.YEAR);
        cmonth = c.get(Calendar.MONTH)+1;
        cday = c.get(Calendar.DAY_OF_MONTH);

        f=fechalarga(cyear,cmonth,cday);

        return f;
    }

    //endregion

}