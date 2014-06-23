//F74001195  陳境圃
/*
	先把檔案讀下來
	存成JSONArray
	再利用JSONObject把要用到的欄位取出來
	在做一下路名交易次數的計算
	即可做出答案
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

public class TocHw4 
{
  	private static String readAll(Reader rd) throws IOException 
	{
    		StringBuilder sb = new StringBuilder();
    		int cp;
    		while ((cp = rd.read()) != -1) 
		{
      			sb.append((char) cp);
    		}
    		return sb.toString();
  	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException 
	{
    		InputStream is = new URL(url).openStream();
    		try 
		{
      			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      			String jsonText = readAll(rd);
      			JSONArray json = new JSONArray(jsonText);
      			return json;
		} 
		finally 
		{
      			is.close();
    		}
  	}

  	public static void main(String[] args) throws IOException, JSONException 
	{
		if(args.length!=1)
		{
			System.out.println("number of arguements is wrong");
			return;
		}
	    	JSONArray json = readJsonFromUrl(args[0]);
    		JSONObject[] jsonobject = new JSONObject[json.length()];
		String[] road = new String[json.length()];
		int[] yearmonth = new int[json.length()];
		String[] finall= new String[json.length()];
		int[][] timefinal= new int[json.length()][200];
		boolean[] run = new boolean[json.length()];
		int[] money = new int[json.length()];
		int[] maxmoney = new int[json.length()];
		int[] minmoney = new int[json.length()];
		int i, j, l, first, count=0, maxcount=0, finalcount=0;
		for(i=0;i<json.length();i++)
		{
			jsonobject[i] = json.getJSONObject(i);
			road[i] = jsonobject[i].getString("土地區段位置或建物區門牌");
			yearmonth[i] = jsonobject[i].getInt("交易年月");
			money[i] = jsonobject[i].getInt("總價元");
			run[i] = false;
		}
		for(i=0;i<json.length();i++)
		{
			first = road[i].indexOf("大道");
			if( first==-1 )
			{
				first = road[i].indexOf("路");
				if( first==-1 )
	                        {
        	                        first = road[i].indexOf("街");
					if( first==-1 )
	                                {
        	                                first = road[i].indexOf("巷");
						if( first==-1 )road[i]=" ";
                                		else road[i] = road[i].substring(0,first+1);
                	                }
                        	     	else road[i] = road[i].substring(0,first+1);
				}
				else road[i] = road[i].substring(0,first+1);
			}
			else road[i] = road[i].substring(0,first+2);
//			System.out.println( road[i] + "  " + yearmonth[i] );
		}
		for(i=0;i<json.length();i++)
		{       
			maxmoney[count]=0;
			minmoney[count]=1000000000;
			if( road[i].equals(" ") )continue;
			if(run[i]==true)continue;
			run[i]=true;
			finall[count]="";
			finall[count]=finall[count].concat(road[i]);
			timefinal[count][1]=yearmonth[i];
			int k=2;

			if( money[i]>maxmoney[count])maxmoney[count]=money[i];
			if( money[i]<minmoney[count])minmoney[count]=money[i];

			for(j=i+1;j<json.length();j++)
			{
				if( run[j]==true )continue;
				if( finall[count].equals(road[j]) )
				{
					if( money[j]>maxmoney[count])maxmoney[count]=money[j];
		                        if( money[j]<minmoney[count])minmoney[count]=money[j];
					
					run[j]=true;
					boolean equal=false;
					for(l=1;l<k;l++)
					{
						if( yearmonth[j]==timefinal[count][l])equal=true;
						if( l==k-1 && equal==false )
						{
							timefinal[count][k]=yearmonth[j];
							k++;
						}
					}
				}
			}
			timefinal[count][0]=k;
			if(k>maxcount)
			{
				maxcount=k;
			}
			count++;
		}
		for(i=0;i<count;i++)
		{
			if(maxcount==timefinal[i][0])
			{
				System.out.println(finall[i]+", 最高成交價:"+maxmoney[i]+", 最低成交價:"+minmoney[i]);
			}
		}
  	}
}
